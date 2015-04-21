package com.map.woodlands.woodlandsmap.Data.SAXKML;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.map.woodlands.woodlandsmap.Data.Coordinate;
import com.map.woodlands.woodlandsmap.Data.KMLController;
import com.map.woodlands.woodlandsmap.Data.MarkerToggler;
import com.map.woodlands.woodlandsmap.Data.ViewToggler;
import com.map.woodlands.woodlandsmap.R;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 3/20/2015.
 */
public class MapController {
    private GoogleMap map;
    private AsyncHttpClient client;
    private CoordinatesParser coordinatesParser;
    private MarkerToggler mt;
    ArrayList<Marker> markers;
    private ViewToggler viewToggler;
    private KMLController kmlController;
    private Context mContext;

    public MapController(GoogleMap gmap, MarkerToggler m, ViewToggler viewToggler, Context context){
        this.map = gmap;
        this.client = new AsyncHttpClient();
        this.coordinatesParser = new CoordinatesParser();
        this.mt = m;
        this.markers = new ArrayList<Marker>();
        this.viewToggler = viewToggler;
        this.kmlController = new KMLController(this, context);
        this.mContext = context;
    }

    public void loadKML(final String url, final String fileName){

        viewToggler.toggleLoadingView();
        NavigationDataSet navigationDataSet = null;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("debug", "Got KML file");

                // Save it to local storage
                String filetype = url.substring(url.length()-3);
                kmlController.saveFile(bytes, fileName+"."+filetype);

                // Start loading to the map
                if(url.contains("kml")) {
                    // It is a kml file
                    addDataToMap(MapService.getNavigationDataSet(bytes));
                }else if(url.contains("kmz")){
                    // It is a kmz file
                    addDataToMap(MapService.getNavigationDataSet(kmlController.getKMLFromKMZ(bytes)));

                }
                viewToggler.toggleLoadingView();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(mContext
                        ,"Network error, try to load KML file from local storage"
                        ,Toast.LENGTH_SHORT)
                        .show();

                String fullFilename = fileName + "." + url.substring(url.length()-3);
                kmlController.loadLocalKML(fullFilename);

                viewToggler.toggleLoadingView();
            }
        });


    }

    public void addDataToMap(NavigationDataSet n){
        if(n != null) {
            ArrayList<Placemark> placemarks = n.getPlacemarks();
            ArrayList<Marker> markers = new ArrayList<Marker>();
            ArrayList<Polyline> polylines = new ArrayList<Polyline>();
            for (Placemark p : placemarks) {
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

                latLngs = coordinatesParser.getLatLngs(p);

                if (latLngs.size() > 0) {
                    if (p.getType() != null) {
                        if (p.getType().equals("Point")) {
                            // This is Point placemark

                            Marker m = map.addMarker(new MarkerOptions().position(latLngs.get(0)).title(p.title));
                            markers.add(m);

                        } else if (p.getType().equals("Polygon")) {
                            // This is Polygon placemark

                            Iterable<LatLng> iterable = latLngs;
                            Polyline polyline = map.addPolyline(new PolylineOptions().addAll(iterable).width(5).color(R.color.lightBlue));
                            polylines.add(polyline);

                        }
                    }
                }
            }
            if (markers.size() > 0) {
                autocenterPoint(markers);
            } else if (polylines.size() > 0) {
                autocenterPolygon(polylines);
            }
        }

    }

    private void autocenterPoint(ArrayList<Marker> ms){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker m : ms){
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);

    }

    private void autocenterPolygon(ArrayList<Polyline> ms){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Polyline m : ms){
            List<LatLng> temp = m.getPoints();
            builder.include(temp.get(0));
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);

    }

    public void loadAllMarkers(ArrayList<Coordinate> list){
        if(list != null){
            for(Coordinate c : list){
                Marker m = null;
                MarkerOptions mo = new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(c.Latitude), Double.parseDouble(c.Longitude)))
                        .title("Risk: " + c.Risk);
                String riskS = c.Risk.toLowerCase();
                if(riskS.contains("high")) {
                    m = map.addMarker(mo);

                }else if(riskS.contains("mod")){
                    mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_orange));
                    m = map.addMarker(mo);
                }else if(riskS.contains("low")){
                    mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green));
                    m = map.addMarker(mo);
                }else if(riskS.contains("no")){
                    mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_grey));
                    m = map.addMarker(mo);
                }else{
                    mo.title("raw");
                    mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue));
                    m = map.addMarker(mo);
                }


                if(m != null) {
//                    m.setVisible(false);
                    markers.add(m);
                }
            }
            if(markers.size()>0) {
                autocenterPoint(markers);
            }
        }

    }

    public void toggleMarkers(boolean b, String title){
        if(markers != null && markers.size()>0){
            for(Marker m : markers){
                if (m.getTitle().toLowerCase().contains(title)) {
                    m.setVisible(b);
                }
            }
            autocenterPoint(markers);
        }
    }

    public void findMyLocation(){
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    map.setOnMyLocationChangeListener(null);

                }
            }
        });


    }

    public void clear(){
        markers.clear();
        map.clear();
    }


}
