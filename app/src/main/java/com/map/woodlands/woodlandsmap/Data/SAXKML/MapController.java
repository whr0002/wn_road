package com.map.woodlands.woodlandsmap.Data.SAXKML;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.map.woodlands.woodlandsmap.Data.Coordinate;
import com.map.woodlands.woodlandsmap.Data.MarkerToggler;
import com.map.woodlands.woodlandsmap.R;

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
    private List<GroundOverlay> groundOverlays;
    public static final int TRASPARENCY_MAX = 100;


    public MapController(GoogleMap gmap, MarkerToggler m, Context context){
        this.map = gmap;
        this.client = new AsyncHttpClient();
        this.coordinatesParser = new CoordinatesParser();
        this.mt = m;
        this.markers = new ArrayList<Marker>();
        this.groundOverlays = new ArrayList<GroundOverlay>();
    }



    public void addDataToMap(NavigationDataSet n){
        if(n != null) {
            Log.i("debug", "adding kml to map");
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

                        }
                        else if (p.getType().equals("Polygon")) {
                            // This is Polygon placemark

                            Iterable<LatLng> iterable = latLngs;
                            Polyline polyline = map.addPolyline(new PolylineOptions().addAll(iterable).width(5).color(R.color.lightBlue));
                            polylines.add(polyline);

                        }else if(p.getType().equals("LineString")){
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
                        .title("ID: "+ c.ID + "\n Risk: "+c.Risk);
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
//                    mo.title("raw");
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
//                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    map.setOnMyLocationChangeListener(null);




                }
            }
        });


    }

    public void clear(){
        markers.clear();
        groundOverlays.clear();
        map.clear();
    }

    public void addOverlays(BitmapDescriptor bitmapDescriptor, LatLngBounds bounds){
        if(bitmapDescriptor != null && bounds != null){
            GroundOverlay groundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
            .image(bitmapDescriptor)
            .positionFromBounds(bounds)
            .transparency(0.5f)
            .visible(false));

            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            groundOverlays.add(groundOverlay);
        }

    }

    /**
     * Listening to zoom level change
     * */
    public void onCameraChanged(CameraPosition position){

//        Log.i("debug", "Zoom Level: "+position.zoom);

        for(GroundOverlay g : groundOverlays){
            if(g.getBounds().contains(position.target)){
                // Camera center is in the bound, show overlay
                g.setVisible(true);
            }else{
                g.setVisible(false);
            }
        }
    }

    public void onTransparencyChanged(int currentTransparency){

        for(GroundOverlay g: groundOverlays){
            g.setTransparency((float) currentTransparency / (float) TRASPARENCY_MAX);
        }
    }
}
