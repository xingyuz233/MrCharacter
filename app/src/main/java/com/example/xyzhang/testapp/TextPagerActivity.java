package com.example.xyzhang.testapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.xyzhang.testapp.util.SessionID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TextPagerActivity extends FragmentActivity {
    private static final String EXTRA_PAGE_COUNT =
            "com.example.xyzhang.intent.page_count";
    public static final String EXTRA_TIME_CREATED =
            "com.example.xyzhang.intent.time_created";

    private ViewPager mViewPager;
    private int pageCount;

    public static Intent newIntent(Context packageContext, int pageCount, String time) {
        Intent intent = new Intent(packageContext, TextPagerActivity.class);
        intent.putExtra(EXTRA_PAGE_COUNT, pageCount);
        intent.putExtra(EXTRA_TIME_CREATED, time);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_pager);

        pageCount = getIntent().getIntExtra(EXTRA_PAGE_COUNT, 0);

        mViewPager = (ViewPager) findViewById(R.id.activity_text_pager_view_pager);

        //final int pageCount = getIntent().getIntExtra(EXTRA_PAGE_COUNT, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return TextFragment.newInstance(position+1);
            }

            @Override
            public int getCount() {
                return pageCount;
            }
        });

        Toast.makeText(this, "图片已经保存至 " + "MyFonts/" + SessionID.getInstance().getUser() + "/"
                + getIntent().getStringExtra(TextPagerActivity.EXTRA_TIME_CREATED) + " 目录下", Toast.LENGTH_SHORT).show();

    }
}
