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


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    private OnFragmentInteractionListener mListener;

    public UploadFragment() {
        // Required empty public constructor
    }



    //view
    private View rootView;

    private ImageButton mAddButton;
    private EditText edit;
    private ViewPager mMainLayout;
    private TextView mEditingTab;
    private TextView mProccessingTab;
    private TextView mFinishedTab;
    private View mEditingLine;
    private View mProccessingLine;
    private View mFinishedLine;



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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_upload, container, false);
        initView();
        initEvent();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        /*
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //重置所有文本的选中状态
    public void selected(){
        mEditingTab.setSelected(false);
        mProccessingTab.setSelected(false);
        mFinishedTab.setSelected(false);
        mEditingLine.setSelected(false);
        mProccessingLine.setSelected(false);
        mFinishedLine.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editingTab:
                selected();
                mEditingTab.setSelected(true);
                mEditingLine.setSelected(true);
                displayEditFont(FontList.editingFontList);
                break;
            case R.id.processingTab:
                selected();
                mProccessingTab.setSelected(true);
                mProccessingLine.setSelected(true);
                displayServerFont(FontList.proccessingFontList);
                break;
            case R.id.finishedTab:
                selected();
                mFinishedTab.setSelected(true);
                mFinishedLine.setSelected(true);
                displayServerFont(FontList.finishedFontList);
                break;
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
                        if (!FontList.addFont(getActivity(), edit.getText().toString())) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle("该字体已经存在！");

                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialog.show();

                        } else {
                            FontList.editingFontList.add(edit.getText().toString());
                            displayEditFont(FontList.editingFontList);
                        }
                        /***
                         * 以下代码用于按钮提交
                         */
                        /*
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
                        */
                    }

                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;
                /*
                LayoutInflater inflater = getActivity().getLayoutInflater();
                LayoutInflater inflater2 = getActivity().getLayoutInflater();

                //得到界面视图
                View currean_View = inflater.inflate(R.layout.fragment_upload, null);
                View view = inflater2.inflate(R.layout.fontchoose_layout, null);

                //得到要弹出的界面视图
                WindowManager windowManager = getActivity().getWindowManager();
                int width = windowManager.getDefaultDisplay().getWidth();
                int heigth = windowManager.getDefaultDisplay().getHeight();
                Log.i("width", width+"");
                Log.i("height", heigth+"");
                PopupWindow popupWindow = new PopupWindow(view,(int)(width*0.8),(int)(heigth*0.5));
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //显示在屏幕中央
                popupWindow.showAtLocation(currean_View, Gravity.CENTER, 0, 40);
                //popupWindow弹出后屏幕半透明
                BackgroudAlpha((float)0.5);
                //弹出窗口关闭事件
                popupWindow.setOnDismissListener(new popupwindowdismisslistener());

                //设置弹窗
                //控件
                Spinner spinner = (Spinner) view.findViewById(R.id.)

                //数据
                ArrayList<String> data_list = new ArrayList<String>();
                data_list.add("北京");
                data_list.add("上海");
                data_list.add("广州");
                data_list.add("深圳");

                //适配器
                ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, data_list);
                //设置样式
                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(arr_adapter);
                TextView mSureBtn = (TextView) view.findViewById(R.id.sureBtn);
                mSureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch()
                    }
                });

                break;
                */

        }
    }



    //编辑中字体的展示
    public void displayEditFont(List<String> fontList) {

        mMainLayout.removeAllViews();

        final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (final String font: fontList) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = inflater.inflate(R.layout.font_entry, mMainLayout, false);
                    TextView txtTitle = view.findViewById(R.id.txtTitle);
                    txtTitle.setText(font);

                    // todo display picture in imgFont
                    ImageView imgFont = view.findViewById(R.id.imgFont);
                    //imgFont.setBackgroundColor(Color.BLUE);
                    // imgFont....

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), UploadEditActivity.class);
                            //intent.putExtra("FONT_ID", font.getId())
                            intent.putExtra("FONT_NAME", font);
                            startActivity(intent);
                        }
                    });

                    mMainLayout.addView(view);
                }
            });
        }


    }
    //服务器字体的展示
    public void displayServerFont(List<Font> fontList) {
        mMainLayout.removeAllViews();

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


                    mMainLayout.addView(view);
                }
            });
        }
    }

    public void initView(){
        mAddButton = (ImageButton) rootView.findViewById(R.id.addButton);
        mMainLayout = (ViewPager) rootView.findViewById(R.id.status_pager);
        mEditingTab = (TextView) rootView.findViewById(R.id.editingTab);
        mProccessingTab = (TextView) rootView.findViewById(R.id.processingTab);
        mFinishedTab = (TextView) rootView.findViewById(R.id.finishedTab);
        mEditingLine = (View)rootView.findViewById(R.id.editingLine);
        mProccessingLine = (View) rootView.findViewById(R.id.processingLine);
        mFinishedLine = (View) rootView.findViewById(R.id.finishedLine);

        FontList.initEditingFontList(getActivity());
        FontList.initServerFontList(getFontOriginAddress);

    }
    public void initEvent() {
        mAddButton.setOnClickListener(this);
        mEditingTab.setOnClickListener(this);
        mProccessingTab.setOnClickListener(this);
        mFinishedTab.setOnClickListener(this);
    }

}
