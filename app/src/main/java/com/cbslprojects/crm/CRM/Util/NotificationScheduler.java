package com.cbslprojects.crm.CRM.Util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.broadcast.AlarmReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Jaison on 20/06/17.
 */

public class NotificationScheduler {

    private static final int DAILY_REMINDER_REQUEST_CODE = 100;
    private static final String CHANNEL_TWO_ID = "com.cbslprojects.crm.ONE";
    private static final String CHANNEL_TWO_NAME = "Channel ONE";

    public static void setReminder(final Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        // cancel already scheduled reminders
        cancelReminder(context, cls);

        if (setcalendar.before(calendar)) {
            setcalendar.add(Calendar.DATE, 1);
          //  Toast.makeText(context, "alarm set", Toast.LENGTH_SHORT).show();
        }
        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Log.d("set Alarm", "time :" + setcalendar.getTimeInMillis());
        System.out.println("set Alarm time :" + setcalendar.getTimeInMillis());
      //  LongConvert(context,setcalendar.getTimeInMillis());

//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            am.set(AlarmManager.RTC_WAKEUP,
                    setcalendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP,
                    setcalendar.getTimeInMillis(),
                    pendingIntent);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), pendingIntent);
        }
    }


//    private static void LongConvert(Context context, long strDate1){
//
//        Calendar setcalendar = Calendar.getInstance();
//        setcalendar.set(Calendar.HOUR_OF_DAY, 9);
//        setcalendar.set(Calendar.MINUTE, 29);
//        setcalendar.set(Calendar.SECOND, 0);
//        long a=setcalendar.getTimeInMillis();
//        long d=strDate1-a;
//
//        Date date=new Date(strDate1);
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss a z");
//            String s = format.format(date);
//            Toast.makeText(context, ""+s+"\n diff:"+format.format(d), Toast.LENGTH_LONG).show();
//            Log.d("set Alarm",""+s+"\n diff :"+format.format(d));
//            System.out.println("set Alarm"+s+"\n diff :"+format.format(d));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public static void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context, Class<?> cls, String title, String content) {
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Intent notificationIntent = new Intent(context, cls);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    notificationIntent.putExtra(Constaints.STATUS, true);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(cls);
    stackBuilder.addNextIntent(notificationIntent);

    PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE,
            PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder builder;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder = new NotificationCompat.Builder(context, CHANNEL_TWO_ID);
    } else {
        builder = new NotificationCompat.Builder(context);
    }
    Notification notification = builder.setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setSmallIcon(R.drawable.cbm_logo)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(new long[]{500, 1000})
            .build();

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_TWO_ID,
                CHANNEL_TWO_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel2.enableLights(false);
        notificationChannel2.enableVibration(true);
        notificationChannel2.setLightColor(Color.RED);
        notificationChannel2.setShowBadge(false);
        notificationManager.createNotificationChannel(notificationChannel2);
    }

    notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);

    NotificationScheduler.setReminder(context, AlarmReceiver.class, 9,30);

}

}
