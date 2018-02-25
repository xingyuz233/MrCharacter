package com.example.xyzhang.testapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

public class info extends AppCompatActivity implements View.OnClickListener{
    private TextView tabHome;
    private TextView tabUpload;
    private TextView tabUser;

    private FrameLayout ly_content;

    private HomeFragment f1;
    private upload f2;
    private UserFragment f3;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info);

        bindView();

    }

    //UI组件初始化与事件绑定
    private void bindView() {
        tabHome = (TextView)this.findViewById(R.id.txt_home);
        tabUpload = (TextView)this.findViewById(R.id.txt_upload);
        tabUser = (TextView)this.findViewById(R.id.txt_user);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);

        tabHome.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        tabUpload.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabHome.setSelected(true);
        tabUpload.setSelected(false);
        tabUser.setSelected(false);
    }

    //隐藏所有Fragment

    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if(f1==null){
                    f1 = new HomeFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_upload:
                selected();
                tabUpload.setSelected(true);
                if(f2==null){
                    f2 = new upload();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                if(f3==null){
                    f3 = new UserFragment();
                    transaction.add(R.id.fragment_container, f3);
                }else{
                    transaction.show(f3);
                }
                break;
        }

        transaction.commit();
    }
}
