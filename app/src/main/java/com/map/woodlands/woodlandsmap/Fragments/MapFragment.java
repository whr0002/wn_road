package com.map.woodlands.woodlandsmap.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.map.woodlands.woodlandsmap.Data.DataController;
import com.map.woodlands.woodlandsmap.Data.MarkerToggler;
import com.map.woodlands.woodlandsmap.Data.PopupController;
import com.map.woodlands.woodlandsmap.Data.SAXKML.MapController;
import com.map.woodlands.woodlandsmap.Data.ViewToggler;
import com.map.woodlands.woodlandsmap.R;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

/**
 * Created by Jimmy on 3/19/2015.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    private MapView mMapView;
    private GoogleMap map;
    private MapController mapController;
    private Context mContext;
    private PopupController popupController;
    private DataController dataController;
    private MarkerToggler markerToggler;
    private ViewToggler viewToggler;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)          // 5 seconds
            .setFastestInterval(16)     // 16ms = 60 fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        View loadingView = v.findViewById(R.id.loadingView);
        loadingView.setVisibility(View.GONE);
        viewToggler = new ViewToggler(loadingView);

        this.markerToggler = new MarkerToggler();
        mContext = this.getActivity();

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(mContext);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);

        mMapView.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();

        Spinner spinner = (Spinner) v.findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:

                View v = this.getActivity().findViewById(item.getItemId());
                popupController.showKMLPopup(v);

               return true;

            case 1:
                View v1 = this.getActivity().findViewById(0);
                popupController.showControlPopup(v1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,0,0,"Layers")
                .setIcon(R.drawable.file_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0,1,1,"Markers")
                .setIcon(R.drawable.file_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }


    public void getMapData(){
//        dataController.loadCoords(getResources().getString(R.string.coords_url),0);
        mapController.findMyLocation();
        dataController.getKML();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
//        this.map = mMapView.getMap();
        this.map = map;
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.setMyLocationEnabled(true);
        this.map.setOnMarkerClickListener(this);


        this.mapController = new MapController(this.map, markerToggler, viewToggler, mContext);
        this.dataController = new DataController(mContext, mapController, viewToggler);
        this.popupController = new PopupController(mContext, mapController, markerToggler, dataController);



        getMapData();


    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        viewToggler.toggleLoadingView();
        dataController.loadRow(marker.getPosition());
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // This is also called by the Android framework in onResume(). The map may not be created at
        // this stage yet.
        if (map != null) {
            setLayer((String) parent.getItemAtPosition(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setLayer(String layerName) {
        if (layerName.equals(getString(R.string.normal))) {
            map.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals(getString(R.string.hybrid))) {
            map.setMapType(MAP_TYPE_HYBRID);
        } else if (layerName.equals(getString(R.string.satellite))) {
            map.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals(getString(R.string.terrain))) {
            map.setMapType(MAP_TYPE_TERRAIN);
        } else if (layerName.equals(getString(R.string.none_map))) {
            map.setMapType(MAP_TYPE_NONE);
        } else {
            Log.i("debug", "Error setting layer with name " + layerName);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                REQUEST,
                this);  // LocationListener
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(mContext, location.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
