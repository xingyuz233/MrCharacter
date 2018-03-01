package com.example.xyzhang.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class UploadEditActivity extends AppCompatActivity {
    FrameLayout mDrawingViewFrameLayout;
    TextView mSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_edit);

        mDrawingViewFrameLayout = (FrameLayout) findViewById(R.id.drawingView);
        mSaveBtn = (TextView) findViewById(R.id.saveBtn);

        mDrawingViewFrameLayout.addView(new DrawingView(UploadEditActivity.this));
    }
}
