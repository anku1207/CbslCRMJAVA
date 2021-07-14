package com.cbslprojects.crm.CRM.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Interfaces.GetLocation;
import com.cbslprojects.crm.CRM.Location.LocationTrack;

import com.cbslprojects.crm.CRM.Model.CommenResponse;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;

public class Machine_Installation extends AppCompatActivity {

    private TextInputEditText ip_machine_insta;
    private TextInputEditText insta_machine_cmmt;
    private ImageView insta_cap_img;
    private ImageView check_alert_machine;
    private String uid;
    private String machineID;
    private String soleID;
    private String bankID;
    private String latitute;
    private String longitude;
    private String currentDateandTime;
    private Bitmap bitmap;
    private String encodedImage;
    private String saveFilename;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private TextView msg_test_view_machine;
    private String mFilePath;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private LocationTrack locationTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine__installation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button add_btn = findViewById(R.id.machine_insta_add);
        ImageView camera_imageView = findViewById(R.id.image_machine_insta);
        ip_machine_insta = findViewById(R.id.machine_insta_ip);
        insta_cap_img = findViewById(R.id.capture_image_insta);
        TextInputEditText insta_machine_date = findViewById(R.id.machine_insta_date);
        insta_machine_cmmt = findViewById(R.id.machine_insta_cmmt);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotateimg);
        camera_imageView.setAnimation(animation);

        camera_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Machine_Installation.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Machine_Installation.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA};

                    ActivityCompat.requestPermissions(Machine_Installation.this, permissions, 1);
                    return;
                }
                callChooser();
            }
        });

        uid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_ID, null);

        machineID = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_MACHINE_ID, null);

        soleID = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_SOLE_ID, null);

        bankID = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_BANK_ID, null);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        currentDateandTime = sdf.format(new Date());        //        textView.setText(currentDateTimeString);
        insta_machine_date.setText(currentDateandTime);


        checkPermission();


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    Toast.makeText(Machine_Installation.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                } else if (ip_machine_insta.getText().toString().equals("")) {
                    Toast.makeText(Machine_Installation.this, "Please Enter IP", Toast.LENGTH_SHORT).show();


                } else if (insta_machine_cmmt.getText().toString().equals("")) {
                    Toast.makeText(Machine_Installation.this, "Please Enter Comment", Toast.LENGTH_SHORT).show();

                } else {

                    dialog = new Dialog(Machine_Installation.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.machine_installation_custom_dialog);
                    dialog.setCancelable(false);

                    check_alert_machine = dialog.findViewById(R.id.check_image_machine);
                    check_alert_machine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialog.cancel();
                        }
                    });

                    bitmap = ((BitmapDrawable) insta_cap_img.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                    progressDialog.show();

                    GenrateRandamNumber();

                    MachineInstallation();

                }

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void GenrateRandamNumber() {

        long msTime = System.currentTimeMillis();
        Date dt = new Date(msTime);
        String format = "ddmmyyyyhhmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String PhotoFileName = String.format("Photo%s", sdf.format(dt));
        saveFilename = PhotoFileName + ".png";
    }


    private void MachineInstallation() {
        progressDialog.show();

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.MachineInstallationComplete(latitute,
                longitude,
                bankID,
                uid,
                soleID,
                machineID,
                currentDateandTime,
                encodedImage,
                saveFilename,
                insta_machine_cmmt.getText().toString(),
                ip_machine_insta.getText().toString()
        );

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {

                        Toast.makeText(Machine_Installation.this, "Error :=" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        dialog.show();

                        msg_test_view_machine = dialog.findViewById(R.id.text_view_msg_machine);
                        msg_test_view_machine.setText(commenResponse.getErrorMessage());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Machine_Installation.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Machine_Installation.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    getCurrentLocation();

                } else {
                    Utility.showToast(this, "Oops you just denied the permission");
                    finish();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    callChooser();

                }
                break;
            default:
                Utility.showToast(this, "Oops you just denied the permission");
                finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                if (intent == null) {
                    if (mFilePath != null) {
                        bitmap = Utility.setPic(insta_cap_img, mFilePath);
                    }
                } else {
                    bitmap = Utility.selectGalleryPhoto(this, insta_cap_img, intent);

                }
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


    private void callChooser() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utility.getOutputMediaFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                //    Log.e(TAG, "Image file creation failed", ex);
            }
            if (photoFile != null) {
                //mFilePath = "file:" + photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this, this.getPackageName() +
                        ".provider", photoFile);

                mFilePath = photoFile.getAbsolutePath();
                // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            } else {
                takePictureIntent = null;
            }
        }
        //  new Intent(Intent.ACTION_GET_CONTENT);
        Intent contentSelectionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        contentSelectionIntent.setType("*/*");
        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);

    }

    private void checkPermission() {
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 103);
            return;
        }
        getCurrentLocation();
    }


    private void getCurrentLocation() {
        locationTrack = new LocationTrack();
        locationTrack.init(this);
        locationTrack.getCurrentLocation(this, new GetLocation() {
            @Override
            public void getLocation(Location location) {
                if (location != null) {
                    DecimalFormat df2 = new DecimalFormat("00.000000");
                    latitute = String.valueOf(df2.format(location.getLatitude()));
                    longitude = String.valueOf(df2.format(location.getLongitude()));

                } else {
                    Toast.makeText(Machine_Installation.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationTrack.startLocationUpdates(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationTrack != null) {
            locationTrack.stopLocationUpdates();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(locationTrack!=null){
            locationTrack=null;
        }
    }
}

