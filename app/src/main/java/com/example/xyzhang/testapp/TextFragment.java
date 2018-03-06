package com.example.xyzhang.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.SessionID;

import java.io.FileInputStream;

public class TextFragment extends Fragment {
    private static final String ARG_INTEGER_ID = "integer_id";

    private int pageNumber;

    private ImageView mImageView;

    public TextFragment() {
        // Required empty public constructor
    }

    public static TextFragment newInstance(int pageNummber) {
        TextFragment fragment = new TextFragment();
        fragment.pageNumber += pageNummber;

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
        View v = inflater.inflate(R.layout.fragment_text, container, false);

        mImageView = v.findViewById(R.id.text_img);
        FileInputStream fis=null;
        try {
            fis = new FileInputStream(
                    Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/MyFonts/" + SessionID.getInstance().getUser() + "/"
                            + getActivity().getIntent().getStringExtra(TextPagerActivity.EXTRA_TIME_CREATED) + "/"
                            +pageNumber+".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        mImageView.setImageBitmap(bitmap);

        return v;
    }

}
