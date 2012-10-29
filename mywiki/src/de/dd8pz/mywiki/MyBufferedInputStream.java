package de.dd8pz.mywiki;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MyBufferedInputStream extends InputStream {

	private byte[] IOBuffer;
	private int IOTemp;
	private int IOLen,IOPos;
	private InputStream stream;
	
	public MyBufferedInputStream(InputStream in) {
		IOBuffer=new byte[8192];
		IOLen=0;
		IOPos=0;
		IOTemp=-1;
		stream=in;
	}
	public MyBufferedInputStream(InputStream in,int len) {
		IOBuffer=new byte[len];
		IOLen=0;
		IOPos=0;
		IOTemp=-1;
		stream=in;
	}
	@Override
	public int read() 
	throws EOFException,IOException {
		if (IOTemp>=0) {
			int i=IOTemp;
			IOTemp=-1;
			return i;
		}
		if (IOLen<0) {
			throw new EOFException();
		}
		if ((IOLen==0)||(IOPos==IOLen)) {
			IOLen=stream.read(IOBuffer);
			IOPos=0;
			if (IOLen==0) {
				return -1;
			}
			if (IOLen<0) {
				throw new EOFException();
			}
		}
		return ((int)IOBuffer[IOPos++]);
	}
	public int push(char b) {
		if (IOTemp>=0) {
			return -1;
		}
		IOTemp=(int)b;
		return IOTemp;
	}
	public int read(char[] b) 
	throws EOFException, IOException {
		int b1,i;
		for(i=0;i<b.length;i++) {
			b1=read();
			if (b1<0) {
				break;
			}
			b[i]=(char)b1;
		}
		return i;
	}
	public int read(char []b,int off,int len)
	throws EOFException,IOException {
		if (off>0) {
			if (skip(off)<0) {
				return -1;
			}
		}
		int i,b1;
		int l=len>b.length?len:b.length;
		for(i=0;i<l;i++) {
			b1=read();
			if (b1<0) {
				break;
			}
			b[i]=(char)b1;
		}
		return i;
	}
	public String read(int len)
	throws EOFException,IOException {
		String str="";
		int bl;
		char x;
		for(int i=0;i<len;i++) {
			bl=read();
			if (bl<0) {
				break;
			}
			x=(char)bl;
			str+=x;
		}
		return str;
	}
	public String readLine()
	throws EOFException,IOException {
		String str="";
		char x;
		int bl;
		do {
			bl=read();
			if (bl<0) {
				break;
			}
			if ((bl==10)||(bl==13)) {
				int bl1=read();
				if ((bl!=13)&&(bl1!=10)) {
					push((char)bl1);
				}
			} else {
				x=(char)bl;
				str+=x;
			}
		} while((bl!=10)&&(bl!=13));
	return str;
	}
	public int available() {
		return 1;
	}
	public void close() 
	throws IOException {
		stream.close();
	}
}
