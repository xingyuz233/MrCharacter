package com.example.xyzhang.testapp;

import android.content.Context;
import android.os.Message;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingyu on 2018/3/4.
 */

public class FontList {

    static String getFontOriginAddress = "";
    static String user = SessionID.getInstance().getUser();
    static List<String> editingFontList;
    static List<Font> proccessingFontList;
    static List<Font> finishedFontList;
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






    private static List<Font> getProccessingFontList(List<Font> fontList) {
        List<Font> newFontList = new ArrayList<>();
        for (Font font: fontList) {
            if (!font.isFinished()) {
                newFontList.add(font);
            }
        }
        return newFontList;
    }

    private static List<Font> getFinishedFontList(List<Font> fontList) {
        List<Font> newFontList = new ArrayList<>();
        for (Font font: fontList) {
            if (font.isFinished()) {
                newFontList.add(font);
            }
        }
        return newFontList;
    }





    //服务器字体
    public static void initServerFontList(String address) {
        try {
            HttpUtil.sendGet(address, null,new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    // SessionID.getInstance().setUser(user);
                    Gson gson = new Gson();
                    List<Font> fontList = gson.fromJson(response, new TypeToken<List<Font>>(){}.getType());

                    proccessingFontList = FontList.getProccessingFontList(fontList);
                    finishedFontList = FontList.getFinishedFontList(fontList);
                    //display(rootView, response);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initEditingFontList(Context context) {
        editingFontList = getEditingFontList(context);
    }

    public static boolean addFont(Context context, String fontName) {
        List<String> newFontList = new ArrayList<>();
//        Context context = getActivity();
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + user + "/" + fontName);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
            return true;
        }
        else {
            return false;
        }
    }

}

