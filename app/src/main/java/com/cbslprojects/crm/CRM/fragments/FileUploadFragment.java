package com.cbslprojects.crm.CRM.fragments;


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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.CRM.Adapters.CustomFiletypeSpinner;
import com.cbslprojects.crm.CRM.Async.ImageEncode;
import com.cbslprojects.crm.CRM.Interfaces.ApiInterface;
import com.cbslprojects.crm.CRM.Interfaces.GetLocation;
import com.cbslprojects.crm.CRM.Interfaces.ImageEncodeListener;
import com.cbslprojects.crm.CRM.Location.LocationTrack;
import com.cbslprojects.crm.CRM.Model.CommenResponse;
import com.cbslprojects.crm.CRM.Model.GetFileType;
import com.cbslprojects.crm.CRM.Model.MachineInformation;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.ApiClient;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.MyPrefences;
import com.cbslprojects.crm.CRM.Util.Utility;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileUploadFragment extends Fragment {


    private ProgressDialog progressDialog;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private String mFilePath = null;
    private CustomFiletypeSpinner customFiletypeSpinneradapter;
    private ArrayList<GetFileType> arrayList_getfiletype = new ArrayList<>();
    private String uid;
    private Bitmap bitmap;
    private String file_name;
    private ImageView uploadclick_image;
    private ImageView check_alert_file;
    private Dialog dialog;
    private TextView msg_test_view;
    private String filetypeId;
    private String bankId;
    private String Branchid;
    private String projectID;
    private LocationTrack locationTrack;
    private String latitude="0.0";
    private String longitude="0.0";

    public FileUploadFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_file_upload, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        uid = MyPrefences.getInstance(getActivity()).getString(MyPrefences.PREFRENCE_USER_ID, null);

        uploadclick_image = v.findViewById(R.id.upload_click_image);
        ImageView image_camera = v.findViewById(R.id.camera_image_file_upload);
        Spinner spinner_file_type = v.findViewById(R.id.filetype_spinner);

        customFiletypeSpinneradapter = new CustomFiletypeSpinner(getActivity(), arrayList_getfiletype);
        spinner_file_type.setAdapter(customFiletypeSpinneradapter);
        Button upload_file_btn = v.findViewById(R.id.upload_btn);

        Animation animation_camera_img = AnimationUtils.loadAnimation(getActivity(), R.anim.rotateimg);
        Animation animation_btn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        image_camera.setAnimation(animation_camera_img);
        upload_file_btn.setAnimation(animation_btn);

        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA};

                    ActivityCompat.requestPermissions(getActivity(), permissions, 1);
                    return;
                }
                callChooser();
            }
        });

        //************ spinner for file type
        spinner_file_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                file_name = arrayList_getfiletype.get(i).getFilename();
                filetypeId = arrayList_getfiletype.get(i).getFileTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        upload_file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (bitmap == null) {
                    Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
                } else if (file_name.matches("Choose Your File Type")) {

                    Toast.makeText(getActivity(), "Choose Your File Type", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();

                    bitmap = ((BitmapDrawable) uploadclick_image.getDrawable()).getBitmap();

                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.file_upload_custom_dialog);
                    dialog.setCancelable(false);

                    check_alert_file = dialog.findViewById(R.id.check_image_file);
                    check_alert_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  startActivity(new Intent(getActivity(), FileUploadActivity.class));

                            dialog.cancel();
                            getActivity().finish();
                        }
                    });


                    ImageEncode imageEncode=new ImageEncode();
                    imageEncode.setImageEncodeListener(new ImageEncodeListener() {
                        @Override
                        public void ImageEncode(String image_encode) {

                            if (Utility.isNetworkAvailable(getContext()))
                                if (image_encode != null)
                                    UploadFileOnServer(image_encode);
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Image encoding error", Toast.LENGTH_SHORT).show();
                                }
                            else
                                Toast.makeText(getContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    });
                    imageEncode.execute(bitmap);

                }
            }
        });

        checkPermission();

        if (Utility.isNetworkAvailable(getActivity())) {
            getFileTypeList();

            //****** get a value of machine
            String machineNo = getArguments().getString(Constaints.MACHINE_ID);

            getMachineInformation(machineNo);
        } else {
            Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    private void getFileTypeList() {
        progressDialog.show();

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<GetFileType> listCall = apiInterface.getFileTypeList();

        listCall.enqueue(new Callback<GetFileType>() {
            @Override
            public void onResponse(@NonNull Call<GetFileType> call, @NonNull retrofit2.Response<GetFileType> response) {
                try {
                    GetFileType getFileType = response.body();

                    if (getFileType == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getFileType.getMessage().equals("Success") && getFileType.getErrorCode().equals("000")) {
                        arrayList_getfiletype.clear();
                        arrayList_getfiletype.add(new GetFileType("Choose Your File Type", ""));
                        arrayList_getfiletype.addAll(getFileType.getFilelist());
                        customFiletypeSpinneradapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "File type data not available", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetFileType> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadFileOnServer(String image_encode) {
        progressDialog.show();

        @SuppressLint("HardwareIds")
        String Device_id = Utility.getDeviceId(getActivity());
        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<CommenResponse> listCall = apiInterface.fileUploadOnServer(latitude,
               longitude,
                uid,
                image_encode,
                Device_id,
                filetypeId,
                bankId,
                Branchid,
                projectID);

        listCall.enqueue(new Callback<CommenResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommenResponse> call, @NonNull retrofit2.Response<CommenResponse> response) {
                try {
                    CommenResponse commenResponse = response.body();

                    if (commenResponse == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (commenResponse.getErrorMessage().equals("Success") && commenResponse.getErrorCode().equals("000")) {
                        dialog.show();
                        msg_test_view = dialog.findViewById(R.id.text_view_msg_file);
                        msg_test_view.setText(commenResponse.getErrorMessage());

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "" + commenResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommenResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMachineInformation(String MachineNo) {
        progressDialog.show();

        ApiInterface apiInterface =  ApiClient.getApi(Utility.base_url).create(ApiInterface.class);

        Call<MachineInformation> listCall = apiInterface.MachineInfoDetails(MachineNo);

        listCall.enqueue(new Callback<MachineInformation>() {
            @Override
            public void onResponse(@NonNull Call<MachineInformation> call, @NonNull retrofit2.Response<MachineInformation> response) {
                try {
                    MachineInformation machineInformation = response.body();

                    if (machineInformation == null) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (machineInformation.getErrorMessage().equals("Successs") && machineInformation.getErrorCode().equals("000") && machineInformation.getHarwareInformation() != null) {

                        bankId = String.valueOf(machineInformation.getBankId());
                        Branchid = String.valueOf(machineInformation.getBranchId());
                        projectID = String.valueOf(machineInformation.getProjectId());

                    } else {
                        Toast.makeText(getActivity(), "Data is not available for this solid Id ", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MachineInformation> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                  //  newLocation = new NewLocation(getActivity());
                    getCurrentLocation();
                }
                break;
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    callChooser();
                }
                break;
            default:
                Utility.showToast(getActivity(), "Oops you just denied the permission");

        }
    }

    private void checkPermission() {
        if ((ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ) {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 103);
            return;
        }

        getCurrentLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                if (intent == null) {
                    if (mFilePath != null) {
                        // setPic(uploadclick_image, mFilePath);
                        bitmap = Utility.setPic(uploadclick_image, mFilePath);
                    }
                } else {
                    // selectGalleryPhoto(uploadclick_image, intent);
                    bitmap = Utility.selectGalleryPhoto(getActivity(), uploadclick_image, intent);
                }
            }
        }
    }



    private void callChooser() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utility.getOutputMediaFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                //    Log.e(TAG, "Image file creation failed", ex);
            }
            if (photoFile != null) {
                //mFilePath = "file:" + photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName() +
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

    private void getCurrentLocation() {
        //  progressDialog.show();
        locationTrack = new LocationTrack();
        locationTrack.init(getActivity());
        locationTrack.getCurrentLocation(getActivity(), new GetLocation() {
            @Override
            public void getLocation(Location location) {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                } else {
                    Toast.makeText(getActivity(), "Location Not Found", Toast.LENGTH_SHORT).show();
                }
                //   progressDialog.dismiss();
            }
        });
        locationTrack.startLocationUpdates(getActivity());
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
