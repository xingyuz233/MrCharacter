package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xyzhang.testapp.util.DownloadFileAsync;
import com.example.xyzhang.testapp.util.SessionID;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class StatusFragment extends Fragment {
    public final static int IN_EDIT = 0;
    public final static int IN_PROCESSING = 1;
    public final static int FINISHED = 2;

    private int status;

    private List<String> fontNameList;
    private List<Font> fontList;

    private BaseAdapter adapter;
    private static final String GET_FONT_ORIGIN_ADDRESS = "http://10.0.2.2:8080/get_font.php";

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
                FontList.initEditingFontList(getActivity());
                fontNameList = FontList.editingFontList;
                break;
            case IN_PROCESSING:
                if (FontList.initServerFontList(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fontList = FontList.getProcessingFontList();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }))
                    fontList = new ArrayList<>();
                else
                    fontList = FontList.getProcessingFontList();
                break;
            case FINISHED:
                if (FontList.initServerFontList(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fontList = FontList.getFinishedFontList();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }))
                    fontList = new ArrayList<>();
                else
                    fontList = FontList.getFinishedFontList();
        }
        System.out.println(status + "..." + fontList);
        adapter = new FontListAdapter();
        listView.setAdapter(adapter);

        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        if (status == IN_EDIT) {
            registerForContextMenu(listView);
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String fontName = fontNameList.get(i);
                    Intent intent = new Intent(getActivity(), UploadEditActivity.class);
                    //intent.putExtra("FONT_ID", font.getId())
                    intent.putExtra("FONT_NAME", fontName);
                    startActivity(intent);
                }
            });
        } else {
            ((ViewGroup) view).removeView(addButton);
        }
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.editing_font_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        final String fontName = fontNameList.get(index);
        switch (item.getItemId()) {
            case R.id.menuDelete: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setTitle("确认要删除吗？");

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FontList.removeFont(getActivity(), fontName);
                        adapter.notifyDataSetChanged();
//                      displayEditFont(FontList.editingFontList);

                    }

                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return true;
            }
            case R.id.menuRename: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final EditText edit = new EditText(dialog.getContext());

                dialog.setTitle("请输入字体名称");
                dialog.setView(edit);
                edit.setText(fontName);

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newFontName = edit.getText().toString();
                        if (FontList.existInEdit(newFontName)) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle("该字体已经存在！");

                            dialog.setCancelable(false);
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            dialog.show();

                        } else {

                            FontList.renameFont(getActivity(), fontName, newFontName);
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
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
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
                final Font fontFinished = fontList.get(index);
                txtTitle = view.findViewById(R.id.txtTitleFinished);
                txtTitle.setText(fontFinished.getName());

                final ProgressBar prgDownload = view.findViewById(R.id.prgDownload);
                final Button btnDownload = view.findViewById(R.id.btnDownload);
                prgDownload.setVisibility(View.GONE);

                if (fontFinished.isDownloaded()) {
                    btnDownload.setText(R.string.downloaded);
                    btnDownload.setEnabled(false);
                } else {
                    btnDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String path = getActivity().getFilesDir().getAbsolutePath() + "/" +
                                    SessionID.getInstance().getUser() + "/fonts/" + fontFinished.getName() + ".ttf";
                            new DownloadFileAsync(new DownloadFileAsync.UpdateTask() {
                                @Override
                                public void onProgressUpdate(double progress) {
                                    prgDownload.setProgress((int) (progress * 100));
                                }

                                @Override
                                public void onPostExecute(String message) {
                                    prgDownload.setVisibility(View.GONE);
                                    btnDownload.setVisibility(View.VISIBLE);
                                    btnDownload.setText(R.string.downloaded);
                                    btnDownload.setEnabled(false);
                                    fontFinished.setDownloaded(true);
                                }

                                @Override
                                public void onPreExecute() {
                                    prgDownload.setVisibility(View.VISIBLE);
                                    btnDownload.setVisibility(View.GONE);
                                }
                            }).execute(fontFinished.getName(), path);
                        }
                    });
                }

                break;
        }

    }

    private class FontListAdapter extends BaseAdapter {

        private LayoutInflater inflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            System.out.println(status + "???" + fontList);
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
