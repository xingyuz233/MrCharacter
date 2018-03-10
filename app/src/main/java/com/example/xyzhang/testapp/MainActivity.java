package com.example.xyzhang.testapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.xyzhang.testapp.util.SessionID;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    int[] imagePath = {
            R.drawable.ic_character_chong,
            R.drawable.ic_character_shan,
            R.drawable.ic_character_jun,
            R.drawable.ic_character_ling,
            R.drawable.ic_character_mao,
            R.drawable.ic_character_lin,
            R.drawable.ic_character_xiu,
            R.drawable.ic_character_zhu
    };

    private static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在加载布局文件前判断是否登陆过
        SharedPreferences sprfMain = getSharedPreferences("User", MODE_PRIVATE);
        //.getBoolean("main",false)；当找不到"main"所对应的键值是默认返回false

        if(sprfMain.getBoolean("logged",false)){
            SessionID.getInstance().setUser(sprfMain.getString("user", ""));
            Intent intent=new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Button joinNowBtn = (Button) findViewById(R.id.joinNowBtn);
        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinNowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });



        final ImageView mCharacterImage = findViewById(R.id.characterImage);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mCharacterImage, "alpha", 0f, 1f, 0f);
        animator.setDuration(4000);
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
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
                if (i++ >= imagePath.length - 1) {
                    i = 0;
                }
                mCharacterImage.setImageResource(imagePath[i]);
            }
        });
    }
}
