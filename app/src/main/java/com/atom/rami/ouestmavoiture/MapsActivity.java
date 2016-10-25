package com.atom.rami.ouestmavoiture;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.messages.internal.Update;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    public String STORAGER =  "LOCALISATION" ;

    private GoogleMap mMap;

    LocationUpdater locationUpdater ;

    SharedPreferences settings ;

    LatLng carLL;

    Marker carMarker, marker ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        settings = getSharedPreferences(STORAGER, 0);


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ;
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ;

        if(!isGpsEnabled && !isNetworkEnabled)
        {
            Toast.makeText(this, "On a besoin de votre GPS pour positionner la voiture. ", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return ;

        }



            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        requestLocationDialog();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    ProgressDialog mProgressDialog ;


    private void requestLocationDialog() {
        mProgressDialog = ProgressDialog.show(this, "S'il vous plaît, attendez",
                "On calcule vos coordonnées ", true);

        new Thread((new Runnable() {
            @Override
            public void run() {
                mProgressDialog.setMessage("On calcule vos coordonnées...");




            }
        })).start();
        // ...
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {







        locationUpdater = new LocationUpdater(this , mProgressDialog) ;


        while(mProgressDialog.isShowing())
        {

            continue ;
        }

        displayData(googleMap);





        //    UpdatePosition() ;


    }

    public void displayData(GoogleMap googleMap) {


        LatLng beirut = new LatLng(locationUpdater.location.getLatitude(), locationUpdater.location.getLongitude());


        marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("walker", 50, 50)))
                .anchor(0.4f, 0.4f) // Anchors the marker on the bottom left
                .position(beirut));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(beirut));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16f));


        locationUpdater.updateMarker(beirut, marker, mMap);


        if (alreadyRegistred()) {

            setCarMarker(Double.parseDouble(settings.getString("LX", "0")), Double.parseDouble(settings.getString("LY", "0")));

        }


    }



    public void SavePos(View v)
    {



        SharedPreferences.Editor editor = settings.edit();

        editor.putString("LX" , locationUpdater.location.getLatitude()+"" ) ;
        editor.putString("LY" , locationUpdater.location.getLongitude()+"") ;

        editor.commit() ;


        Toast.makeText(this , "C'est noté! ",Toast.LENGTH_LONG).show();


        setCarMarker(locationUpdater.location.getLatitude(), locationUpdater.location.getLongitude());



    }

    protected void onStop()
    {

        super.onStop();


    }

    protected void onPause()
    {

        super.onPause();



    }
    protected void onResume()
    {

        super.onResume();



    }





    public boolean alreadyRegistred()
    {

        if(settings.contains("LX")&&settings.contains("LY"))     return true ;
                                                                 return false ;




    }

    public void setCarMarker(double X, double Y)
    {

        if(X*Y==0)
            return ;

        if(carMarker != null)
            carMarker.remove();


            carLL = new LatLng(X , Y);



        carMarker =  mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car" , 50 , 50)))
                .anchor(0.4f, 0.4f) // Anchors the marker on the bottom left
                .position(carLL));



    }
    public void getDirection(View v) {

        if(!alreadyRegistred())
        {

            Toast.makeText(this , "Mais elle est où votre voiture ?" , Toast.LENGTH_LONG).show();
            return ;
        }

            final Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?" + "saddr="+ locationUpdater.location.getLatitude() + "," + locationUpdater.location.getLongitude() + "&daddr=" + settings.getString("LX" , "0") + "," + settings.getString("LY" , "0")));
            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
            startActivity(intent);



    }
    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "mipmap", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }









}
