package server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class ServerPort extends Thread {
	private static String uuid = "00001101-0000-1000-8000-00805F9B34FB";
	public static String data=""; 
	public static String string="";
	private static BluetoothSocket socket=null;
	private static OutputStream os=null;
	
	
    public static String operate_temp(BluetoothAdapter badapter,final TextView tv1,final TextView tv2){
    	try {
    		final BluetoothServerSocket tmp =badapter.listenUsingRfcommWithServiceRecord("blue",UUID.fromString(uuid));
			new Thread(new Runnable(){
				boolean flag;

				@Override
				public void run() {
					//BluetoothSocket socket=null;
					 try {
						socket=tmp.accept();
						flag=true;
						 Log.i("liang", "不仅仅靠蒋经国规划看看咯根据");
					} catch (IOException e1) {
						 Log.i("liang", "你失败了！");
						e1.printStackTrace();
						flag=false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					while(flag){  
						try {
							
						 
							
							if(socket!=null){
								Log.i("liang", "两台设备已经成功连接");
							Thread.sleep(8000);
			                data =ServerData.displaydata_tmp(socket);
								Log.i("liang", "收到的数据为"+data);
							if(data.contains("TT")){
								tv1.setText(data.substring(2, 4));
							}
							else if(data.contains("TG")){
							tv2.setText(data.substring(2, 4));	
							}
							else{
								Log.i("liang", "未收到数据");
							}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
				}}).start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
		}
    
    public static void sendData(final String data){
    	new Thread(new Runnable(){

    		@Override
    		public void run() {
    			try {
    				  if(socket!=null){
    					  os=socket.getOutputStream();
    						byte [] bytestring=data.getBytes();
    						os.write(bytestring);
    				  }	
    				  else{
						  Log.i("liang", "发送数据错误");
    				  }
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			
    		}}).start();
    }
    
    public static void connect(BluetoothDevice bd,final TextView tv1,final TextView tv2){
  	  try {
  			socket=bd.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
  		} catch (IOException e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
  		new Thread(new Runnable(){
          boolean flag;
  			@Override
  			public void run() {
  				try {
  					socket.connect();
  					flag=true;
  					
  				} catch (IOException  e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  					flag=false;
  				}
  				
				while(flag){  
					try {
						
					 
						
						if(socket!=null){
							Log.i("liang", "两台设备已经成功连接");
		                data =ServerData.displaydata_tmp(socket);		                
		                string=string+data;
		                if(string.endsWith("C")){
							Log.i("liang", "收到的数据为"+string);
		                	Thread.sleep(500);
		                	string="";
		                }
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
  				
  			}}).start();
    }
    
    
    

	
}
