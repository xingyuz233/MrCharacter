package com.example.xyzhang.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.xyzhang.testapp.util.SessionID;
import com.example.xyzhang.testapp.util.TextWrapper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private EditText inputText;
    private Button getPictureBtn;
    private Spinner mSpinner;
    private List<File> fontList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    private List<File> getFonts() {
        String path = getActivity().getFilesDir().getAbsolutePath() + "/" + SessionID.getInstance().getUser() + "/fonts";
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempfonts";

        File fontDir = new File(path);
        if (!fontDir.exists()) {
            return null;
        }
        System.out.println(fontDir);
        File[] files = fontDir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                System.out.println(pathname);

                if (pathname.isDirectory()) {
                    return false;
                }

                String name = pathname.getName();
                return name.endsWith(".ttf");

            }
        });
        System.out.println(Arrays.toString(files));
        System.out.println(Arrays.asList(files));

        return Arrays.asList(files);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fontList = getFonts();


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        inputText = rootView.findViewById(R.id.input_text);
        getPictureBtn = (Button) rootView.findViewById(R.id.getPictureBtn);
        getPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                final String time = new Date().toString();

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x, height = size.y;

                new TextWrapper(new TextWrapper.UpdateTask() {
                    @Override
                    public void onProgressUpdate(double progress) {
//                        mProgressBar.setProgress((int) (100 * progress));
                    }

                    @Override
                    public void onFinish(int x) {
                        System.out.println("finish");

                        Intent intent = TextPagerActivity.newIntent(getActivity(), x, time);
                        startActivity(intent);

                    }
                }, width, height, fontList.get(mSpinner.getSelectedItemPosition()).getAbsolutePath(), time

                        , inputText.getText().toString())
                        .execute(getActivity());


            }
        });

        mSpinner = rootView.findViewById(R.id.chooseFontSpinner);

        final List<String> fontNameList = new ArrayList<>();
        if (fontList != null)
            for (File font : fontList) {
                fontNameList.add(font.getName());
            }
        final ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, fontNameList);
        mSpinner.setAdapter(spinAdapter);

        FontList.setDownloadObserver(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fontList = getFonts();
                        fontNameList.clear();
                        if (fontList != null)
                            for (File font : fontList) {
                                fontNameList.add(font.getName());
                            }

                        if (fontList == null || !fontList.isEmpty()) {
                            mSpinner.setSelection(0, true);
                            getPictureBtn.setClickable(true);
                            getPictureBtn.setAlpha(1);
                        } else {
                            mSpinner.setEnabled(false);
                            getPictureBtn.setClickable(false);
                            getPictureBtn.setAlpha(.5f);
                        }
                        spinAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        if (fontList == null || !fontList.isEmpty()) {
            mSpinner.setSelection(0, true);
            getPictureBtn.setClickable(true);
            getPictureBtn.setAlpha(1);
        } else {
            mSpinner.setEnabled(false);
            getPictureBtn.setClickable(false);
            getPictureBtn.setAlpha(.5f);
        }

        return rootView;
    }


}
