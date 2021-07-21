package com.cbslprojects.crm.CRM.Util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cbslprojects.crm.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 04/12/18.
 */

public class Utility {

    // public static final String base_url = "http://192.168.2.66:3009/MobileService.asmx/";

  // public static final String base_url = "http://182.73.134.116:5001/MobileService.asmx/";

    //public static final String domain_name ="http://testcrm.cbslprojects.com";

    public static final String domain_name = "https://crm.cbslprojects.com";
    public static final String base_url = domain_name + "/mobileservice.asmx/";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public static String getCurrentTimeStamp(String s) {
//        //   SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yy hh.mm aa");//dd/MM/yyyy
//        SimpleDateFormat src = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
//        SimpleDateFormat dest = new SimpleDateFormat("dd-MMM-yyyy hh.mm aa");
//        Date date = null;
//        try {
//            date = src.parse(s);
//        } catch (ParseException e) {
//            Log.d("Exception", e.getMessage());
//        }
//        return dest.format(date);
//    }

    //    public static String getDatetime() {
//        SimpleDateFormat src = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss aa");
//        Date d = new Date();
//        return src.format(d);
//    }

//    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
//        // bitmap factory
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        // downsizing image as it throws OutOfMemory Exception for larger
//        // images
//        options.inSampleSize = sampleSize;
//
//        return BitmapFactory.decodeFile(filePath, options);
//    }

    public static String getWeekDayName() {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
//        calendar.set(Calendar.MONTH, (Integer.parseInt(month)-1));
//        calendar.set(Calendar.YEAR, Integer.parseInt(year));

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toUpperCase();
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        return formatter.format(date);
    }


    public static ProgressDialog showProgressDialog(Context context) {
        try {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage(context.getResources().getString(R.string.please_wait));
            dialog.show();
            return dialog;
        }catch (Exception e){

            return null;
        }

    }
    public static void ShowDialog(final Context context, String title, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  context.super.onBackPressed();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    public static String getDeviceId(Context context) {

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean checkPermission(Context context) {
        if ((ActivityCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA}, 102);
            return false;
        } else
            return true;

    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
//    toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setGravity(Gravity.TOP, 0, 20);
        toast.show();
    }

    public static void showSettingsAlert(final Context mContext) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static Bitmap setPic(ImageView imageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        if (targetH == 0 && targetW == 0) {
            return null;
        }
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);

        return bitmap;
    }

    public static Bitmap selectGalleryPhoto(Context context, ImageView imageView, Intent data) {

        Uri selectd_img = data.getData();

        String[] projection = {MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA};

        if (selectd_img != null) {
            Cursor cursr = context.getContentResolver().query(selectd_img, projection, null, null, null);

            cursr.moveToFirst();

            //  int titl_index = cursr.getColumnIndex(projection[0]);

            int data_index = cursr.getColumnIndex(projection[1]);


            // String title = cursr.getString(titl_index);
            String img_data = cursr.getString(data_index);

            if (cursr != null)
                cursr.close();

            return setPic(imageView, img_data);
        }
        return null;

    }

    public static String selectGalleryPhoto1(Context context, ImageView imageView, Intent data) {

        Uri selectd_img = data.getData();

        String[] projection = {MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DATA};

        if (selectd_img != null) {
            Cursor cursr = context.getContentResolver().query(selectd_img, projection, null, null, null);

            cursr.moveToFirst();

            //  int titl_index = cursr.getColumnIndex(projection[0]);

            int data_index = cursr.getColumnIndex(projection[1]);


            // String title = cursr.getString(titl_index);
            String img_data = cursr.getString(data_index);

            if (cursr != null)
                cursr.close();

            return img_data;
        }
        return null;

    }


    public static void setHighLightCharacter(String desc, String query, TextView mTextView) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        Pattern word = Pattern.compile(query.toLowerCase());
        Matcher match = word.matcher(desc.toLowerCase());
        SpannableString redSpannable = new SpannableString(desc);
        while (match.find()) {
            redSpannable.setSpan(new BackgroundColorSpan(Color.YELLOW), match.start(), match.end(), 0);
        }
        builder.append(redSpannable);

        mTextView.setText(builder, TextView.BufferType.SPANNABLE);

    }

    public static void showImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context.getApplicationContext()).load(imageUrl)
                // .thumbnail(0.1f)
                .fitCenter()
                .override(500, 500)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                // .error(R.mipmap.img_default)
                .into(imageView);
    }

    public static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                Constaints.GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constaints.GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + Constaints.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

}
