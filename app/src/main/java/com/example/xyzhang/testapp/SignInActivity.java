package com.example.xyzhang.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private TextView mSignInBtn;
    private TextView mJoinNowBtn;
    private String originAddress = "http://10.0.2.2:8080/login.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){
                jumpToInfo();
            }else {
                //todo
                Toast.makeText(SignInActivity.this, "用户信息不正确！", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void jumpToInfo() {
        Intent intent = new Intent(SignInActivity.this, InfoActivity.class);
        startActivity(intent);
        finish();
    }

    private void jumpToJoinNow() {
        Intent intent = new Intent(SignInActivity.this, JoinNowActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        initEvent();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBtn:
                login();
                break;
            case R.id.joinNowBtn:
                jumpToJoinNow();
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
        mSignInBtn = (TextView) findViewById(R.id.signInBtn);
        mJoinNowBtn = (TextView) findViewById(R.id.joinNowBtn);
    }

    private void initEvent() {
        mSignInBtn.setOnClickListener(this);
        mJoinNowBtn.setOnClickListener(this);
    }

    public void login() {
        //取得用户输入的账号和密码
        if (!isInputValid()){
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        final String user = mPhoneNumberEditText.getText().toString();
        params.put(User.PHONENUMBER, user);
        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
        try {
            HttpUtil.sendPost(originAddress, params,new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    SessionID.getInstance().setUser(user);
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
