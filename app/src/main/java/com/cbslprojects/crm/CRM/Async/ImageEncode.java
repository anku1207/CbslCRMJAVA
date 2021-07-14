package com.cbslprojects.crm.CRM.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.cbslprojects.crm.CRM.Interfaces.ImageEncodeListener;
import com.cbslprojects.crm.R;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class ImageEncode extends AsyncTask<Bitmap, Void, String> {

    private ImageEncodeListener imageEncodeListener;

    public void setImageEncodeListener(ImageEncodeListener imageEncodeListener) {
        this.imageEncodeListener = imageEncodeListener;
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        Bitmap bitmap = bitmaps[0];
        String encodedImage = null;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
        if (b)
            encodedImage = Base64.encodeToString(data, Base64.NO_WRAP);

//        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
//        bitmap.copyPixelsToBuffer(buffer);
//        byte[] data = buffer.array();
//        encodedImage = Base64.encodeToString(data, Base64.DEFAULT);


        return encodedImage;
    }

    @Override
    protected void onPostExecute(String encodedImage) {
        imageEncodeListener.ImageEncode(encodedImage);
    }
}
