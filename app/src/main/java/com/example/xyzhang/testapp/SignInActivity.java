package com.example.xyzhang.testapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
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

    private View mInputLayout;
    private View progress;

    private String originAddress = "http://35.196.26.218/login.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){
                jumpToInfo();
            }else {
                //todo
                mPhoneNumberEditText.setVisibility(View.VISIBLE);
                mPassWordEditText.setVisibility(View.VISIBLE);
                recoverInputAnimator(progress, mInputLayout);
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
                if (!isInputValid()) {
                } else {
                    mPhoneNumberEditText.setVisibility(View.INVISIBLE);
                    mPassWordEditText.setVisibility(View.INVISIBLE);
                    inputAnimator(mInputLayout, progress);
                }
                break;
            case R.id.joinNowBtn:
                jumpToJoinNow();
                break;
        }
    }

    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return !(mPhoneNumberEditText.getText().toString().equals("") ||
                mPassWordEditText.getText().toString().equals(""));

    }
    private void initView() {
        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mSignInBtn = (TextView) findViewById(R.id.signInBtn);
        mJoinNowBtn = (TextView) findViewById(R.id.joinNowBtn);

        mInputLayout = findViewById(R.id.input_layout);
        progress = findViewById(R.id.layout_progress);
    }

    private void initEvent() {
        mSignInBtn.setOnClickListener(this);
        mJoinNowBtn.setOnClickListener(this);
    }

    public void login() {
        //取得用户输入的账号和密码
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


    public void inputAnimator(final View view, final View newView) {






        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleX",1f, 0.5f);
        animator2.setDuration(200);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
        animator2.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                newView.setVisibility(View.VISIBLE);
                progressAnimator(newView);
                view.setVisibility(View.GONE);
                login();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    public void recoverInputAnimator(final View newView, final View view) {

        newView.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 0.5f,1f );
        animator2.setDuration(200);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    public void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(3000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }




}
