package com.example.xyzhang.testapp.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by flower on 18-3-1.
 */

public class Saver {

    public static void save(Context context, Bitmap bitmap) {
        try {
            String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            System.out.println(path);
            File file = new File(path + "/tempfonts");
            if (!file.exists()) {
                System.out.println(file.mkdirs());
            }

            File file2 = new File(file.getAbsolutePath() + "/my-font.png");
            System.out.println(file2.getAbsolutePath());
            if (!file2.exists()) {
                file2.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file2);

            try {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        upload(context);
    }

    private static void upload(Context context) {

        new UploadFileAsync(context.getFilesDir().getAbsolutePath() +
                "/tempfonts/my-font.png").execute("");


    }
}
