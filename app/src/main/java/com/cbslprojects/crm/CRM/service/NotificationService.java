package com.cbslprojects.crm.CRM.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Activitys.MainActivity;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Model.ReloveComplaint;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.NotificationScheduler;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.broadcast.AlarmReceiver;

import retrofit2.Call;
import retrofit2.Callback;

public class NotificationService extends IntentService {

    //    public static final String CHANNEL_TWO_ID = "com.cbslprojects.crm.TWO";
//    public static final String CHANNEL_TWO_NAME = "Channel Two";
//    public static final int DAILY_REMINDER_REQUEST_CODE = 0;
//    private NotificationManager notifManager;
    private Context mContext;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();

        String uid = MyPrefences.getInstance(mContext)
                .getString(MyPrefences.PREFRENCE_USER_ID, null);

        if (Utility.isNetworkAvailable(mContext)) {
            ReloveComplaintOnServer(uid);
        }
    }


    private void ReloveComplaintOnServer(String user_id) {

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<ReloveComplaint> listCall = apiInterface.ResolveComplaintList(user_id, "");

        listCall.enqueue(new Callback<ReloveComplaint>() {
            @Override
            public void onResponse(@NonNull Call<ReloveComplaint> call, @NonNull retrofit2.Response<ReloveComplaint> response) {
                try {
                    ReloveComplaint reloveComplaint = response.body();

                    if(reloveComplaint==null){
                        Toast.makeText(mContext, "Error : null", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (reloveComplaint.getErrorMessage().equals("Success") && reloveComplaint.getErrorCode().equals("000") && reloveComplaint.getList() != null) {

                        //  callNotification(reloveComplaint.getList().size());
                        if (!Utility.getWeekDayName().equals("SUNDAY")) {
                            NotificationScheduler.showNotification(mContext,
                                    MainActivity.class,
                                    "CRM",
                                    "Today Resolve Complaint is " + reloveComplaint.getList().size());
                        }else {
                            NotificationScheduler.setReminder(mContext, AlarmReceiver.class, 9,30);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReloveComplaint> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
