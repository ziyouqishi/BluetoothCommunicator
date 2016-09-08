package tools;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 张佳亮 on 2016/8/10.
 */
public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    public String datas;
    public GetDataListener getDataListener;
    public Message message;
    public Handler handler;

    public ConnectedThread(BluetoothSocket socket,Handler handlers) {
        mmSocket = socket;
        this.handler=handlers;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

        }
           mmInStream = tmpIn;
           mmOutStream = tmpOut;
    }

    /**
     * 接收数据，线程
     */
    public void run() {
        byte[] buffer = new byte[2048];
        int bytes;
        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                String str= new String(buffer,"ISO-8859-1");
                str=str.substring(0,bytes);
                datas=str;
                //getDataListener.getData();
                Log.e("read", str);
                message=new Message();
                message.what=Constant.DATAS;
                handler.sendMessage(message);
            } catch (IOException e) {
                break;
            }
        }
    }



    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public interface GetDataListener{
        void getData();
    }

    public void setGetDataListener(GetDataListener getDataListener) {
        this.getDataListener = getDataListener;
    }
}
