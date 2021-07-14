package com.cbslprojects.crm.CRM.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Login.SplashActivity;
import com.cbslprojects.crm.CRM.Model.CommenResponse;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Location.NewLocation;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

import static com.cbslprojects.crm.CRM.Util.LocationChannel.CHANNEL_ID;


public class LocationUpdateService extends Service {

    private static final String TAG = "LocationUpdateService";
    private PingAlarmReceiver mPingAlarmReceiver;
    private PendingIntent mPingAlarmPendIntent;
    private NewLocation newLocation;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CBM Service Running")
                .setSmallIcon(R.drawable.cbm_logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        newLocation = new NewLocation(this);

        startgetLocation();
        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startgetLocation() {
        Log.d(TAG, "startgetLocation");
        String PING_ALARM = "com.cbslprojects.PING_ALARM";
        Intent mPingAlarmIntent = new Intent(PING_ALARM);
        mPingAlarmReceiver = new PingAlarmReceiver();

        mPingAlarmPendIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mPingAlarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Each and every 15 minutes will trigger onReceive of your BroadcastReceiver
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                .setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        0,
                        30*60*1000,
                        mPingAlarmPendIntent);

        // Register the receiver
        registerReceiver(mPingAlarmReceiver, new IntentFilter(PING_ALARM));

    }

    private void cancel() {
        if (mPingAlarmPendIntent != null)
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).cancel(mPingAlarmPendIntent);
        if (mPingAlarmReceiver != null)
            unregisterReceiver(mPingAlarmReceiver);

    }

    private void stopService() {
        Intent serviceIntent = new Intent(this, LocationUpdateService.class);
        stopService(serviceIntent);
    }

    public class PingAlarmReceiver extends BroadcastReceiver {

        public void onReceive(Context ctx, Intent i) {
            // Do your work her
            Log.d("LocationUpdateService", "time :" + getTime());

            if (getTime().equals("06.00 PM")) {
                Log.d("LocationUpdateService", "receive Broadcast cancel");
                stopService();
            } else {


                Log.d("LocationUpdateService", "receive Broadcast \n lat:" + newLocation.getLatitude() + "\nlon:" + newLocation.getLongitude());

                String uid = MyPrefences.getInstance(ctx).getString(MyPrefences.PREFRENCE_USER_ID,null);

                if(Utility.isNetworkAvailable(ctx)) {
                    sendEmployeeLocationOnServer(uid,
                            String.valueOf(newLocation.getLatitude()),
                            String.valueOf(newLocation.getLongitude()));
                }
            }
        }

        private String getTime() {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            Date date = new Date();
            return formatter.format(date);
        }
    }

    private void sendEmployeeLocationOnServer(String user_id, String lat, String lon) {

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.sendEmployeeLocation(user_id, lat, lon);

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        //     Toast.makeText(CheckIn.this, "Error : UserCredential=" + commenResponse, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") && commenResponse.getErrorCode().equals("000")) {
                        Log.i(TAG, "punch" + commenResponse.toString());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //    Toast.makeText(CheckIn.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                //  progressDialog.dismiss();
                //  Toast.makeText(CheckIn.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
