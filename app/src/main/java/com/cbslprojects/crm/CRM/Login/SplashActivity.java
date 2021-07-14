package com.cbslprojects.crm.CRM.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.cbslprojects.crm.CRM.Activitys.MainActivity;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.MyPrefences;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name =  MyPrefences.getInstance(SplashActivity.this).getString(MyPrefences.PREFRENCE_EMAIL_ID,null);

        String refnumber =  MyPrefences.getInstance(SplashActivity.this).getString(MyPrefences.PREFRENCE_REFERENCE_NO,null);

        if (name == null && refnumber == null) {
            setContentView(R.layout.activity_splash_screen);
            Button splashbtn = findViewById(R.id.splash_btn);
            ImageView cbmlogo = findViewById(R.id.cbm_logo);

            Animation animationbottom = AnimationUtils.loadAnimation(this, R.anim.splashtop);
            Animation animationtop = AnimationUtils.loadAnimation(this, R.anim.splashbuttom);
            cbmlogo.setAnimation(animationtop);
            splashbtn.setAnimation(animationbottom);
            splashbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                }
            });
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}
