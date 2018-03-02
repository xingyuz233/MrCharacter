package com.example.xyzhang.testapp.util;


import com.example.xyzhang.testapp.HttpCallbackListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XY Zhang on 2018/2/22.
 */
public class HttpUtil {

    static {
        CookieHandler.setDefault(new CookieManager());
    }

    //封装的发送请求函数
    public static void sendGet(final String address, final HashMap<String, String> params, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()) {
            //这里写相应的网络设置处理
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String completedAddress = getURLWithParams(address, params);
                    URL url = new URL(completedAddress);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置方法和参数
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    if (SessionID.getInstance().getId() != null)
                        connection.setRequestProperty("Cookie", SessionID.getInstance().getId());
                    // Set cookie
                    Map<String, List<String>> headers = connection.getHeaderFields();
                    System.out.println(headers);
                    if (headers.get("Set-Cookie") != null) {
                        String cookie = headers.get("Set-Cookie").get(0);
                        SessionID.getInstance().setId(cookie);
                    }
                    //获取返回结果
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //成功则回调onFinish
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendPost(final String address, final HashMap<String, String> params, final HttpCallbackListener listener) {
        if (!HttpUtil.isNetworkAvailable()) {
            //这里写相应的网络设置处理
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String completedAddress = address;
                    URL url = new URL(completedAddress);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置方法和参数
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    //类型
                    connection.setRequestProperty("Content-Type", "application/json");
                    if (SessionID.getInstance().getId() != null)
                        connection.setRequestProperty("Cookie", SessionID.getInstance().getId());
                    //设置Body值
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    String postContent = (new JSONObject(params)).toString();
                    out.write(postContent);
                    out.flush();
                    // Set cookie
                    Map<String, List<String>> headers = connection.getHeaderFields();
                    System.out.println(headers);
                    if (headers.get("Set-Cookie") != null) {
                        String cookie = headers.get("Set-Cookie").get(0);
                        SessionID.getInstance().setId(cookie);
                    }
                    //获取返回结果
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //成功则回调onFinish
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //组装出带参数的完整URL
    private static String getURLWithParams(String address, HashMap<String, String> params) throws UnsupportedEncodingException {
        //设置编码
        final String encode = "UTF-8";
        StringBuilder url = new StringBuilder(address);
        //判断是否带参数
        if (params != null && !params.isEmpty()) {
            url.append("?");
            //将map中的key，value构造进入URL中
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), encode));
                url.append("&");
            }
            //删掉最后一个&
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }

    /*
        private static String getJsonWithParams(HashMap<String,String> params) throws UnsupportedEncodingException {
            final String encode = "UTF-8";
            StringBuilder body = new StringBuilder("");
            JSONObject jsonObject = new JSONObject(params);
            //判断是否带参数
            if (params != null && !params.isEmpty()) {
                //将map中的key，value构造进入URL中


                for (Map.Entry<String, String> entry : params.entrySet()) {
                    body.append(entry.getKey()).append("=");
                    body.append(URLEncoder.encode(entry.getValue(), encode));
                    body.append("&");
                }
                //删掉最后一个&
                body.deleteCharAt(body.length() - 1);

            }
            return jsonObject.toString();
        }
        */
    //判断当前网络是否可用
    public static boolean isNetworkAvailable() {
        return true;
    }
}
