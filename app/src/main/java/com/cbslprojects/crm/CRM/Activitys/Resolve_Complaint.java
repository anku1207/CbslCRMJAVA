package com.cbslprojects.crm.CRM.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Async.ImageEncode;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Interfaces.GetLocation;
import com.cbslprojects.crm.CRM.Interfaces.ImageEncodeListener;
import com.cbslprojects.crm.CRM.Location.LocationTrack;
import com.cbslprojects.crm.CRM.Model.CommenResponse;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;


import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Resolve_Complaint extends AppCompatActivity {


    private ImageView resolve_image;
    private ImageView resolve_check;
    private Bitmap bitmap;
    private TextInputEditText cmmt_edittxt;
    private String uid;
    private String machiceid;
    private String saveFilename;
    private String complaintnumber;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private TextView textView_resole_status;
    private String mFilePath;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private LocationTrack locationTrack;
    private String latitude = "0.0";
    private String longitude = "0.0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve__complaint);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        ImageView imageView_camera_resolve = findViewById(R.id.camera_image_resolve);
        Button button_submit = findViewById(R.id.submit_btn);
        resolve_image = findViewById(R.id.capture_image_resolve);
        TextInputEditText editText_comp_no = findViewById(R.id.complaint_no);
        cmmt_edittxt = findViewById(R.id.cmmt_edit_txt);

        Animation animation_camera = AnimationUtils.loadAnimation(this, R.anim.rotateimg);
        Animation animation_btn_submit = AnimationUtils.loadAnimation(this, R.anim.shake);
        imageView_camera_resolve.setAnimation(animation_camera);
        button_submit.setAnimation(animation_btn_submit);

        uid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_ID, null);

        if (getIntent() != null) {
            complaintnumber = getIntent().getStringExtra("complaintNo");
            machiceid = getIntent().getStringExtra("machineID");
        }

        editText_comp_no.setText(complaintnumber);

        checkPermission();

        imageView_camera_resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Resolve_Complaint.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Resolve_Complaint.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA};

                    ActivityCompat.requestPermissions(Resolve_Complaint.this, permissions, 1);
                    return;
                }
                callChooser();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmap == null) {
                    Toast.makeText(Resolve_Complaint.this, "Please Select Image", Toast.LENGTH_SHORT).show();

                } else if (cmmt_edittxt.getText().toString().equals("")) {
                    Toast.makeText(Resolve_Complaint.this, "Please Add Comment", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    bitmap = ((BitmapDrawable) resolve_image.getDrawable()).getBitmap();
                    Log.d("Bitmap", "size :" + bitmap.getByteCount());
//                    bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) resolve_image.getDrawable()).getBitmap()
//                            , 500, 500, true);

                    dialog = new Dialog(Resolve_Complaint.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.resolove_custom_dialog);
                    dialog.setCancelable(false);
                    progressDialog.show();

                    resolve_check = dialog.findViewById(R.id.check_image_resolve);

                    resolve_check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialog.cancel();
                        }
                    });
                    GenrateRandamNumber();

                    final String comment = cmmt_edittxt.getText().toString();


                    //  CloseComplaintOnServer1(mFilePath, comment);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            uploadFile(new File(mFilePath));
