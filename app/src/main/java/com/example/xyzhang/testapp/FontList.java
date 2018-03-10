package com.example.xyzhang.testapp;

import android.content.Context;
import android.util.Log;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingyu on 2018/3/4.
 */

public class FontList {

    static List<String> editingFontList;
    private static List<Font> processingFontList;
    private static List<Font> finishedFontList;
    private static final String GET_FONT_ORIGIN_ADDRESS = "http://35.196.26.218/get_font.php";

    private static boolean editingLoaded, remoteLoaded;

    private static Map<Integer, Runnable> observers = new HashMap<>();
    private static Runnable downloadObserver;

    //本地字体
    private static List<String> getEditingFontList(Context context) {
        List<String> newFontList = new ArrayList<>();
//        Context context = getActivity();
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + SessionID.getInstance().getUser());
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return (int) (t1.lastModified() - file.lastModified());
            }
        });
        for (File f : files) {
            newFontList.add(f.getName());
        }
        return newFontList;
    }


    private static List<Font> getProcessingFontList(List<Font> fontList) {
        List<Font> newFontList = new ArrayList<>();
        for (Font font : fontList) {
            if (!font.isFinished()) {
                newFontList.add(font);
            }
        }
        return newFontList;
    }

    private static List<Font> getFinishedFontList(List<Font> fontList, Context context) {
        String path = context.getFilesDir().getAbsolutePath() + "/fonts/" + SessionID.getInstance().getUser();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }
        List<Font> newFontList = new ArrayList<>();
        for (Font font : fontList) {
            if (font.isFinished()) {
                newFontList.add(font);
                if (new File(path + "/" + font.getName() + ".ttf").exists())
                    font.setDownloaded(true);
            }
        }
        return newFontList;
    }

    //服务器字体
    public static boolean initServerFontList(final Context context, final Runnable callback, final boolean refresh, final int index) {
        if (!remoteLoaded || refresh) {
            try {
                HttpUtil.sendGet(GET_FONT_ORIGIN_ADDRESS, null, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        // SessionID.getInstance().setUser(user);
                        Gson gson = new Gson();
                        System.out.println("response-----" + response);
                        List<Font> fontList = gson.fromJson(response, new TypeToken<List<Font>>() {
                        }.getType());
//                        System.out.println("fontlist-----" + fontList.get(2).isFinished());

                        processingFontList = FontList.getProcessingFontList(fontList);
                        finishedFontList = FontList.getFinishedFontList(fontList, context);
                        remoteLoaded = true;
                        //display(rootView, response);
                        if (!refresh) {
                            if (callback != null) {
                                observers.put(index, callback);
                                callback.run();
                            }
                        } else
                            for (Runnable runnable: observers.values())
                                runnable.run();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            observers.put(index, callback);
        }
        return false;
    }

    public static void initEditingFontList(Context context) {
        System.out.println("editingLoaded = " + editingLoaded + SessionID.getInstance().getUser());
        if (!editingLoaded) {
            editingLoaded = true;
            editingFontList = getEditingFontList(context);
        }
    }

    public static boolean existInEdit(String fontName) {
        if (editingFontList == null)
            return false;
        for (String font : editingFontList) {
            if (font.equals(fontName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean existInProcess(String fontName) {
        if (processingFontList == null)
            return false;
        for (Font font : processingFontList) {
            if (font.getName().equals(fontName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean existInFinished(String fontName) {
        if (finishedFontList == null)
            return false;
        for (Font font : finishedFontList) {
            if (font.getName().equals(fontName)) {
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
        File file = new File(path + "/" + SessionID.getInstance().getUser() + "/" + fontName);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
            editingFontList.add(0, fontName);
            return true;
        } else {
            return false;
        }
    }


    public static List<Font> getProcessingFontList() {
        return processingFontList;
    }

    public static List<Font> getFinishedFontList() {
        return finishedFontList;
    }

    public static boolean renameFont(Context context, String fontName, String newFontName) {
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + SessionID.getInstance().getUser() + "/" + fontName);
        File newFile = new File(path + "/" + SessionID.getInstance().getUser() + "/" + newFontName);
        if (file.exists()) {
            int index = FontList.editingFontList.indexOf(fontName);
            FontList.editingFontList.set(index, newFontName);
            file.renameTo(newFile);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeFont(Context context, String fontName) {
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + SessionID.getInstance().getUser() + "/" + fontName);
        if (file.exists()) {
            int index = FontList.editingFontList.indexOf(fontName);
            FontList.editingFontList.remove(fontName);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()){
                    File photoFile = new File(files[i].getPath());
                    Log.d("photoPath -->> ", photoFile.getPath());
                    photoFile.delete();
                }
            }
            System.out.print(file.delete());
            return true;
        }
        else {
            return false;
        }
    }

    public static void setDownloadObserver(Runnable downloadObserver) {
        System.out.println("set observer " + downloadObserver);
        FontList.downloadObserver = downloadObserver;
    }

    public static void triggerDownload() {
        System.out.println("downloadObserver = " + downloadObserver);
        if (downloadObserver != null)
            downloadObserver.run();
    }

    public static void resetLoaded() {
        editingLoaded = false;
        remoteLoaded = false;
    }
}

