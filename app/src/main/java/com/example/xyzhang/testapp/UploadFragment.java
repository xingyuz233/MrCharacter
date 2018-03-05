package com.example.xyzhang.testapp;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
    private ViewPager mStausPager;
    private TextView[] txt_tabs = new TextView[3];
    private View[] line_views = new View[3];

    public UploadFragment() {
        // Required empty public constructor
    }



    //view
    private View rootView;


    private static final String GET_FONT_ORIGIN_ADDRESS = "http://111.230.231.55:8080/testapp/get_font.php";


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
        for (int i = 0; i < 3; i++) {
            txt_tabs[i].setSelected(false);
            line_views[i].setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editingTab:
                mStausPager.setCurrentItem(0);
//                displayEditFont(FontList.editingFontList);
                break;
            case R.id.processingTab:
                mStausPager.setCurrentItem(1);
               // displayServerFont(FontList.proccessingFontList);
                break;
            case R.id.finishedTab:
                mStausPager.setCurrentItem(2);
               // displayServerFont(FontList.finishedFontList);
                break;
        }
    }

    public void initView(){
        line_views[0] = rootView.findViewById(R.id.editingLine);
        line_views[1] = rootView.findViewById(R.id.processingLine);
        line_views[2] = rootView.findViewById(R.id.finishedLine);

        mStausPager = rootView.findViewById(R.id.status_pager);
        txt_tabs[0] = rootView.findViewById(R.id.editingTab);
        txt_tabs[1] = rootView.findViewById(R.id.processingTab);
        txt_tabs[2] = rootView.findViewById(R.id.finishedTab);

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
                selected();
                txt_tabs[status].setSelected(true);
                line_views[status].setSelected(true);
                System.out.println("Status:"+status);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        mStausPager.setCurrentItem(0);
        txt_tabs[0].setSelected(true);
        line_views[0].setSelected(true);

        FontList.initEditingFontList(getActivity());
        FontList.initServerFontList(GET_FONT_ORIGIN_ADDRESS);

    }
    public void initEvent() {
        for (int i = 0; i < 3; i++) {
            txt_tabs[i].setOnClickListener(this);
        }
    }

}
