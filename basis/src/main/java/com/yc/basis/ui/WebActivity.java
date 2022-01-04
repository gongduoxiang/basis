package com.yc.basis.ui;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yc.basis.R;
import com.yc.basis.base.BasisBaseActivity;
import com.yc.basis.utils.DataUtils;
import com.yc.basis.utils.WebJsUtils;


public class WebActivity extends BasisBaseActivity {

    private WebView mWebView;
    private ProgressBar bar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        mWebView = findViewById(R.id.web);
        bar = findViewById(R.id.pb_webView);
        setTextBlack(true);
        WebSettings webSettings = mWebView.getSettings();
        //适配手机屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);

        mWebView.addJavascriptInterface(new WebJsUtils(this), "Android");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    //加载完毕进度条消失
                    bar.setVisibility(View.GONE);
                } else {
                    //更新进度
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (SchemeUtils.parseUrl(WebZpActivity.this, Uri.parse(url))) return true;
//                if (url.startsWith("tel:")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        final Intent intent = getIntent();
        mWebView.loadUrl(intent.getStringExtra("url"));
        try {
            String str = intent.getStringExtra("name");
            if (!DataUtils.isEmpty(str)) {
                titleLayout.setTitleText(str);
            } else {
                titleLayout.setTitleText("返回");
            }
        } catch (Exception e) {

        }

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        //后退
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            } catch (Exception e) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void baseClick(View v) {

    }
}
