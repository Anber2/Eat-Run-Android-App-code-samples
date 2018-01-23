package com.mawaqaa.eatandrun.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mawaqaa.eatandrun.Constants.AppConstants;
import com.mawaqaa.eatandrun.R;

/**
 * Created by HP on 7/3/2017.
 */

public class RestaurntMapPupupFragment extends EatndRunBaseFragment implements OnMapReadyCallback {
    public static int position;
    TextView offerTitle, offerDate, offerDescription;
    ImageView offerImage;
    SupportMapFragment mapFragment;
    double latitude, longitude;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_restaurnt_map, container,
                false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

       /* mMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);*/

        mapFragment.getMapAsync(this);


        /*if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }*/

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);



        latitude = Double.parseDouble(AppConstants.Res_Latitude_);
        longitude = Double.parseDouble(AppConstants.Res_Longitude_);

        /*if (!latitudee.equalsIgnoreCase("Res_Latitude") || !longitudee.equalsIgnoreCase("Res_Longitude")) {
            latitude = Double.parseDouble(PreferenceUtil.getResLatitude(getActivity()));
            longitude = Double.parseDouble(PreferenceUtil.getResLongitude(getActivity()));
        } else {
            Toast.makeText(getActivity(), "Location is not available!", Toast.LENGTH_LONG).show();
        }*/


        // For dropping a marker at a point on the Map
        LatLng vimkw = new LatLng(latitude, longitude);


      /*  googleMap.addMarker(new MarkerOptions().position(vimkw));
        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(vimkw).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


        mMap.addMarker(new MarkerOptions().position(vimkw).title("Marker in Kuwait"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(vimkw).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}