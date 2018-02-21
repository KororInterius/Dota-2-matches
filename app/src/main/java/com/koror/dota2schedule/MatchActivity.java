package com.koror.dota2schedule;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        WebView webView = (WebView) findViewById(R.id.webVew);
        webView.setWebViewClient(new WebViewClient());
        String data = getIntent().getStringExtra("uri");
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setMediaPlaybackRequiresUserGesture(true);
        if (data != null) {
            webView.loadUrl(data);
        }
    }
}
