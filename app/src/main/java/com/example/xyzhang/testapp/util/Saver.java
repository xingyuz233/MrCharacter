package com.example.xyzhang.testapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by flower on 18-3-1.
 */

public class Saver {

    public static void save(Context context, Bitmap bitmap, String fontName, String picName) {
        try {
            String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            System.out.println(path);
            File file = new File(path + "/" + SessionID.getInstance().getUser() +
                    "/" + fontName);
            if (!file.exists()) {
                System.out.println(file.mkdirs());
            }

            File file2 = new File(file.getAbsolutePath() + "/" + picName + ".png");
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
    }

    public static void savePng(Context context, Bitmap bitmap, String fileName) {

        System.out.println("----------------------saving png " + fileName);
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(path + "/tempfonts/" +fileName);
            if (!file.exists()) {
                System.out.println(file.createNewFile());
            }

            FileOutputStream out = new FileOutputStream(file);

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
    }

    private static void upload(Context context) {

        new UploadFileAsync(context.getFilesDir().getAbsolutePath() +
                "/tempfonts/my-font.png", "Any-Font", "my-font.png").execute("");


    }
}
