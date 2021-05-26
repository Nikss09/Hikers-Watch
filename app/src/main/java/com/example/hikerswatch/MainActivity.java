package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView longitude;
    TextView latitude;
    TextView accuracy;
    TextView altitude;
    TextView addresss;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);
        accuracy=findViewById(R.id.accuracy);
        altitude=findViewById(R.id.altitude);
        addresss=findViewById(R.id.address);

        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat=location.getLatitude();
                double lon=location.getLongitude();
                double acc=location.getAccuracy();
                double alt=location.getAltitude();

                Log.i("***Longitude: ", String.valueOf(lon));

                longitude.setText("Longitude: "+lon);
                latitude.setText("Latitude: "+lat);
                accuracy.setText("Accuracy: "+acc);
                altitude.setText("Altitude: "+alt);

                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(listAddresses!=null && listAddresses.size()>0){
                        //Log.i("***Place Info***",listAddresses.get(0).toString());
                        String address="";

                        if(listAddresses.get(0).getFeatureName()!=null){
                            address+=listAddresses.get(0).getFeatureName()+", ";
                        }
                        if(listAddresses.get(0).getThoroughfare()!=null){
                            address+=listAddresses.get(0).getThoroughfare()+"\n";
                        }
                        if(listAddresses.get(0).getSubAdminArea() != null){
                            address+=listAddresses.get(0).getSubAdminArea()+", ";
                        }
                        if(listAddresses.get(0).getAdminArea() != null){
                            address+=listAddresses.get(0).getAdminArea()+" ";
                        }
                        addresss.setText("Address: "+address);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            /*double lat=location.getLatitude();
            double lon=location.getLongitude();
            double acc=location.getAccuracy();
            double alt=location.getAltitude();

            Log.i("***Longitude: ", String.valueOf(lon));

            longitude.setText("Longitude: "+lon);
            latitude.setText("Latitude: "+lat);
            accuracy.setText("Accuracy: "+acc);
            altitude.setText("Altitude: "+alt);*/
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT);
        }
    }
}