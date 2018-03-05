package com.example.xyzhang.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.Saver;
import com.example.xyzhang.testapp.util.SessionID;

import java.io.File;
import java.lang.*;

public class UploadEditActivity extends AppCompatActivity implements View.OnClickListener{

    String fontName;
    String user = SessionID.getInstance().getUser();
    int id;
    FrameLayout mDrawingViewFrameLayout;
    DrawingView mDrawingView;
    TextView mCharacter;
    TextView mClearBtn;
    TextView mLeftBtn;
    TextView mRightBtn;
    TextView mBackBtn;
    SeekBar mSeekBar;
    TextView mSeekBarValue;

    boolean editable;
    boolean edited;
    private String originAddress = "http://10.0.2.2:8080/get_pic.php";

    private void initView() {
        Intent intent = getIntent();
        fontName = getIntent().getStringExtra("FONT_NAME");

        mDrawingViewFrameLayout = (FrameLayout) findViewById(R.id.drawingView);
        mCharacter = (TextView) findViewById(R.id.character);

        mClearBtn = (TextView) findViewById(R.id.clearBtn);
        mLeftBtn = (TextView) findViewById(R.id.leftBtn);
        mRightBtn = (TextView) findViewById(R.id.rightBtn);
        mBackBtn = (TextView) findViewById(R.id.backBtn);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBarValue = (TextView) findViewById(R.id.seekBarValue);
        display();
    }

    private void initEvent() {
        mClearBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                id = i;
                mSeekBarValue.setText("页数: "+(i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                display();
            }
        });
    }

    private void display() {
        //展示标准字体
        mCharacter.setText(String.valueOf(Character.CHARACTERS.charAt(id)));
        //展示编辑
       File file = getPicFile();
        if (file == null) {
            clear();
        } else {
            ImageView imageView = new ImageView(UploadEditActivity.this);
            imageView.setImageURI(Uri.fromFile(file));
            mDrawingViewFrameLayout.removeAllViews();
            mDrawingViewFrameLayout.addView(imageView);
            editable = false;
            edited = false;
        }
    }

    private File getPicFile() {

        //展示编辑
        Context context = UploadEditActivity.this;
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);
        File file = new File(path + "/" + user +
                "/" + fontName);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }

        String src = file.getAbsolutePath() + "/" + id + ".png";
        File file2 = new File(src);
        System.out.println(file2.getAbsolutePath());
        if (!file2.exists()) {
            return null;
        } else {
            return file2;
        }
    }

    private void clear() {
        mDrawingViewFrameLayout.removeAllViews();
        mDrawingView = new DrawingView(UploadEditActivity.this);
        mDrawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                edited = true;
                return false;
            }
        });

        mDrawingViewFrameLayout.addView(mDrawingView);
        editable = true;
        edited = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_edit);
        initView();
        initEvent();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearBtn:
                clear();
                break;
            case R.id.leftBtn:
                if (editable && edited) {
                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" + id);
                }
                if (id > 0) {
                    id--;
                }
                display();
                break;
            case R.id.rightBtn:
                if (editable && edited) {
                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" + id);
                }
                if (id < Character.MAX - 1) {
                    id++;
                }
                display();
                break;
            case R.id.backBtn:
                if (editable && edited) {
                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" + id);
                }
                finish();
                break;

        }

    }

}
