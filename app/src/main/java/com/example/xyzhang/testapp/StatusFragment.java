package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.List;

public class StatusFragment extends Fragment {
    public final static int IN_EDIT = 0;
    public final static int IN_PROCESSING = 1;
    public final static int FINISHED = 2;

    private int status;

    private List<String> fontNameList;
    private List<Font> fontList;

    private BaseAdapter adapter;

    public StatusFragment() {
        // Required empty public constructor
    }


    public static StatusFragment newInstance(int status) {
        StatusFragment fragment = new StatusFragment();
        fragment.status = status;
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
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        ListView listView = view.findViewById(R.id.listStatus);

        switch (status) {
            case IN_EDIT:
                fontNameList = FontList.editingFontList;
                break;
            case IN_PROCESSING:
                fontList = FontList.getProcessingFontList();
                break;
            case FINISHED:
                fontList = FontList.getFinishedFontList();
        }
        adapter = new FontListAdapter();
        listView.setAdapter(adapter);

        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        if (status == IN_EDIT) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    final EditText edit = new EditText(dialog.getContext());

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
                                adapter.notifyDataSetChanged();
//                            displayEditFont(FontList.editingFontList);
                            }
                        }

                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });
        } else {
            ((ViewGroup) view).removeView(addButton);
        }
        return view;
    }

    private int getCount() {
        if (status == IN_EDIT)
            return fontNameList.size();
        else
            return fontList.size();
    }

    @LayoutRes
    private int getLayoutRes() {
        switch (status) {
            case IN_EDIT:
                return R.layout.font_entry_editing;
            case IN_PROCESSING:
                return R.layout.font_entry_processing;
            case FINISHED:
                return R.layout.font_entry_finished;
            default:
                return 0;
        }
    }

    private void inflateItem(View view, final int index) {
        TextView txtTitle;
        switch (status) {
            case IN_EDIT:
                final String fontName = fontNameList.get(index);
                txtTitle = view.findViewById(R.id.txtTitle);
                txtTitle.setText(fontName);

                // todo display picture in imgFont
                ImageView imgFont = view.findViewById(R.id.imgFont);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UploadEditActivity.class);
                        //intent.putExtra("FONT_ID", font.getId())
                        intent.putExtra("FONT_NAME", fontName);
                        startActivity(intent);
                    }
                });
                break;
            case IN_PROCESSING:
                Font font = fontList.get(index);
                txtTitle = view.findViewById(R.id.txtTitleProgressing);
                txtTitle.setText(font.getName());

                ProgressBar progressBar = view.findViewById(R.id.prgProgressing);
                progressBar.setProgress((int) (font.getProgress() * 100));

                TextView txtPercentage = view.findViewById(R.id.txtPercentage);
                txtPercentage.setText(MessageFormat.format("{0}{1}",
                        font.getProgress() * 100, getString(R.string.percentage_mark)));
                break;
            case FINISHED:
                // todo
                break;
        }

    }

    private class FontListAdapter extends BaseAdapter {

        private LayoutInflater inflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            return StatusFragment.this.getCount();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                view = inflater.inflate(getLayoutRes(), null);
            inflateItem(view, i);
            return view;
        }
    }
}
