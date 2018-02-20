package com.ingreens.googlemapdemo;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    // Google Map
    private GoogleMap googleMap;
    private Marker marker;
    private String provider;
    LocationManager locManager;
    TextView address,countryName,countryCode,postalCode,adminArea,subAdminArea,locality,subThroughFare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        address=findViewById(R.id.tvAddress);
        countryName=findViewById(R.id.tvCountryName);
        countryCode=findViewById(R.id.tvCountryCode);
        adminArea=findViewById(R.id.tvAdminArea);
        postalCode=findViewById(R.id.tvPostalCode);
        subAdminArea=findViewById(R.id.tvSubAdminArea);
        locality=findViewById(R.id.tvLocality);
        subThroughFare=findViewById(R.id.tvSubThroughFare);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }


        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locManager.getBestProvider(new Criteria(), false);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }


        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);



    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locManager.removeUpdates(this);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
        //locManager.requestLocationUpdates(provider,400,1,this);
        initilizeMap();
    }*/


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker!=null){
                    marker.remove();

                }

                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("lat=="+latLng.latitude);
                System.out.println("long=="+latLng.longitude);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                Toast.makeText(MainActivity.this, "lat="+latLng.latitude +"long="+latLng.longitude, Toast.LENGTH_SHORT).show();

                MarkerOptions a = new MarkerOptions()
                        .position(new LatLng(latLng.latitude,latLng.longitude));
                marker = googleMap.addMarker(a);
                marker.setPosition(new LatLng(latLng.latitude,latLng.longitude));
                getAddress(latLng.latitude,latLng.longitude);
            }
        });

       /* LatLng kolkata=new LatLng(0,0);
        googleMap.addMarker(new MarkerOptions().position(kolkata).title("kolkata").snippet("Capital of WestBengal"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kolkata));
*/




    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("address========="+add);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            address.setText(add);
            countryName.setText(obj.getCountryName());
            countryCode.setText(obj.getCountryCode());
            adminArea.setText(obj.getAdminArea());
            postalCode.setText(obj.getPostalCode());
            subAdminArea.setText(obj.getSubAdminArea());
            locality.setText(obj.getLocality());
            subThroughFare.setText(obj.getSubThoroughfare());

           // Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "location fetched="+location.toString(), Toast.LENGTH_SHORT).show();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("my current location==="+location.toString());
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        if (marker!=null){
            marker.remove();

        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        marker = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        locManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
