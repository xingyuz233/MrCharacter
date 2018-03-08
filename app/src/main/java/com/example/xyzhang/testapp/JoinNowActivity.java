package com.example.xyzhang.testapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzhang.testapp.util.HttpUtil;

import java.util.HashMap;

public class JoinNowActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private EditText mUserNameEditText;
    private TextView mSignInBtn;
    private TextView mJoinNowBtn;

    private View mInputLayout;
    private View progress;

    private String originAddress = "http://10.0.2.2:8080/register.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){
                jumpToSendCode();
            }else if ("Wrong".equals(msg.obj.toString())){
                mUserNameEditText.setVisibility(View.VISIBLE);
                mPassWordEditText.setVisibility(View.VISIBLE);
                mPhoneNumberEditText.setVisibility(View.VISIBLE);
                recoverInputAnimator(progress, mInputLayout);
                Toast.makeText(JoinNowActivity.this, "该用户已存在！", Toast.LENGTH_LONG).show();

            }else {
                //todo
            }
        }
    };

    private void jumpToSignIn() {
        Intent intent = new Intent(JoinNowActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void jumpToSendCode() {
        Intent intent = new Intent(JoinNowActivity.this, InfoActivity.class);
        startActivity(intent);
        finish();
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
                if (!isInputValid()) {
                } else {
                    mUserNameEditText.setVisibility(View.INVISIBLE);
                    mPassWordEditText.setVisibility(View.INVISIBLE);
                    mPhoneNumberEditText.setVisibility(View.INVISIBLE);
                    inputAnimator(mInputLayout, progress);
                }
                break;
        }
    }

    private boolean isInputValid() {
        return !(mPhoneNumberEditText.getText().toString().equals("") ||
                mPassWordEditText.getText().toString().equals("") ||
                mUserNameEditText.getText().toString().equals(""));
    }
    private void initView() {
        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mSignInBtn = (TextView) findViewById(R.id.signInBtn);
        mJoinNowBtn = (TextView) findViewById(R.id.joinNowBtn);
        mInputLayout = findViewById(R.id.input_layout);
        progress = findViewById(R.id.layout_progress);
    }

    private void initEvent() {
        mSignInBtn.setOnClickListener(this);
        mJoinNowBtn.setOnClickListener(this);
    }

    public void register() {
        //取得用户输入的账号和密码
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
        animator3.setDuration(500);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                register();
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
