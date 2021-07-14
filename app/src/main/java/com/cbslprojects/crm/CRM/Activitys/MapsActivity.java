package com.cbslprojects.crm.CRM.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.service.LocationUpdateService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SurfaceHolder.Callback {

    private GoogleMap mMap;
    private TextView locationText;
    private SurfaceHolder camHolder;
    private String uid;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private ImageView map_image_view;
    private ImageView camera_image_view;
    private Camera camera = null;
    private boolean previewRunning;
    private int cameraId = -1;
    private LocationTrack locationTrack;
    private Bitmap bitmap;
    private ImageEncode imageEncode;
    private ProgressDialog progressDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Utility.checkPermission(this)) {
            loaddata();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loaddata() {
        progressDialog = getProgressDialog();
        progressDialog.show();

        linearLayout = findViewById(R.id.snapshot_layout);

        uid = MyPrefences.getInstance(this).getString(MyPrefences.PREFRENCE_USER_ID, null);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button capture = findViewById(R.id.button_capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utility.isNetworkAvailable(MapsActivity.this)) {
                    captureimage();
                } else {
                    Toast.makeText(MapsActivity.this,
                            "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        map_image_view = findViewById(R.id.image_sel_map);
        camera_image_view = findViewById(R.id.image_sel_camera);
        camera_image_view.setRotation(-90);

        initialize();

        locationText = findViewById(R.id.locationText);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Utility.checkPermission(this)) {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        progressDialog.show();
        locationTrack = new LocationTrack();
        locationTrack.init(MapsActivity.this);
        locationTrack.getCurrentLocation(MapsActivity.this, new GetLocation() {
            @Override
            public void getLocation(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    LocationUpdateOnGoogleMap();

//                    Toast.makeText(MapsActivity.this, "lat :" + location.getLatitude() + "\nlon :" + location.getLongitude(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
        locationTrack.startLocationUpdates(MapsActivity.this);
    }

    private void LocationUpdateOnGoogleMap() {
        LatLng latLng = new LatLng(latitude, longitude);
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() != 0) {
                String str = addressList.get(0).getAddressLine(0) + ", " +
                        addressList.get(0).getAddressLine(1) + ", " +
                        addressList.get(0).getAddressLine(2);

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));

                locationText.setText(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void initialize() {
        SurfaceView cameraPreview = findViewById(R.id.camera_preview);
        cameraPreview.setVisibility(View.VISIBLE);
        camHolder = cameraPreview.getHolder();                           //NEEDED FOR THE PREVIEW
        camHolder.addCallback(this);                               //NEEDED FOR THE PREVIEW
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        releaseCamera();
        camera = Camera.open(cameraId);
        camera.startPreview();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //when on Pause, release camera in order to be used from other applications
        // releaseCamera();
        if (locationTrack != null) {
            locationTrack.stopLocationUpdates();
        }
    }


    private void captureimage() {
        try {
            if (camera != null && mnPicture != null) {
                camera.takePicture(null, null, mnPicture);

            } else {
                Toast.makeText(this, "Please try Again...", Toast.LENGTH_SHORT).show();
                return;
            }
            CaptureMapScreen();

            imageEncode = new ImageEncode();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    bitmap = takeScreenshot();
                    imageEncode.execute(bitmap);
                    saveBitmap(bitmap);

                }
            }, 800);


            dialog = new Dialog(MapsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.attendance_custom_dialog);
            dialog.setCancelable(false);

            progressDialog1 = getProgressDialog();
            progressDialog1.show();

            ImageView imageview_checkin = dialog.findViewById(R.id.check_image_attendance);

            imageview_checkin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                    finish();
                    dialog.cancel();
                }
            });


            imageEncode.setImageEncodeListener(new ImageEncodeListener() {
                @Override
                public void ImageEncode(String image_encode) {

                    if (Utility.isNetworkAvailable(MapsActivity.this))
                        if (image_encode != null) {
                            setPunchOnServer(uid, String.valueOf(latitude), String.valueOf(longitude), image_encode);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MapsActivity.this, "Image encoding error", Toast.LENGTH_SHORT).show();
                        }
                    else
                        Toast.makeText(MapsActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProgressDialog getProgressDialog(){
        ProgressDialog     progressDialog1 = new ProgressDialog(MapsActivity.this);
        progressDialog1.setMessage("Please wait...");
        progressDialog1.setCancelable(false);
        return progressDialog1;
    }

    private void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;
                try {
//                    FileOutputStream out = new FileOutputStream("/mnt/sdcard/"
//                            + "MyMapScreen" + System.currentTimeMillis()
//                            + ".png");

                    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                    map_image_view.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mMap.snapshot(callback);

    }

    private Bitmap takeScreenshot() {
        View v = linearLayout;
        v.setDrawingCacheEnabled(true);
        return v.getDrawingCache();
    }

    private void saveBitmap(Bitmap bitmap) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                Constaints.GALLERY_DIRECTORY_NAME);

        File imagePath = new File(mediaStorageDir.getPath() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//            byte[] byteArray = bytes.toByteArray();
            //messaggio = Base64.encodeToString(byteArray, Base64.DEFAULT);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }


    private void releaseCamera() {
        // stop and release camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void setPunchOnServer(String user_id, String lat, String lon, String imagepath) {

        String Device_id = Utility.getDeviceId(this);

        ApiInterface apiInterface = ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.sendPunch(user_id, lat, lon, imagepath, Device_id);

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        Log.d("punch", "commenResponse : null");
                        progressDialog1.dismiss();
                        Toast.makeText(MapsActivity.this, "Error :" + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") && commenResponse.getErrorCode().equals("000")) {
                        Log.i("rerereere", commenResponse.toString());
                        Log.d("punch", "Success");
                        TextView attendance_text_view_msg = dialog.findViewById(R.id.text_view_msg_attendance);
                        attendance_text_view_msg.setText(commenResponse.getErrorMessage());

                        MyPrefences.getInstance(MapsActivity.this).setString(MyPrefences.PREFRENCE_REFERENCE_NO
                                , commenResponse.getReferenceNo());

                        Intent serviceIntent = new Intent(MapsActivity.this, LocationUpdateService.class);
                        ContextCompat.startForegroundService(MapsActivity.this, serviceIntent);

                         dialog.show();

                    } else {
                        Toast.makeText(MapsActivity.this, "Location not updated ", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog1.dismiss();
                } catch (Exception e) {
                    Log.d("punch", "Exception");
                    progressDialog1.dismiss();
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                Log.d("punch", "onFailure");
                onRestart();
                t.printStackTrace();
                progressDialog1.dismiss();
                finish();
                startActivity(new Intent(MapsActivity.this, MapsActivity.class) );
                Toast.makeText(MapsActivity.this, "onFailure :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera == null)
            return;
        if (previewRunning) {
            camera.stopPreview();
        }
        try {
//            Camera.Parameters camParams = camera.getParameters();
//            // Camera.Size size = camParams.getSupportedPreviewSizes().get(0);
//            //  camParams.setPreviewSize(size.width, size.height);
//
//            List<Camera.Size> allSizes = camParams.getSupportedPreviewSizes();
//            Camera.Size size = allSizes.get(0); // get top size
//            for (int i = 0; i < allSizes.size(); i++) {
//                if (allSizes.get(i).width > size.width)
//                    size = allSizes.get(i);
//            }
//            camParams.setPreviewSize(size.width, size.height);
//            camera.setParameters(camParams);

            camera.setPreviewDisplay(holder);
            camera.startPreview();
            previewRunning = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {

        createSurface();
    }

    private static void setCameraDisplayOrientation(Activity activity, Camera camera) {

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();

        android.hardware.Camera.getCameraInfo(1, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //NEEDED FOR THE PREVIEW
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private void createSurface() {
        try {
            Camera.Parameters param;
            if (camera == null)
                return;
            param = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                param.set("orientation", "portrait");
                setCameraDisplayOrientation(this, camera);
            }
            camera.setParameters(param);
            camera.setPreviewDisplay(camHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            //  System.err.println(e);
            // return;
        }
    }

    private Camera.PictureCallback mnPicture = new Camera.PictureCallback() {   //THIS METHOD AND THE METHOD BELOW
        //CONVERT THE CAPTURED IMAGE IN A JPG FILE AND SAVE IT

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bmp1 = BitmapFactory.decodeByteArray(data, 0, data.length);
            // bmp1 = decodeeFile(tmpFile);
            camera_image_view.setImageBitmap(bmp1);


        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    loaddata();
                    createSurface();
                    getCurrentLocation();
                }
//                else {
//                    Utility.showToast(this, "Oops you just denied the permission");
//                    finish();
//                }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        if (locationTrack != null) {
            locationTrack = null;
        }
    }
}



