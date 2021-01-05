package com.example.neorky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview_activity extends AppCompatActivity {
    String url;
    private WebView mywebview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getStringExtra("URL");
        mywebview = findViewById(R.id.webview);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mywebview.loadUrl(url);
        mywebview.setWebViewClient(new WebViewClient());





    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();



    }
}
