package issat.baraa.findfriends;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationService extends Service {
    String numero=null;
    public MyLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        numero=intent.getStringExtra("numero");
        //Recuperer la position gps
        FusedLocationProviderClient mClient= LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                   sendSms(location);
                }
            }
        });
        LocationRequest request =LocationRequest.create()
                .setSmallestDisplacement(10)
                .setFastestInterval(10000);
            mClient.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location=locationResult.getLastLocation();
                    if(location!=null){
                        sendSms(location);
                    }
                }
            }, null);


        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSms(Location location) {
        SmsManager manager= SmsManager.getDefault();
        manager.sendTextMessage(numero,null,"FindFriends: ma position est : #"+location.getLongitude()+"#"+location.getLatitude(),null,null);

    }

    //cycle de vie
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}