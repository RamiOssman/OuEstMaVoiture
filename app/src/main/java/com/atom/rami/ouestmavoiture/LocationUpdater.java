package com.atom.rami.ouestmavoiture;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationUpdater {


    Context context;
    public Location location;
    public Boolean Update , Wait;
    public GoogleMap maps;
    public Marker marker;
    public LatLng ll;


    public LocationUpdater(Context c , final ProgressDialog dialog) {

        Update = false;
        Wait = false ;

        context = c;


        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListner = new LocationListener() {

            public void onLocationChanged(Location lc) {


                if(lc != null) {
                    Update(true, lc);
                    dialog.dismiss();
                }


               LatLng newBeirut = new LatLng(location.getLatitude(), location.getLongitude());
               maps.moveCamera(CameraUpdateFactory.newLatLng(newBeirut));

               marker.setPosition(newBeirut);

                Log.e("Location Started" , "") ;


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override
            public void onProviderEnabled(String provider) {


            }

            @Override
            public void onProviderDisabled(String provider) {

            }


        };




        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            // Here, thisActivity is the current activity


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }

        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(context, "Veuillez indiquer votre LOCATION PROVIDER ", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;

        }

        if (isNetworkEnabled) {


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListner);

            if (locationManager != null)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }
        if (isGpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListner);

            if (locationManager != null)
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }


        //    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListner);


        //   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  1000*6*1, 10, locationListner);


        Log.e("User Permission", "ON");




    }

    public void Update(boolean status , Location lc)
    {


        location = lc ;
        Update = status ;


    }

    public void updateMarker(LatLng beirut, Marker markerr, GoogleMap mMap) {

        maps = mMap;
        marker = markerr;
        ll = beirut;

    }








}


