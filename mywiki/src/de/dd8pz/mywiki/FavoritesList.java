package de.dd8pz.mywiki;

public class FavoritesList {
	private FavoritesList Next;
	private FavoritesList Prev;
	String Value;
	
	public FavoritesList(FavoritesList Last,String Val) {
		if (Last==null) {
			Next=null;
			Prev=null;
		} else {
			Next=Last.getNext();
			Prev=Last;
			Last.setNext(this);
			if (Next!=null) {
				Next.setPrev(this);
			}
		}
		Value=Val;
	}
	public void delete() {
		Prev.setNext(Next);
		Next.setPrev(Prev);
		Prev=null;
		Next=null;
	}
	public void setNext(FavoritesList cur) {
		Next=cur;
	}
	public void setPrev(FavoritesList cur) {
		Prev=cur;
	}
	public FavoritesList getNext() {
		return Next;
	}
	public FavoritesList getPrev() {
		return Prev;
	}
	public String getString() {
		return Value;
	}
	public void setString(String val) {
		Value=val;
	}
	public FavoritesList findFirst() {
		FavoritesList start=this;
		FavoritesList cur=start;
		while(cur!=null) {
			start=cur;
			cur=cur.getPrev();
		}
		return start;
	}
	public int length() {
		FavoritesList First=findFirst();
		int num=0;
		while(First!=null) {
			num++;
			First=First.getNext();
		}
		return num;
	}
	public String[] toStringAarry() {
		FavoritesList First=findFirst();
		int i=0;
		String[] Strings=new String[length()];
		while(First!=null) {
			Strings[i++]=First.getString();
			First=First.getNext();
		}
		return Strings;
	}
}
