package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyzhang.testapp.util.Saver;
import com.example.xyzhang.testapp.util.SessionID;
import com.example.xyzhang.testapp.util.UploadFileAsync;

import java.io.File;

public class UploadEditActivity extends AppCompatActivity implements View.OnClickListener {

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
    TextView mSubmitBtn;

    boolean editable;
    boolean edited;
    private String originAddress = "http://35.196.26.218/get_pic.php";

    private void initView() {
        Intent intent = getIntent();
        fontName = getIntent().getStringExtra("FONT_NAME");

        mDrawingViewFrameLayout = findViewById(R.id.drawingView);
        mCharacter = findViewById(R.id.character);

        mClearBtn = findViewById(R.id.clearBtn);
        mLeftBtn = findViewById(R.id.leftBtn);
        mRightBtn = findViewById(R.id.rightBtn);
        mBackBtn = findViewById(R.id.backBtn);

        mSeekBar = findViewById(R.id.seekBar);
        mSeekBarValue = findViewById(R.id.seekBarValue);
        mSeekBar.setMax(Character.MAX - 1);

        mSubmitBtn = findViewById(R.id.submitBtn);
        display();
    }

    private void initEvent() {
        mClearBtn.setOnClickListener(this);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (editable && edited) {
                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
                            ((int) Character.CHARACTERS.charAt(id)));
                }
                id = i;
                mSeekBarValue.setText("页数: " + (i + 1));
                display();
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
        //进度条更新
        mSeekBar.setProgress(id);
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

        String src = file.getAbsolutePath() + "/" + ((int) Character.CHARACTERS.charAt(id)) + ".png";
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
//                if (editable && edited) {
//                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
//                            ((int) Character.CHARACTERS.charAt(id)));
//                }
                if (id > 0) {
//                    id--;
                    mSeekBar.setProgress(id - 1);
//                    display();
                }
                break;
            case R.id.rightBtn:
//                if (editable && edited) {
//                    Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
//                            ((int) Character.CHARACTERS.charAt(id)));
//                }
                if (id < Character.MAX - 1) {
//                    id++;
                    mSeekBar.setProgress(id + 1);
//                    display();
                }
                break;
            case R.id.backBtn:

                finish();
                break;
            case R.id.submitBtn:
                upload(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(2);
        if (editable && edited) {
            Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
                    ((int) Character.CHARACTERS.charAt(id)));
        }
    }

    private void upload(Context context) {


//        Context context = getActivity();
        String path = context.getFilesDir().getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println(path);

        String fontPath = path + "/" + user + "/" + fontName;
        File file = new File(fontPath);
        if (!file.exists()) {
            System.out.println(file.mkdirs());
        }
        File[] files = file.listFiles();

        final String[] picArray = new String[files.length];
        for (int i = 0; i < picArray.length; i++) {
            picArray[i] = files[i].getName();
        }


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIcon(R.mipmap.ic_launcher);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(UploadEditActivity.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                });

        mProgressDialog.setMessage("上传中...");


        if (FontList.existInProcess(fontName) || FontList.existInFinished(fontName)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("该字体已上传，确定要覆盖吗？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (editable && edited) {
                        Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
                                ((int) Character.CHARACTERS.charAt(id)));
                    }

                    mProgressDialog.show();


                    new UploadFileAsync(getFilesDir().getAbsolutePath() + "/" +
                            user + "/" + fontName + "/", fontName, true) { //todo
                        @Override
                        protected void onPostExecute(String result) {
                            System.out.println(result);
                            mProgressDialog.setProgress(100);
                            mProgressDialog.dismiss();
                            Toast.makeText(UploadEditActivity.this, "提交成功", Toast.LENGTH_LONG);
                            setResult(1);
                            finish();


                        }

                        @Override
                        protected void onProgressUpdate(Double... values) {
                            System.out.println("progress " + values[0]);
                            mProgressDialog.setProgress((int) (values[0] * 100));

                        }

                    }.execute(picArray);


                }


            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        } else {
        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("确定要退出吗？");
        dialog.setView();
        dialog.setCancelable(false);
        dialog.show();
        */
            // 设置水平进度条
            if (editable && edited) {
                Saver.save(UploadEditActivity.this, mDrawingView.toBitMap(), fontName, "" +
                        ((int) Character.CHARACTERS.charAt(id)));
            }


            mProgressDialog.show();


            new UploadFileAsync(getFilesDir().getAbsolutePath() + "/" +
                    user + "/" + fontName + "/", fontName, false) { //todo
                @Override
                protected void onPostExecute(String result) {
                    System.out.println(result);
                    mProgressDialog.setProgress(100);
                    mProgressDialog.dismiss();
                    Toast.makeText(UploadEditActivity.this, "提交成功", Toast.LENGTH_LONG);
                    setResult(1);
                    finish();


                }

                @Override
                protected void onProgressUpdate(Double... values) {
                    System.out.println("progress " + values[0]);
                    mProgressDialog.setProgress((int) (values[0] * 100));

                }

            }.execute(picArray);
        }

    }


}
