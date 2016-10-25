package com.atom.rami.ouestmavoiture;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rami on 19/10/2016.
 */
public class Mactivity  extends FragmentActivity implements OnMapReadyCallback{



    GoogleMap map ;
    Updater u ;
    boolean FirstRun ;
    SharedPreferences settings ;

    LatLng carLL;

    Marker carMarker, marker ;

    public String STORAGER =  "LOCALISATION" ;

    public void onCreate(Bundle bundle)
    {
      super.onCreate(bundle);


        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(location!=null)
                    updateLocation(location) ;

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
        } ;

        settings = getSharedPreferences(STORAGER, 0);
        FirstRun = true ;


        u = new Updater(this , listener) ;

        if(!u.isLocationAccessible())
        {
            Toast.makeText(this, "On a besoin de votre GPS pour positionner la voiture. ", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return ;

        }
        if(u.knownLocation!=null)
            updateLocation(u.knownLocation);




  /*
*/



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap ;

        LatLng beirut = new LatLng(u.knownLocation.getLatitude(), u.knownLocation.getLongitude());


        marker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("walker", 50, 50)))
                .anchor(0.4f, 0.4f) // Anchors the marker on the bottom left
                .position(beirut));

        map.moveCamera(CameraUpdateFactory.newLatLng(beirut));
        map.animateCamera(CameraUpdateFactory.zoomTo(16f));

        if (alreadyRegistred()) {

            setCarMarker(Double.parseDouble(settings.getString("LX", "0")), Double.parseDouble(settings.getString("LY", "0")));

        }

    }

    public void onResume()
    {
        super.onResume();



    }

    public void updateLocation(Location location)
    {

        if(u.progressDialog!=null)
            if(u.progressDialog.isShowing())
                u.progressDialog.dismiss();
        u.knownLocation = location ;

        if(FirstRun) {

            setContentView(R.layout.activity_maps);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            FirstRun = false ;

        }

        if(map!=null)
        updateBeirutPosition() ;




    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "mipmap", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void getDirection(View v) {

        if(!alreadyRegistred())
        {

            Toast.makeText(this , "Mais elle est où votre voiture ?" , Toast.LENGTH_LONG).show();
            return ;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr="+ u.knownLocation.getLatitude() + "," + u.knownLocation.getLongitude() + "&daddr=" + settings.getString("LX" , "0") + "," + settings.getString("LY" , "0")));
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(intent);



    }
    public boolean alreadyRegistred()
    {

        if(settings.contains("LX")&&settings.contains("LY"))     return true ;
        return false ;




    }
    public void SavePos(View v)
    {



        SharedPreferences.Editor editor = settings.edit();

        editor.putString("LX" , u.knownLocation.getLatitude()+"" ) ;
        editor.putString("LY" , u.knownLocation.getLongitude()+"") ;

        editor.commit() ;


        Toast.makeText(this , "C'est noté! ",Toast.LENGTH_LONG).show();


        setCarMarker(u.knownLocation.getLatitude(),u.knownLocation.getLongitude());



    }

    public void setCarMarker(double X, double Y)
    {

        if(X*Y==0)
            return ;

        if(carMarker != null)
            carMarker.remove();


        carLL = new LatLng(X , Y);



        carMarker =  map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car" , 50 , 50)))
                .anchor(0.4f, 0.4f) // Anchors the marker on the bottom left
                .position(carLL));



    }

    public void updateBeirutPosition()
    {

        LatLng newBeirut = new LatLng(u.knownLocation.getLatitude(), u.knownLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(newBeirut));

        marker.setPosition(newBeirut);


    }











}
