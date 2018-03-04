package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

public class UploadFragment extends Fragment implements View.OnClickListener {


    //view
    private ImageButton mAddButton;
    private EditText edit;
    private LinearLayout mMainLayout;

    private TextView[] txt_tabs = new TextView[3];
    private ViewPager mStausPager;

    private String addFontOriginAddress = "http://111.230.231.55:8080/testapp/add_font.php";
    private String getFontOriginAddress = "http://111.230.231.55:8080/testapp/get_font.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){

            }else if ("Wrong".equals(msg.obj.toString())){
                //todo
            }else {
                //todo
            }
        }
    };


    public UploadFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance() {
        UploadFragment fragment = new UploadFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_upload, container, false);

        initView(rootView);
        initEvent();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                //输入框

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                edit = new EditText(dialog.getContext());

                dialog.setTitle("请输入字体名称");
                dialog.setView(edit);

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("name", edit.getText().toString());
                        try {
                            HttpUtil.sendPost(addFontOriginAddress, params,new HttpCallbackListener() {
                                @Override
                                public void onFinish(String response) {
                                    Message message = new Message();
                                    message.obj = response;

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

                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;

        }
    }

    public void display(View rootView, String response) {

        Gson gson = new Gson();
        List<Font> fontList = gson.fromJson(response, new TypeToken<List<Font>>(){}.getType());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout rowLayout = null;

        final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (final Font font: fontList) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = inflater.inflate(R.layout.font_entry, mMainLayout, false);
                    TextView txtTitle = view.findViewById(R.id.txtTitle);
                    txtTitle.setText(font.getName());

                    // todo display picture in imgFont
                    ImageView imgFont = view.findViewById(R.id.imgFont);
                    //imgFont.setBackgroundColor(Color.BLUE);
                    // imgFont....

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), UploadEditActivity.class);
                            intent.putExtra("FONT_ID", font.getId())
                                    .putExtra("FONT_NAME", font.getName());
                            startActivity(intent);
                        }
                    });

                    mMainLayout.addView(view);
                }
            });
        }


    }


    private void setTabSelectedFalse() {
        txt_tabs[0].setSelected(false);
        txt_tabs[1].setSelected(false);
        txt_tabs[2].setSelected(false);
    }
    public void initView(final View rootView){

        mStausPager = rootView.findViewById(R.id.status_pager);
        txt_tabs[0] = rootView.findViewById(R.id.txt_editing);
        txt_tabs[1] = rootView.findViewById(R.id.txt_making);
        txt_tabs[2] = rootView.findViewById(R.id.txt_finished);

        FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        mStausPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public android.support.v4.app.Fragment getItem(int status) {
                return StatusFragment.newInstance(status);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        mStausPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int status) {
                setTabSelectedFalse();
                txt_tabs[status].setSelected(true);
                System.out.println("Status:"+status);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        txt_tabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setTabSelectedFalse();
//                txt_tabs[0].setSelected(true);
//                System.out.println(0);
                mStausPager.setCurrentItem(StatusFragment.IN_EDIT);
            }
        });
        txt_tabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setTabSelectedFalse();
//                txt_tabs[1].setSelected(true);
//                System.out.println(1);
//                System.out.println(txt_tabs[1].getText());
                mStausPager.setCurrentItem(StatusFragment.IN_PROCESSING);
            }
        });
        txt_tabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setTabSelectedFalse();
//                txt_tabs[2].setSelected(true);
//                System.out.println(2);
                mStausPager.setCurrentItem(StatusFragment.FINISHED);
            }
        });

        //---------------------------下面是张星宇的代码------------------------------------


        mAddButton = (ImageButton) rootView.findViewById(R.id.addButton);
//        mMainLayout = (LinearLayout) rootView.findViewById(R.id.mainLayout);

//        try {
//            HttpUtil.sendGet(getFontOriginAddress, null,new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                   // SessionID.getInstance().setUser(user);
//
//                    display(rootView, response);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Message message = new Message();
//                    message.obj = e.toString();
//                    mHandler.sendMessage(message);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void initEvent() {
        mAddButton.setOnClickListener(this);
    }
    //设置屏幕背景透明度
    private void BackgroudAlpha(float alpha) {
        // TODO Auto-generated method stub
        WindowManager.LayoutParams l = getActivity().getWindow().getAttributes();
        l.alpha = alpha;
        getActivity().getWindow().setAttributes(l);
    }
    //点击其他部分popwindow消失时，屏幕恢复透明度
    class popupwindowdismisslistener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            BackgroudAlpha((float)1);
        }

    }

}