//                        }
//                    }).start();

                    ImageEncode imageEncode = new ImageEncode();
                    imageEncode.setImageEncodeListener(new ImageEncodeListener() {
                        @Override
                        public void ImageEncode(String image_encode) {

                            if (Utility.isNetworkAvailable(Resolve_Complaint.this))
                                if (image_encode != null) {
                                    CloseComplaintOnServer(image_encode, comment);
                                    // decode Base64 String to image
                                    try {
                                        File photoFile = Utility.getOutputMediaFile();
                                        FileOutputStream fos = new FileOutputStream(photoFile.getAbsolutePath()); //change path of image according to you
                                        byte byteArray[] = Base64.decode(image_encode, Base64.DEFAULT);

                                        fos.write(byteArray);
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Resolve_Complaint.this, "Image encoding error", Toast.LENGTH_SHORT).show();
                                }
                            else
                                Toast.makeText(Resolve_Complaint.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    });
                    imageEncode.execute(bitmap);

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

    private void CloseComplaintOnServer(String encodedImage, String comment) {
        progressDialog.show();

        String status = "Close";

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Log.d("CloseComplaintOnServer", "lat :" + latitude + "\nlong :" + longitude);

        Call<CommenResponse> listCall = apiInterface.ResolveComplaint(latitude,
                longitude,
                uid,
                encodedImage,
                machiceid,
                complaintnumber,
                status,
                comment,
                saveFilename);


        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        progressDialog.dismiss();
                        Toast.makeText(Resolve_Complaint.this, "Error :=" +
                                commenResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("commenResponse", "Error :=" +
                                commenResponse.getErrorMessage());
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") &&
                            commenResponse.getErrorCode().equals("000")) {
                        dialog.show();
                        textView_resole_status = dialog.findViewById(R.id.text_view_msg_resolve);
                        textView_resole_status.setText(commenResponse.getErrorMessage());
                    } else {
                        Toast.makeText(Resolve_Complaint.this, ""
                                + commenResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("commenResponse", "Error :=" +
                                commenResponse.getErrorMessage());
                    }
                    progressDialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Resolve_Complaint.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Resolve_Complaint.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CloseComplaintOnServer1(String selectedImagePath, String comment) {
        progressDialog.show();

        String status = "Close";

        ApiInterface apiInterface = ApiClient.getApi("ftp://182.73.134.116/").create(ApiInterface.class);

        Log.d("CloseComplaintOnServer", "lat :" + latitude + "\nlong :" + longitude);
        RequestBody lat =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), latitude);
        // add another part within the multipart request
        RequestBody lng =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), longitude);
        // add another part within the multipart request
        RequestBody UID =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), uid);

        RequestBody machiceID = RequestBody.create(MediaType.parse("multipart/form-data"), machiceid);
        RequestBody complaint_number = RequestBody.create(MediaType.parse("multipart/form-data"), complaintnumber);
        RequestBody status1 = RequestBody.create(MediaType.parse("multipart/form-data"), status);
        RequestBody comment1 = RequestBody.create(MediaType.parse("multipart/form-data"), comment);

        MultipartBody.Part imagenPerfil = null;
        if (selectedImagePath != null) {
            try {
                File file = new File(selectedImagePath);
                Log.i("Register", "Nombre del archivo " + file.getName());
                // create RequestBody instance from file

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("image/*"), file);

                // MultipartBody.Part is used to send also the actual file name
                imagenPerfil = MultipartBody.Part.createFormData("Image", file.getName(), requestFile);
                //imagenPerfil=imagenPerfil1;
                Log.i("Register", "Nombre del archivo " + imagenPerfil);
            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        Call<CommenResponse> listCall = apiInterface.ResolveComplaint1(imagenPerfil);


        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        progressDialog.dismiss();
                        Toast.makeText(Resolve_Complaint.this, "Error :=" +
                                commenResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("commenResponse", "Error :=" +
                                commenResponse.getErrorMessage());
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") &&
                            commenResponse.getErrorCode().equals("000")) {
                        dialog.show();
                        textView_resole_status = dialog.findViewById(R.id.text_view_msg_resolve);
                        textView_resole_status.setText(commenResponse.getErrorMessage());
                    } else {
                        Toast.makeText(Resolve_Complaint.this, ""
                                + commenResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("commenResponse", "Error :=" +
                                commenResponse.getErrorMessage());
                    }
                    progressDialog.dismiss();

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(Resolve_Complaint.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Resolve_Complaint.this, "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadFile(File fileName) {
        /*********  work only for Dedicated IP ***********/
        final String FTP_HOST = "182.73.134.116";

        /*********  FTP USERNAME ***********/
        final String FTP_USER = "test";

        /*********  FTP PASSWORD ***********/
        final String FTP_PASS = "cbsl@123";
        FTPClient client = new FTPClient();

        try {

            client.connect(FTP_HOST);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/test folder/");

            client.upload(fileName, new FTPDataTransferListener() {

                @Override
                public void started() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resolve_Complaint.this, "started", Toast.LENGTH_SHORT).show();
                            //  progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void transferred(int i) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resolve_Complaint.this, "transferred", Toast.LENGTH_SHORT).show();
                            //  progressDialog.dismiss();
                        }
                    });

                }

                @Override
                public void completed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resolve_Complaint.this, "completed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }

                @Override
                public void aborted() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resolve_Complaint.this, "aborted", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }

                @Override
                public void failed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Resolve_Complaint.this, "failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

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
                        bitmap = Utility.setPic(resolve_image, mFilePath);
                    }
                } else {
                    bitmap = Utility.selectGalleryPhoto(this, resolve_image, intent);
                    mFilePath = Utility.selectGalleryPhoto1(this, resolve_image, intent);
                }
            }
        }
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
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
                    latitude = String.valueOf(df2.format(location.getLatitude()));
                    longitude = String.valueOf(df2.format(location.getLongitude()));

                } else {
                    Toast.makeText(Resolve_Complaint.this, "Location Not Found", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();

        if (locationTrack != null) {
            locationTrack = null;
        }
    }
}
