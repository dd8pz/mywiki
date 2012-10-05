package de.dd8pz.mywiki;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.EditText;

public class WikiEdit extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_edit);
        LoadPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wiki_edit, menu);
        return true;
    }
    
    public void LoadPage() {
        WebView webView1 = (WebView) findViewById(R.id.webView1);
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        mySettings.loginSite();
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.loadUrl(mySetting.getUrl+editText1.getText);
    }
}
