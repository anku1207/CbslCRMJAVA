package com.cbslprojects.crm.CRM.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cbslprojects.crm.CRM.Util.NotificationScheduler;
import com.cbslprojects.crm.CRM.service.NotificationService;


public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            //  if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                Log.d("SystemBootReceiver", "boot complete");
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, 9);
//                calendar.set(Calendar.MINUTE, 30);
//                calendar.set(Calendar.SECOND, 0);
//
//                Intent myIntent = new Intent(context, AlarmReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, myIntent
//                        , PendingIntent.FLAG_UPDATE_CURRENT);
//
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()
//                        , AlarmManager.INTERVAL_DAY, pendingIntent);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, 9, 30);

                return;
            }
        }
        Intent service = new Intent(context, NotificationService.class);
        if (context != null)
            context.startService(service);

//        NotificationScheduler.showNotification(context, MainActivity.class,
//                "CRM", "Today Resolve Complaint is ");
    }


}