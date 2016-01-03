package fractal.extroveert;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class GetMyLocation implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Location location;

    private static double myLat, myLon;

    GetMyLocation(Activity activity){

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);    //wifi
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);   //sim card

        List<String> providers = locationManager.getProviders(true);

        location = null;

        for (int i=providers.size()-1; i>=0; i--){
            location = locationManager.getLastKnownLocation(providers.get(i));
            if (location != null) break;
        }

        if (location != null) {


            this.myLat = location.getLatitude();
            this.myLon = location.getLongitude();

        }

    }

    double GetCurrentLat(){
        return this.myLat;
    }

    double GetCurrentLon(){
        return this.myLon;
    }


    @Override
    public void onLocationChanged(Location location) {
        location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

}