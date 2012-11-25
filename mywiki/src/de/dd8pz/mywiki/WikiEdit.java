package de.dd8pz.mywiki;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class WikiEdit extends Activity {

	private mySettings MySettings;
	private viewWiki ViewWiki;
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
        Edited();
        ViewWiki=new viewWiki(this,(WebView)findViewById(R.id.webView1),MySettings);
        UpdatePage(What.HISTORY);
        UpdatePage(What.LOADPAGE);
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
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    public void SearchButton(View view) {
    	EditText editText=(EditText)findViewById(R.id.editText1);
    	try {
    		ViewWiki.LoadPage(MySettings.makeURL(editText.getText().toString()));
    	} catch (NoSuchFieldException e) {
    	}
    }
    public void Edited() {
    	EditText editText=(EditText)findViewById(R.id.editText1);
    	editText.addTextChangedListener(new TextWatcher() {
    		public void afterTextChanged(Editable ed) {
    			try {
    				ViewWiki.LoadPage(MySettings.makeURL(ed.toString()));
    			} catch (NoSuchFieldException e) {
    			}
    		}
    		public void onTextChanged(CharSequence s,int start,int before,int count) {
    		}
    		public void beforeTextChanged(CharSequence s,int start,int count,int after) {
    		}
    	});
    }
    public enum What { LOADPAGE,HISTORY }
    public void UpdatePage(What what) {
    	switch (what) {
    	case LOADPAGE:
    		try {
    			if (MySettings.isCurHistory()) {
    				ViewWiki.LoadPage(MySettings.getCurHistroy());
    			} else {
    				ViewWiki.LoadPage(MySettings.getURL()+MySettings.Favorites.getString());
    			}
    		} catch (NoSuchFieldException e) {
    		}
    		break;
    	case HISTORY:
			ImageButton BackButton=(ImageButton)findViewById(R.id.backButton);
			ImageButton ForwardButton=(ImageButton)findViewById(R.id.forwardButton);
			boolean islast=MySettings.isLastHistory();
			boolean isnext=MySettings.isNextHistory();
    		BackButton.setClickable(islast);
    		ForwardButton.setClickable(isnext);
    		break;
    	}
    }
}
