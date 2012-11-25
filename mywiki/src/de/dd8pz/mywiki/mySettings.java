package de.dd8pz.mywiki;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.NoSuchFieldException;
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
	private String[] History;
	private int HistoryPos;
	private static final int MaxHistory=20;
	public FavoritesList Favorites;
	
	private void DefaultFavorites() {
		Favorites=new FavoritesList(null,"Hauptseite");
		FavoritesList tmp=new FavoritesList(Favorites,"Spezial:Spezialseiten");
		tmp=new FavoritesList(tmp,"Spezial:Letzte_Änderungen");
	}
	
	mySettings(Context mycontext)
	throws IOException
	{
		context=mycontext;
		History=new String[MaxHistory];
		myDir=new File(mycontext.getFilesDir().getAbsolutePath());
		DefaultSettings();
		LoadPrefs();
	}
	
	public String getUser() {
		return User;
	}
	public String getPasswd() {
		return Passwd;
	}
	public String makeURL(String Search)
	throws NoSuchFieldException {
		if (Search!="") {
			return URL+Search+Option;
		}
		if (isCurHistory()) {
			return getCurHistroy();	
		}
		return URL+Favorites.getString()+Option;
	}
	public void DefaultSettings() {
		URL="http://en.wikipedia.org/wiki/";
		Option="?useskin=chick";
		User="";
		Passwd="";
		DefaultFavorites();
		for (int i=0;i<MaxHistory;i++) {
			History[i]="";
		}
		HistoryPos=-1;
	}
	
	public void LoadPrefs()
	throws IOException
	{
		FavoritesList Fav=Favorites;
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
							Fav=null;
						}
						Fav=new FavoritesList(Fav,Buffer.substring(c+1));
						if (!clearedFav) {
							Favorites=Fav;
							clearedFav=true;
						}
					} else if (Buffer1.equals("history")) {
						if (num<MaxHistory) {
							if ((Buffer.substring(c+1, c+2)==">")||(Buffer.substring(c+1,c+2)==" ")) {
								if (Buffer.substring(c+1,c+2)==">") {
									HistoryPos=num;
								}
								c++;
							}
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
		FavoritesList Fav=Favorites;
		while(Fav!=null) {
			file.write("favorite="+Fav.getString());
			file.newLine();
			Fav=Fav.getNext();
		}
		for (int i=0;i<MaxHistory;i++) {
			if (History[i]!="") {
				if (i==HistoryPos) {
					file.write(">"+History[i]);
				} else if (History[i].startsWith(">")||History[i].startsWith(" ")) {
					file.write(" "+History[i]);
				} else {
					file.write("history="+History[i]);
				}
				file.newLine();
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
				dialog.dismiss();
				if (URL!=URLEdit.getText().toString()) {
					URL=URLEdit.getText().toString();
					clearHistory();
					WikiEdit wikiEdit=(WikiEdit)context;
					wikiEdit.UpdatePage(WikiEdit.What.LOADPAGE);
				}
				User=UserEdit.getText().toString();
				Passwd=PasswdEdit.getText().toString();
				Option=OptionEdit.getText().toString();
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
	public void clearHistory() {
		HistoryPos=-1;
		for (int i=0;i<MaxHistory;i++) {
			History[i]="";
		}
	}
	public int findHistory(String search) {
		for (int i=0;i<MaxHistory;i++) {
			if (History[i]==search) {
				return i;
			}
		}
	return -1;
	}
	public String getCurHistroy()
	throws NoSuchFieldException {
		if (HistoryPos<0) {
			throw new NoSuchFieldException();
		}
		return History[HistoryPos];
	}
	public boolean isCurHistory() {
		return HistoryPos>=0;
	}
	public String getLastHistory()
	throws NoSuchFieldException {
		if (HistoryPos<=0) {
			throw new NoSuchFieldException();
		}
		return History[--HistoryPos];
	}
	public boolean isLastHistory() {
		return HistoryPos>0;
	}
	public String getNextHistory()
	throws NoSuchFieldException {
		if (HistoryPos>=MaxHistory-1) {
			throw new NoSuchFieldException();
		}
		if (History[HistoryPos+1]=="") {
			throw new NoSuchFieldException();
		}
		return History[++HistoryPos];
	}
	public boolean isNextHistory() {
		if (HistoryPos>=MaxHistory-1) {
			return false;
		}
		if (History[HistoryPos+1]=="") {
			return false;
		}
		return true;
	}
	public void setHistory(String history) {
		if (findHistory(history)>=0) {
			return;
		}
		if (HistoryPos>=MaxHistory-1) {
			for(int i=1;i<MaxHistory-1;i++) {
				History[i-1]=History[i];
			}
			HistoryPos--;
		}
		History[++HistoryPos]=history;
	}
	public String getURL() {
		return URL;
	}
	public boolean TestURL(String testedURL) {
		return false;
		/* TUDU: Übergebene URL prüfen, ob von der zubearbeitenden Wiki */
	}
}
