package com.example.xyzhang.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.Saver;

public class HomeDisplayActivity extends AppCompatActivity {
    FrameLayout mDrawingViewFrameLayout;
    TextView mSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_display);

        mDrawingViewFrameLayout = (FrameLayout) findViewById(R.id.drawingView);
        mSaveBtn = (Button) findViewById(R.id.saveBtn);

        final DrawingView drawingView = new DrawingView(HomeDisplayActivity.this);

        mDrawingViewFrameLayout.addView(drawingView);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saver.save(HomeDisplayActivity.this, drawingView.toBitMap());
            }
        });

    }
}
