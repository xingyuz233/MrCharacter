package com.example.xyzhang.testapp;

/**
 * Created by XY Zhang on 2018/2/22.
 */
//请求回调接口
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
