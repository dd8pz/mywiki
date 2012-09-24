package de.dd8pz.mywiki;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WikiEdit extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_wiki_edit, menu);
        return true;
    }
}
