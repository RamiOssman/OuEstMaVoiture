package com.atom.rami.ouestmavoiture;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


/**
 * Created by rami on 19/10/2016.
 */
public class Updater {

    Context c;
    Location knownLocation  ;
    LocationManager locationManager;
    LocationListener locationListener ;
    ProgressDialog progressDialog ;

    public Updater(Context context , LocationListener listener) {


        c = context;

        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

        locationListener = listener ;



        if (!this.getPermission() || !isLocationAccessible())
            return;

        if(isNetworkEnabled())
        knownLocation = getNetworkLocation() ;
        else if(isGPSEnabled())
        knownLocation = getGPSLocation() ;







    }

    public boolean isLocationAccessible() {


        return isGPSEnabled() || isNetworkEnabled();


    }


    public boolean isGPSEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ;

    }
    public boolean isNetworkEnabled(){

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean getPermission() {


        if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            // Here, thisActivity is the current activity


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) c, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }

        return true;


    }

    public Location getNetworkLocation() {



        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);

        if (locationManager != null)


            if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }

        Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(l == null)
            requestLocationDialog();

        return l ;



    }

    public Location getGPSLocation(){


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);

        if (locationManager != null)
            if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }

        Location l =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(l == null)
            requestLocationDialog();

       return l ;



    }


    private void requestLocationDialog() {
         progressDialog = ProgressDialog.show(c, "S'il vous plaît, attendez",
                "On calcule vos coordonnées ", true);

        new Thread((new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("On calcule vos coordonnées...");




            }
        })).start();
        // ...
    }







}
