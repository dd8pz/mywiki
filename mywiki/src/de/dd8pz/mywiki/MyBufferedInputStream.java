package de.dd8pz.mywiki;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MyBufferedInputStream extends InputStream {

	private byte[] IOBuffer;
	private byte[] IOTemp;
	private int IOLen,IOPos,IOTempPos;
	private InputStream stream;
	
	public MyBufferedInputStream(InputStream in) {
		IOBuffer=new byte[8192];
		IOTemp=new byte[8192];
		IOLen=0;
		IOPos=0;
		IOTempPos=0;
		stream=in;
	}
	public MyBufferedInputStream(InputStream in,int len) {
		IOBuffer=new byte[len];
		IOTemp=new byte[len];
		IOLen=0;
		IOPos=0;
		IOTempPos=0;
		stream=in;
	}
	@Override
	public int read() 
	throws EOFException,IOException {
		if (IOTempPos!=0) {
			return (int) IOTemp[IOTempPos--] & 0xff;
		}
		if (IOLen<0) {
			throw new EOFException();
		}
		if ((IOLen==0)||(IOPos==IOLen)) {
			IOLen=stream.read(IOBuffer);
			IOPos=0;
			if (IOLen<=0) {
				throw new EOFException();
			}
		}
		return (int)IOBuffer[IOPos++] & 0xff;
	}
	public int push(byte b) {
		if (IOTempPos<IOTemp.length) {
			return -1;
		}
		IOTemp[IOTempPos++]=b;
		return (int)b & 0xff;
	}
	public int read(char[] b) 
	throws EOFException, IOException {
		int b1,i;
		for(i=0;i<b.length;i++) {
			try {
				b1=read();
				b[i]=(char)b1;
			} catch (EOFException e) {
				if (i==0) {
					throw e;
				}
				return i;
			}
		}
		return i;
	}
	public int read(char[] b,int off,int len)
	throws EOFException,IOException {
		if (off>0) {
			if (skip(off)<0) {
				return -1;
			}
		}
		int i,b1;
		int l=len>b.length?len:b.length;
		for(i=0;i<l;i++) {
			try {
				b1=read();
				b[i]=(char)b1;
			} catch (EOFException e) {
				if (i==0) {
					throw e;
				}
				return i;
			}
		}
		return i;
	}
	public String read(int len)
	throws EOFException,IOException {
		String str="";
		int bl;
		for(int i=0;i<len;i++) {
			try {
				bl=read();
				str+=(char)bl;
			} catch (EOFException e) {
				if (str=="") {
					throw e;
				}
				return str;
			}
		}
		return str;
	}
	public String readLine()
	throws EOFException,IOException {
		String str="";
		int bl;
		do {
			try {
				bl=read();
				if ((bl==10)||(bl==13)) {
					int bl1=read();
					if ((bl!=13)&&(bl1!=10)) {
						push((byte)bl1);
					}
				} else {
					
					str+=(char)bl;
				}
			} catch(EOFException e) {
				if (str=="") {
					throw e;
				}
				return str;
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
