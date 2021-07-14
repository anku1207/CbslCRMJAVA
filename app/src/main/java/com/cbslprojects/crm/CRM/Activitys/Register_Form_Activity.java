package com.cbslprojects.crm.CRM.Activitys;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.BottomNavigationBehavior;
import com.cbslprojects.crm.CRM.Util.BottomNavigationViewHelper;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.fragments.FileUploadFragment;
import com.cbslprojects.crm.CRM.fragments.RegisterComplaintFormFragment;

public class Register_Form_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String machineNo;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");


        BottomNavigationView navigation = findViewById(R.id.navigation);
        menu = navigation.getMenu();
        navigation.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        boolean status = false;
        Intent i = getIntent();
        if (i != null) {
            machineNo = i.getStringExtra(Constaints.MACHINE_ID);
            status = i.getBooleanExtra(Constaints.WHOS_FRAGMENT_LOAD, false);
        }
//        Bundle bundle = new Bundle();
//        bundle.putString(Constaints.MACHINE_ID, machineNo);
        if (status) {

            registerformClickVisible();

        } else {
            fileUploadClickVisible();

        }
    }


    private void callFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_contain, f);
        fragmentTransaction.commit();
    }

    private void registerformClickVisible() {
        onNavigationItemSelected(menu.getItem(1));
        menu.getItem(1).setChecked(true);
    }

    private void fileUploadClickVisible() {
        onNavigationItemSelected(menu.getItem(2));
        menu.getItem(2).setChecked(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        // startActivity(new Intent(this, RegisterComplaintActivity.class));
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Bundle bundle = new Bundle();
        bundle.putString(Constaints.MACHINE_ID, machineNo);

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                onBackPressed();
                return true;
            case R.id.navigation_file:

                FileUploadFragment fileUploadFragment = new FileUploadFragment();
                fileUploadFragment.setArguments(bundle);
                callFragment(fileUploadFragment);
                return true;
            case R.id.navigation_register:

                RegisterComplaintFormFragment registerComplaintFormFragment = new RegisterComplaintFormFragment();
                registerComplaintFormFragment.setArguments(bundle);
                callFragment(registerComplaintFormFragment);
                return true;
        }
        return false;
    }
}
