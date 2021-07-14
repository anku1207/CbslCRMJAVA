package com.cbslprojects.crm.CRM.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.dialog.SaveUserDetailDialogFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv_save_user_detail=findViewById(R.id.tv_save_user_detail);
        tv_save_user_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveUserDetailDialogFragment().show(getSupportFragmentManager(),"SaveUserDetailDialogFragment");
            }
        });
    }
}
