package com.example.pocketdictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WikiActivity extends AppCompatActivity {

    private WebView webView;

    //@SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);

        //Set the back button <-
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set Title
        setTitle("Wikipedia");

        webView = (WebView)findViewById(R.id.webview_wiki);

        webView.setWebViewClient(new CustomWebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true);
        }


        String wordToSearch = getIntent().getStringExtra("word");
        if(wordToSearch == null){
            Toast.makeText(this, "Error generating word to search!", Toast.LENGTH_LONG).show();
        }
        else {
            loadSearchWordOnWiki(wordToSearch);
        }
    }

    class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("https:") && url.contains("wikipedia")){
                webView.loadUrl(url);
            }
            return true;
        }
    }

    private void loadSearchWordOnWiki(String dicWord){
        String url = "https://en.wikipedia.org/wiki/" + dicWord;
        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                //onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
