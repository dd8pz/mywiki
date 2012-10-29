package de.dd8pz.mywiki;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



public class mySettings {
	
	private String URL;
	private String Option;
	private String User;
	private String Passwd;
	private File myDir;
	private Context context;
	public String[] History;
	public ArrayList <String> Favorites;
	
	private void DefaultFavorites() {
		Favorites.clear();
		Favorites.add("Hauptseite");
		Favorites.add("Spezial:Spezialseiten");
		Favorites.add("Spezial:Letzte_Änderungen");
	}
	
	mySettings(Context mycontext)
	throws IOException
	{
		context=mycontext;
		History=new String[20];
		myDir=new File(mycontext.getFilesDir().getAbsolutePath());
		Favorites = new ArrayList<String>();
		DefaultSettings();
		LoadPrefs();
	}
	public void DefaultSettings() {
		URL="http://www.wikipedia.org/wiki/";
		Option="&skin=xxx";
		User="";
		Passwd="";
		DefaultFavorites();
		for (int i=0;i<20;i++) {
			History[i]="";
		}
	}
	
	public void LoadPrefs()
	throws IOException
	{
		DefaultSettings();
		boolean clearedFav=false;
		String Buffer;
		boolean EOF;
		int num=0;
		try {
			MyBufferedInputStream file=new MyBufferedInputStream(new FileInputStream(myDir+"/mywiki.conf"));
			EOF=false;
			while (!EOF) {
				try {
					Buffer=file.readLine();
					int c=Buffer.indexOf('#');
					if (c>=0) {
						Buffer=Buffer.substring(0, c);
					}
					c=Buffer.indexOf('=');
					if (c<0) {
						continue;
					}
					String Buffer1=Buffer.substring(0, c);
					if (Buffer1.equals("url"))
						URL=Buffer.substring(c+1);
					else if (Buffer1.equals("user"))
						User=Buffer.substring(c+1);
					else if (Buffer1.equals("passwd"))
						Passwd=Buffer.substring(c+1);
					else if (Buffer1.equals("option"))
						Option=Buffer.substring(c+1);
					else if (Buffer1.equals("favorite")) {
						if (!clearedFav) {
							Favorites.clear();
							clearedFav=true;
						}
						Favorites.add(Buffer.substring(c+1));
					} else if (Buffer1.equals("history")) {
						if (num<20) {
							History[num++]=Buffer.substring(c+1);
						}
					}
				} catch (EOFException e) {
					EOF=true;
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
		};
	}

	public void savePrefs()
	throws IOException
	{
		BufferedWriter file=new BufferedWriter(new FileWriter(myDir+"/mywiki.new"));
		file.write("url="+URL);
		file.newLine();
		file.write("user="+User);
		file.newLine();
		file.write("passwd="+Passwd);
		file.newLine();
		file.write("option="+Option);
		file.newLine();
		for (int i=0;i<Favorites.size();i++) {
			file.write("favorite="+Favorites.get(i));
			file.newLine();
		}
		for (int i=0;i<20;i++) {
			if (History[i]!="") {
				file.write("history="+History[i]);
			}
		}
		file.close();
		File oldFile=new File(myDir+"/mywiki.conf");
		File bakFile=new File(myDir+"/mywiki.bak");
		File newFile=new File(myDir+"/mywiki.new");
		bakFile.delete();
		oldFile.renameTo(bakFile);
		newFile.renameTo(oldFile);
	}
	public void DialogSettings() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_settings);
		dialog.setTitle(context.getResources().getString(R.string.SettingsTitle));
		final EditText URLEdit=(EditText)dialog.findViewById(R.id.editURL);
		final EditText UserEdit=(EditText)dialog.findViewById(R.id.editUser);
		final EditText PasswdEdit=(EditText)dialog.findViewById(R.id.editPasswd);
		final EditText OptionEdit=(EditText)dialog.findViewById(R.id.editOption);
		Button OkButton=(Button)dialog.findViewById(R.id.OkButton);
		Button CancelButton=(Button)dialog.findViewById(R.id.CancelButton);
		URLEdit.setText(URL);
		UserEdit.setText(User);
		PasswdEdit.setText(Passwd);
		OptionEdit.setText(Option);
		OkButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				URL=URLEdit.getText().toString();
				User=UserEdit.getText().toString();
				Passwd=PasswdEdit.getText().toString();
				Option=OptionEdit.getText().toString();
				dialog.dismiss();
			}
		});
		CancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	dialog.show();
	}
	public void loginSite() {
		
	}
}
