package com.cbslprojects.crm.CRM.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;

import com.cbslprojects.crm.CRM.Model.ScheduleMachine;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import retrofit2.Call;
import retrofit2.Callback;

public class Schedule_Machine_List_Description extends AppCompatActivity {

    private TextView textview_schedule_bank;
    private TextView textview_schedule_branch;
    private TextView textview_schedule_add;
    private TextView textview_schedule_city;
    private TextView textview_schedule_state;
    private TextView textview_schedule_machine;
    private TextView textview_schedule_project;
    private TextView textview_schedule_assign;
    private TextView textview_schedule_dispatch;
    private TextView textview_schedule_schedule;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__machine__list__description);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        textview_schedule_bank = findViewById(R.id.schedule_bank_textview);
        textview_schedule_branch = findViewById(R.id.schedule_branch_textview);
        textview_schedule_add = findViewById(R.id.schedule_address_textview);
        textview_schedule_city = findViewById(R.id.schedule_city_textview);
        textview_schedule_state = findViewById(R.id.schedule_state_textview);
        textview_schedule_machine = findViewById(R.id.schedule_machine_textview);
        textview_schedule_project = findViewById(R.id.schedule_project_textview);
        textview_schedule_assign = findViewById(R.id.schedule_assign_textview);
        textview_schedule_dispatch = findViewById(R.id.schedule_dispatch_textview);
        textview_schedule_schedule = findViewById(R.id.schedule_schedule_textview);

        Button button = findViewById(R.id.schedule_btn);

        String uid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_ID, null);
        String solidId = null;
        if (getIntent() != null)
             solidId = getIntent().getStringExtra(Constaints.SOLEID);

        if(Utility.isNetworkAvailable(this)) {
            GetScheduledMachineListforInstallation(uid, solidId);
        }else{
            Toast.makeText(this, "Please ", Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // progressDialog.show();
                startActivity(new Intent(Schedule_Machine_List_Description.this
                        , Machine_Installation.class));
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void GetScheduledMachineListforInstallation(String user_id, String sold_id) {
        progressDialog.show();

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<ScheduleMachine> listCall = apiInterface.ScheduledMachineListforInstallation(user_id, sold_id);

        listCall.enqueue(new Callback<ScheduleMachine>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleMachine> call, @NonNull retrofit2.Response<ScheduleMachine> response) {
                try {
                    ScheduleMachine scheduleMachine = response.body();

                    if (scheduleMachine == null) {
                        progressDialog.dismiss();
                        Toast.makeText(Schedule_Machine_List_Description.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (scheduleMachine.getErrorMessage().equals("Success") && scheduleMachine.getErrorCode().equals("000") && scheduleMachine.getScheduleMachineList() != null) {
                        for (int i = 0; i < scheduleMachine.getScheduleMachineList().size(); i++) {

                            String bankName = scheduleMachine.getScheduleMachineList().get(i).getBankName();
                            String branchName = scheduleMachine.getScheduleMachineList().get(i).getBranchName();
                            String machineId = scheduleMachine.getScheduleMachineList().get(i).getMachineId();
                            String dispatchDate = scheduleMachine.getScheduleMachineList().get(i).getDispatchDate();
                            String projectName = scheduleMachine.getScheduleMachineList().get(i).getProjectName();
                            String assignTo = scheduleMachine.getScheduleMachineList().get(i).getAssignTo();
                            String soleId = scheduleMachine.getScheduleMachineList().get(i).getSoleId();
                            String machineNo = scheduleMachine.getScheduleMachineList().get(i).getMachineNo();
                            String scheduleDate = scheduleMachine.getScheduleMachineList().get(i).getScheduleDate();
                            String cityName = scheduleMachine.getScheduleMachineList().get(i).getCityName();
                            String stateName = scheduleMachine.getScheduleMachineList().get(i).getStateName();
                            String address = scheduleMachine.getScheduleMachineList().get(i).getAddress();
                            String BankId = scheduleMachine.getScheduleMachineList().get(i).getBankID();

                            textview_schedule_bank.setText(bankName);
                            textview_schedule_branch.setText(branchName);
                            textview_schedule_machine.setText(machineNo);
                            textview_schedule_add.setText(address);
                            textview_schedule_city.setText(cityName);
                            textview_schedule_state.setText(stateName);
                            textview_schedule_project.setText(projectName);
                            textview_schedule_assign.setText(assignTo);
                            textview_schedule_dispatch.setText(dispatchDate);
                            textview_schedule_schedule.setText(scheduleDate);

                            MyPrefences.getInstance(Schedule_Machine_List_Description.this).setString(MyPrefences.PREFRENCE_MACHINE_ID, machineId);
                            MyPrefences.getInstance(Schedule_Machine_List_Description.this).setString(MyPrefences.PREFRENCE_SOLE_ID, soleId);
                            MyPrefences.getInstance(Schedule_Machine_List_Description.this).setString(MyPrefences.PREFRENCE_BANK_ID, BankId);
                        }

                    } else {

                        Toast.makeText(Schedule_Machine_List_Description.this, "Data is not available for this solid Id ", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Schedule_Machine_List_Description.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleMachine> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Schedule_Machine_List_Description.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

