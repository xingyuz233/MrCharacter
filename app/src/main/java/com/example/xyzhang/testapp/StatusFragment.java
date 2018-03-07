package com.example.xyzhang.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.io.File;
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
                System.out.println("fontnamelist" + fontNameList);
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
                }, false))
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
                }, false))
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
                    startActivityForResult(intent, 1);
                }
            });
        } else {
            ((ViewGroup) view).removeView(addButton);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result" + requestCode + " " + resultCode);
        if (requestCode == 1)
            if (resultCode == 1) {
                FontList.initServerFontList(getActivity(), null, true);
            } else if (resultCode == 2)
                adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.editing_font_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (status != 0) {
            return false;
        }
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        System.out.println("status ------" + status);
        final String fontName = fontNameList.get(index);
        switch (item.getItemId()) {
            case R.id.menuDelete: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setTitle(R.string.deleteConfirm);

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

                dialog.setTitle(R.string.fontNamePrompt);
                dialog.setView(edit);
                edit.setText(fontName);

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newFontName = edit.getText().toString();
                        if (FontList.existInEdit(newFontName)) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(R.string.fontAlreadyExists);

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

    private View[] inflateItem(View view, final int index, View[] holder) {
        switch (status) {
            case IN_EDIT:
                TextView txtTitle;
                ImageView imgFont;
                if (holder == null) {
                    txtTitle = view.findViewById(R.id.txtTitle);
                    imgFont = view.findViewById(R.id.imgFont);
                } else {
                    txtTitle = (TextView) holder[0];
                    imgFont = (ImageView) holder[1];
                }
                final String fontName = fontNameList.get(index);
                txtTitle.setText(fontName);

                String firstFontPic = getActivity().getFilesDir().getAbsolutePath() + "/" + SessionID.getInstance().getUser() + "/" + fontName + "/" + "0.png";
                File file = new File(firstFontPic);

                System.out.println("inflating " + index + fontName + ": " + file.exists());
                if (file.exists()) {
                    System.out.println("set uri for " + index);
                    imgFont.setImageURI(Uri.fromFile(file));
                } else {
                    imgFont.setImageURI(null);
                }

                if (holder == null)
                    return new View[]{txtTitle, imgFont};
                else
                    return null;
            case IN_PROCESSING:

                TextView txtTitleProgress;
                ProgressBar progressBar;
                TextView txtPercentage;
                if (holder == null) {
                    txtTitleProgress = view.findViewById(R.id.txtTitleProgressing);
                    progressBar = view.findViewById(R.id.prgProgressing);
                    txtPercentage = view.findViewById(R.id.txtPercentage);
                } else {
                    txtTitleProgress = (TextView) holder[0];
                    progressBar = (ProgressBar) holder[1];
                    txtPercentage = (TextView) holder[2];
                }

                Font font = fontList.get(index);
                txtTitleProgress.setText(font.getName());

                progressBar.setProgress((int) (font.getProgress() * 100));

                txtPercentage.setText(MessageFormat.format("{0}{1}",
                        font.getProgress() * 100, getString(R.string.percentage_mark)));

                if (holder == null)
                    return new View[]{txtTitleProgress, progressBar, txtPercentage};
                else
                    return null;
            case FINISHED:
                TextView txtTitleFinished;
                final ProgressBar prgDownload;
                final Button btnDownload;
                if (holder == null) {
                    txtTitleFinished = view.findViewById(R.id.txtTitleFinished);
                    prgDownload = view.findViewById(R.id.prgDownload);
                    btnDownload = view.findViewById(R.id.btnDownload);
                } else {
                    txtTitleFinished = (TextView) holder[0];
                    prgDownload = (ProgressBar) holder[1];
                    btnDownload = (Button) holder[2];
                }
                final Font fontFinished = fontList.get(index);
                txtTitleFinished.setText(fontFinished.getName());
                prgDownload.setVisibility(View.GONE);

                if (fontFinished.isDownloaded()) {
                    btnDownload.setText(R.string.downloaded);
                    btnDownload.setClickable(false);
                    btnDownload.setBackgroundColor(Color.parseColor("#808080"));
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
                                    btnDownload.setClickable(false);
                                    btnDownload.setBackgroundColor(Color.parseColor("#808080"));
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

                if (holder == null)
                    return new View[]{txtTitleFinished, prgDownload, btnDownload};
                else
                    return null;
        }
        return null;
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
            System.out.println("trying to inflate " + i + " " + (view == null));
            if (view == null) {
                System.out.println("...... to inflate " + i + " " + (view == null));
                view = inflater.inflate(getLayoutRes(), null);
                View[] holder = inflateItem(view, i, null);
                view.setTag(holder);
            } else {
                inflateItem(view, i, (View[]) view.getTag());
            }
            return view;
        }
    }
}
