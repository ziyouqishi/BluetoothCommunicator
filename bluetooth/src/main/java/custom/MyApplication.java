package custom;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by 张佳亮 on 2016/8/8.
 */
public class MyApplication extends Application {
    private static Context context; //上下文
    private static BluetoothDevice bd;//远程设备
    private static BluetoothAdapter blueadapter;
    public static BluetoothAdapter getBlueadapter() {
        return blueadapter;
    }

    public static void setBlueadapter(BluetoothAdapter blueadapter) {
        MyApplication.blueadapter = blueadapter;
    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    public static BluetoothDevice getBd() {
        return bd;
    }

    public static void setBd(BluetoothDevice bd) {
        MyApplication.bd = bd;
    }


}
