package com.example.xyzhang.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.HttpUtil;

import java.util.HashMap;

public class JoinNowActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private EditText mUserNameEditText;
    private TextView mSignInBtn;
    private TextView mJoinNowBtn;
    private String originAddress = "http://10.0.2.2:8080/register.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){
                jumpToSendCode();
            }else if ("Wrong".equals(msg.obj.toString())){
                //todo
            }else {
                //todo
            }
        }
    };

    private void jumpToSignIn() {
        Intent intent = new Intent(JoinNowActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void jumpToSendCode() {
        Intent intent = new Intent(JoinNowActivity.this, SendCodeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);
        initView();
        initEvent();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBtn:
                jumpToSignIn();
                break;
            case R.id.joinNowBtn:
                register();
                break;
        }
    }

    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return true;
    }
    private void initView() {
        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mSignInBtn = (TextView) findViewById(R.id.signInBtn);
        mJoinNowBtn = (TextView) findViewById(R.id.joinNowBtn);
    }

    private void initEvent() {
        mSignInBtn.setOnClickListener(this);
        mJoinNowBtn.setOnClickListener(this);
    }

    public void register() {
        //取得用户输入的账号和密码
        if (!isInputValid()){
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(User.PHONENUMBER, mPhoneNumberEditText.getText().toString());
        params.put(User.USERNAME, mUserNameEditText.getText().toString());
        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
        try {
            HttpUtil.sendPost(originAddress, params,new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
