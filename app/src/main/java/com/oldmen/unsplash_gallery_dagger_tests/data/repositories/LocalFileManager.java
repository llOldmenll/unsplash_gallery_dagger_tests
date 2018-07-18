package com.oldmen.unsplash_gallery_dagger_tests.data.repositories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class LocalFileManager {

    private Context mContext;

    public LocalFileManager(Context context) {
        mContext = context;
    }

    public boolean saveImage(Bitmap finalBitmap, String name) {
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/unsplash_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));
        }

        File file = new File(myDir, name.trim() + ".jpg");
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
