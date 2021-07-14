package com.cbslprojects.crm.CRM.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Activitys.MapsActivity;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Model.User;
import com.cbslprojects.crm.CRM.Model.UserCredential;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.NotificationScheduler;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.broadcast.AlarmReceiver;
import com.cbslprojects.crm.CRM.service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends AppCompatActivity {


    private TextInputLayout til_email, til_password;
    private TextInputEditText tiet_password;
    private ProgressDialog progressDialog;
    private AppCompatAutoCompleteTextView tiet_email;
    private ArrayList<User> userDatas;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        tiet_email = findViewById(R.id.tiet_email);
        tiet_password = findViewById(R.id.tiet_password);

        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);

        Button loginbutton = findViewById(R.id.login_btn);
        TextView version_name = findViewById(R.id.version);

        userDatas = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();

        sharedPreferences = getSharedPreferences(Constaints.USER_CREDENTIAL_DATA, 0);

        String s = sharedPreferences.getString(Constaints.USER_CREDENTIAL_DATA, null);

        Gson gson = new Gson();

        List<User> list1 = gson.fromJson(s, new TypeToken<List<User>>() {
        }.getType());

        if (list1 != null) {
            userDatas.addAll(list1);
        }

        for (int i = 0; i < userDatas.size(); i++) {
            list.add(userDatas.get(i).getEmail());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        tiet_email.setAdapter(adapter);

        tiet_email.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < userDatas.size(); i++) {
                    if (email.equals(userDatas.get(i).getEmail())) {
                        tiet_password.setText(userDatas.get(i).getPassword());
                    }
                }
            }
        });
        try {
            PackageInfo pInfo = LoginActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            if (!Utility.base_url.equals("https://crm.cbslprojects.com/mobileservice.asmx/")) {
                version = pInfo.versionName + "\nLocal";
            }
            version_name.setText("Version No. - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(LoginActivity.this)) {
                    Login();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void Login() {

        //   String emailId = "2akjha@gmail.com";
        // String passWord = "CBM@123";
//        String emailId = "abhimanyu.cbsl@gmail.com";
//        String passWord = "abhi726";
//        String emailId = "anwarsadat141@gmail.com";
//        String passWord = "CBSL@123";

        String emailId = tiet_email.getText().toString().trim();
        String passWord = tiet_password.getText().toString().trim();

//        tiet_email.setText(emailId);
//        tiet_password.setText(passWord);
//
//        tiet_email.setText(emailId);
//        tiet_password.setText(passWord);

        if (emailId.isEmpty()) {
            til_email.setError("Please Enter Email Id");
            return;
        } else {
            til_email.setError(null);
        }

        if (passWord.isEmpty()) {
            til_password.setError("Please Enter Password");
            return;
        } else {
            til_password.setError(null);
        }

        userLogin(emailId, passWord);

    }

    private void userLogin(final String email, final String password) {

        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<UserCredential> listCall = apiInterface.LoginUser(email, password);

        listCall.enqueue(new Callback<UserCredential>() {
            @Override
            public void onResponse(@NonNull Call<UserCredential> call, @NonNull retrofit2.Response<UserCredential> response) {
                try {
                    UserCredential userCredential = response.body();

                    if (userCredential == null) {
                        Toast.makeText(LoginActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userCredential.getErrorMessage().equals("Success")) {

                        MyPrefences.getInstance(LoginActivity.this).setString(MyPrefences.PREFRENCE_EMAIL_ID, userCredential.getUserName());
                        MyPrefences.getInstance(LoginActivity.this).setString(MyPrefences.PREFRENCE_USER_NAME, userCredential.getFirstName());
                        MyPrefences.getInstance(LoginActivity.this).setString(MyPrefences.PREFRENCE_USER_ID, userCredential.getUserId());
                        MyPrefences.getInstance(LoginActivity.this).setString(MyPrefences.PREFRENCE_IMAGE_PATH, userCredential.getImageUrl());
                        MyPrefences.getInstance(LoginActivity.this).setBoolean(MyPrefences.PREFRENCE_NOTIFICATION, true);

                        alertDialog(email, password);


                    } else {
                        Toast.makeText(LoginActivity.this, "Please check Email Id and Password ", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserCredential> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertDialog(final String email, final String password) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.cbm_logo)
                .setTitle("CBSL GROUP CRM")
                .setMessage("Are you sure you want to save Email & Password?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveUserCredential(email, password);

                        callMapActivity();

                        Intent service = new Intent(LoginActivity.this, NotificationService.class);
                        LoginActivity.this.startService(service);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callMapActivity();
                    }
                })
                .show();
    }

    private void callMapActivity() {
        NotificationScheduler.setReminder(LoginActivity.this, AlarmReceiver.class, 9, 30);

        startActivity(new Intent(LoginActivity.this, MapsActivity.class));

        LoginActivity.this.finish();
    }

    private void saveUserCredential(String email, String password) {

        if (userDatas.size() == 5) {
            userDatas.clear();
        }

        userDatas.add(new User(email, password));

        Set set = new TreeSet(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getEmail().equalsIgnoreCase(o2.getEmail())) {
                    return 0;
                }
                return 1;
            }
        });

        set.addAll(userDatas);
        userDatas.clear();
        userDatas.addAll(set);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(userDatas);
        editor.putString(Constaints.USER_CREDENTIAL_DATA, json);
        editor.apply();
    }
}

