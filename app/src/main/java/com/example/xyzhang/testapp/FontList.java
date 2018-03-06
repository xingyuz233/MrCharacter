package com.example.xyzhang.testapp;

import android.app.Activity;
import android.content.Context;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyu on 2018/3/4.
 */

public class FontList {

    private static String user = SessionID.getInstance().getUser();
    static List<String> editingFontList;
    private static List<Font> proccessingFontList;
    private static List<Font> finishedFontList;

    private static final String GET_FONT_ORIGIN_ADDRESS = "http://10.0.2.2:8080/get_font.php";

    private static boolean editingLoaded, remoteLoaded;

    //本地字体
    private static List<String> getEditingFontList(Context context) {
        List<String> newFontList = new ArrayList<>();
//        Context context = getActivity();
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + user);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }
        File[] files = file.listFiles();
        for (File f: files) {
            newFontList.add(f.getName());
        }
        return newFontList;
    }






    private static List<Font> getProcessingFontList(List<Font> fontList) {
        List<Font> newFontList = new ArrayList<>();
        for (Font font: fontList) {
            if (!font.isFinished()) {
                newFontList.add(font);
            }
        }
        return newFontList;
    }

    private static List<Font> getFinishedFontList(List<Font> fontList, Context context) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + user + "/fonts";
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }
        List<Font> newFontList = new ArrayList<>();
        for (Font font: fontList) {
            if (font.isFinished()) {
                newFontList.add(font);
                if (new File(path + "/" + font.getName() + ".ttf").exists())
                    font.setDownloaded(true);
            }
        }
        return newFontList;
    }





    //服务器字体
    public static boolean initServerFontList(final Context context, final Runnable callback) {
        if (!remoteLoaded) {
            try {
                HttpUtil.sendGet(GET_FONT_ORIGIN_ADDRESS, null, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        // SessionID.getInstance().setUser(user);
                        Gson gson = new Gson();
                        System.out.println("response-----" + response);
                        List<Font> fontList = gson.fromJson(response, new TypeToken<List<Font>>() {
                        }.getType());

                        proccessingFontList = FontList.getProcessingFontList(fontList);
                        finishedFontList = FontList.getFinishedFontList(fontList, context);
                        remoteLoaded = true;
                        //display(rootView, response);
                        callback.run();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static void initEditingFontList(Context context) {
        if (!editingLoaded) {
            editingLoaded = true;
            editingFontList = getEditingFontList(context);
        }
    }

    public static boolean existInEdit(String fontName) {
        for (String font: editingFontList) {
            if (font.equals(fontName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean addFont(Context context, String fontName) {
        //        Context context = getActivity();
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + user + "/" + fontName);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
            editingFontList.add(fontName);
            return true;
        }
        else {
            return false;
        }
    }



    public static List<Font> getProcessingFontList() {
        return proccessingFontList;
    }

    public static List<Font> getFinishedFontList() {
        return finishedFontList;
    }

    public static boolean renameEditingFont(Context context, String fontName, String newFontName) {
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + user + "/" + fontName);
        File newFile = new File(path + "/" + user + "/" + newFontName);
        if (file.exists()) {
            file.renameTo(newFile);
            return true;
        }
        else {
            return false;
        }
    }
}

