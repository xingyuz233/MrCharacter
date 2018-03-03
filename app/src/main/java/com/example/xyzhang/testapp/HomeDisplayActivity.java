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
import android.widget.TextView;

import com.example.xyzhang.testapp.util.Saver;
import com.example.xyzhang.testapp.util.TextWrapper;

import java.io.IOException;
import java.util.ArrayList;

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
                try {
                    Display display = HomeDisplayActivity.this.getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x, height = size.y;

                    TextWrapper.draw(width, height, Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfonts/hand.ttf", HomeDisplayActivity.this);

                    System.out.println("----------------------------complete");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
