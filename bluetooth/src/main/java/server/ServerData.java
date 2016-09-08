package server;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;

public class ServerData {
	
	

	
	public static String displaydata_tmp(BluetoothSocket bsocket) throws InterruptedException{
		String str=null;
		try {
			
			InputStream is=bsocket.getInputStream();
			
			byte[] buffer = new byte[2048];
			int bytes=0;
	         bytes = is.read(buffer,0,2048);
			str= new String(buffer,"ISO-8859-1");
			str=str.substring(0,bytes);
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
		
		}
	
	


}
