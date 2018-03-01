package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


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

    //view
    private ImageButton mAddButton;

    private OnFragmentInteractionListener mListener;

    public UploadFragment() {
        // Required empty public constructor
    }

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
        View rootView =  inflater.inflate(R.layout.fragment_upload, container, false);
        initView(rootView);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                //输入框

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                EditText edit = new EditText(dialog.getContext());

                dialog.setTitle("请输入字体名称");
                dialog.setView(edit);

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

    public void initView(View rootView){
        mAddButton = (ImageButton) rootView.findViewById(R.id.addButton);
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
