package com.cbslprojects.crm.CRM.Activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Utility;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("About");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView version_name = findViewById(R.id.version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if (!Utility.base_url.equals("https://crm.cbslprojects.com/mobileservice.asmx/")) {
                version = pInfo.versionName + "\nLocal";
            }
            version_name.setText("Version No. - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
