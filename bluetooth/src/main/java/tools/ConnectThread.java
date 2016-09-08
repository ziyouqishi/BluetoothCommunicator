package tools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import custom.MyApplication;

/**
 * Created by 张佳亮 on 2016/8/10.
 */
public class ConnectThread extends Thread {
    /*public ConnectedThread ced;*/
    private static String uuid = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothAdapter adapter;
    public  BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    public ConnectListener connectListener;

    public ConnectThread(BluetoothDevice device) {
        adapter= MyApplication.getBlueadapter();
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        }
        catch (IOException e) {

        }
        mmSocket = tmp;

    }

    @Override
    public void run() {
        adapter.cancelDiscovery();

        try {

           // mmSocket.connect();
            //testConnect();
            /*ced=new ConnectedThread(mmSocket);
            ced.start();*/
            connectListener.connected();
            Log.d("liang", "connectListener.connected()");
        }
        catch (Exception e) {
            Log.e("connect0e", e.toString());
            try {
                mmSocket.close();
            } catch (Exception e1) {
                Log.e("close", e1.toString());
            }
        }
    }

    public interface ConnectListener{
        void connected();
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }


    public void testConnect(){

        adapter.cancelDiscovery();

        try {

            mmSocket.connect();
           /* *//**
             * 连接成功，启动IO流读写数据
             *//*
            ced=new ConnectedThread(mmSocket);
            ced.start();*/
            /**
             * 连接成功后，告诉控制界面已经连接成功，可以发送和接收数据了。
             */
            connectListener.connected();
            Log.d("liang", "connectListener.connected()");
        }
        catch (Exception e) {
            Log.e("connect0e", e.toString());
            try {
                mmSocket.close();
            } catch (Exception e1) {
                Log.e("close", e1.toString());
            }
        }

    }
}
