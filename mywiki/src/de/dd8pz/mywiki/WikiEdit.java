package de.dd8pz.mywiki;

import java.io.IOException;

import de.dd8pz.mywiki.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class WikiEdit extends Activity {

	private mySettings MySettings;
	@Override
    public void onCreate(Bundle savedInstanceState)  
	{
        super.onCreate(savedInstanceState);
        try {
        	MySettings=new mySettings(this);
        } catch (IOException e) {
        	e.printStackTrace();
        	Toast.makeText(getApplicationContext(), "Fehler beim Laden der Configuragtion!", Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_wiki_edit);
//        LoadPage();
    }

	@Override
	public void finish() {
		try {
			MySettings.savePrefs();
		} catch (IOException e) {
        	e.printStackTrace();
        	Toast.makeText(getApplicationContext(), "Fehler beim Speichern der Configuragtion!", Toast.LENGTH_SHORT).show();
		}
		super.finish();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wiki_edit, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_edit:
    		/* TODO: öffne edit-Activity */
    		return true;
    	case R.id.menu_settings:
    		MySettings.DialogSettings();
    		/* LoadPage() */
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
   
/*    public void LoadPage(mySettings MySettings) {
        WebView webView1 = (WebView) findViewById(R.id.webView1);
        EditText editText1 = (EditText) findViewById(R.id.editText1);
        MySettings.loginSite();
//        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.loadUrl(MySettings.getUrl(editText1.getText()));
    } */
}
