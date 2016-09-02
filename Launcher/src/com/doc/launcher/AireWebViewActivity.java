package com.doc.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class AireWebViewActivity extends Activity {

    private WebView wv_user;
//    private LinearLayout progress_text;
    private TextView tv_waite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_aire_web_view);

        wv_user = (WebView) findViewById(R.id.wv_user);
        tv_waite = (TextView) findViewById(R.id.tv_waite);

//        mPref = new MyPreference(this);
//        myIdx = Integer.parseInt(mPref.read("myID", "0"), 16);

        wv_user.loadUrl("http://112.124.97.41:8090/airetalk/");
        setting();



        hideProgress();
    }


    private void setting() {

        WebSettings settings = wv_user.getSettings();//辅助类WebSettings，控制、设置webview
        settings.setJavaScriptEnabled(true); // 启用javascript的脚本功能

        //WebView的缩放操作
        settings.setBuiltInZoomControls(true); // 屏幕上显示放大和缩小按钮
        settings.setUseWideViewPort(true);// 设置双击可以放大或者缩小
    }


    public void hideProgress() {
        // 模拟耗时联网获取数据的操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_waite.setVisibility(View.GONE);
            }
        }, 5000);
    }




}
