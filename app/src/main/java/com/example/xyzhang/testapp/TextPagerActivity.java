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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TextPagerActivity extends FragmentActivity {
    private static final String EXTRA_PAGE_COUNT =
            "com.example.xyzhang.intent.page_count";

    private ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, int pageCount) {
        Intent intent = new Intent(packageContext, TextPagerActivity.class);
        intent.putExtra(EXTRA_PAGE_COUNT, pageCount);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_pager);

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
                return 3;
            }
        });

    }
}
