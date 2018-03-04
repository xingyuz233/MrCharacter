package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.HttpUtil;
import com.example.xyzhang.testapp.util.SessionID;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String originAddress = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private TextView mLogoutBtn;
    private String logoutAddress;



    public UserFragment() {
        // Required empty public constructor
    }





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initView(rootView);
        initEvent(rootView);

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

    private void initView(View rootView) {
        mLogoutBtn = rootView.findViewById(R.id.logoutBtn);
    }

    private void initEvent(View rootView) {
        mLogoutBtn.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutBtn:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("确定要退出吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                        SessionID.getInstance().setId(null);
                        SessionID.getInstance().setUser(null);
                        Intent intent = new Intent(getActivity(),  SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();

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
}
