package com.example.xyzhang.testapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.Saver;
import com.example.xyzhang.testapp.util.TextWrapper;

import java.io.IOException;
import java.util.ArrayList;

public class HomeDisplayActivity extends AppCompatActivity {
    FrameLayout mDrawingViewFrameLayout;
    TextView mSaveBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_display);

        mDrawingViewFrameLayout = findViewById(R.id.drawingView);
        mSaveBtn = (Button) findViewById(R.id.saveBtn);
        mProgressBar = findViewById(R.id.progressBar);

        final DrawingView drawingView = new DrawingView(HomeDisplayActivity.this);

        mDrawingViewFrameLayout.addView(drawingView);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saver.upload(HomeDisplayActivity.this);
            }
        });

    }
}
