package com.example.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayAdminFoodMenuA extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_admin_food_menu_a);

        Intent intent = getIntent();
        String downloadUrl = intent.getStringExtra("downloadurl");

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Use WebView to display PDF file
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + downloadUrl);

        // Set a WebViewClient to handle redirects and open links within the WebView
        webView.setWebViewClient(new WebViewClient());
    }
}
