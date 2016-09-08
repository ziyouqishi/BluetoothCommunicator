package com.zhimei.hardware;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import custom.MyApplication;

public class MainActivity extends AppCompatActivity {
    private static final int DISCOVERY_REQUEST = 1;//这是一个整型常量
    /*
     * 一个list集合，用来装发现的所有扫描发现的远程设备
     */
    private List<String> list = new ArrayList<String>();

    private Button start;
    private Button scan;
    private Button clear;
    private ListView listview;//定义listview
    private BluetoothAdapter adapter;
    private ScanReceiver sr;//定义了一个该内部类的变量
    ArrayAdapter<String> arrayadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();//调用此方法，对控件进行初始化。
        start();//调用start（）方法，关于蓝牙设备的初始化设置
        broadcast();// 对接收到的蓝牙信息进行处理
        scan();//扫描周围的远程设备
        clearList();//清空列表
    }

    /*控件初始化
 * 目前的控件都是按钮，
 *
 */
    void init() {
        start = (Button) findViewById(R.id.start);
        scan = (Button) findViewById(R.id.scan);
        clear=(Button)findViewById(R.id.clear);
        listview=(ListView)findViewById(R.id.listview);

    }

    /*
 * 得到本地的蓝牙适配器
 * 并且打开蓝牙设备，设置可检测时间为300秒
 */
    void start() {
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter = BluetoothAdapter.getDefaultAdapter();//取得本机蓝牙适配器对象
                // 直接打开系统的蓝牙设置面板
                if (!adapter.isEnabled()) // 如果蓝牙没有打开，通过这个Activity打开
                {//未打开进入此函数
                    Intent intent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                    // adapter.enable();
                }
                Intent enable = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                        300); // 3600为蓝牙设备可见时间
                startActivityForResult(enable, DISCOVERY_REQUEST);

            }
        });

    }


    /*
 * (non-Javadoc)
 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
 * 执行上面的方法时会执行此方法
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DISCOVERY_REQUEST)
            if (resultCode == RESULT_CANCELED) {
                Log.i("liang", "该发现功能被取消");
            }
    }


    	/*
	 * 利用已经得到的蓝牙适配器扫描周围的设备
	 */

    void scan() {
        scan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (adapter!=null) {// 如果蓝牙打开，则进行扫描
                    adapter.startDiscovery();// 打开扫描，开始扫描周围的设备，扫描到一台，则发出一条广播。
                    Toast.makeText(MainActivity.this, "扫描时间可能需要十多秒",//弹出提出信息
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "请打开蓝牙设备",//弹出提出信息
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    	/*
	 * 负责接收广播
	 */

    void broadcast() {
        IntentFilter inf = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // IntentFilter inf_2=new
        // IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // IntentFilter inf_2=new
        // IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        sr = new ScanReceiver();
        registerReceiver(sr, inf);

    }

    /*
 * 清空列表
 */
    void clearList(){
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new ScanReceiver().map.clear();
                if (list != null && arrayadapter != null) {
                    list.clear();
                    arrayadapter.notifyDataSetChanged();
                }

            }
        });

    }

    	/*
	 * (non-Javadoc)
 	 * @see android.app.Activity#onDestroy()
	 * 退出程序时，必须取消对广播的注册
	 */

    @Override
    protected void onDestroy() {
        unregisterReceiver(sr);
        System.exit(0);
        super.onDestroy();
    }


    /*
 * 该内部类负责接收到广播后，对广播进行处理，即接收到广播后的响应。
 */
    class ScanReceiver extends BroadcastReceiver {
        HashMap<String , BluetoothDevice> map = new HashMap<String , BluetoothDevice>();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();//搜到设备时，系统发出的广播为BluetoothDevice.ACTION_FOUND
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothdevice = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);// 得到设备对象

				/*
				 * HashMap通过键值对装入远程设备的名字和远程设备的对象，
				 * 得到远程设备的名字目的主要是为了方便用户操作。
				 * 设置此map的目的的作用是为了用户仅通过远程设备的名字进行连接
				 */
                //map = new HashMap<String , BluetoothDevice>();
                map.put(bluetoothdevice.getName(), bluetoothdevice);//每扫描到一个远程设备就装入map中
                list.add(bluetoothdevice.getName());//将远程设备的名称装入list集合中，目的是为了在listview（列表）中显示

                //定义适配器，listview必须需要设配器
                arrayadapter=new ArrayAdapter<String>(
                        MainActivity.this,android.R.layout.simple_list_item_1,list);
                listview.setAdapter(arrayadapter);//给listview设置适配器

                //添加listview的点击事件
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //得到当前的远程设备的对象
                        MyApplication.setBd(map.get(list.get(position)));//传递远程设备的对象
                        MyApplication.setBlueadapter(adapter);//传递BluetoothAdapter
                        Intent intent=new Intent(MainActivity.this,ControlActivity.class);
                        startActivity(intent);

                    }
                });

                Toast.makeText(MainActivity.this, bluetoothdevice.getName(),
                        Toast.LENGTH_SHORT).show();
                if (bluetoothdevice.getName().equals("HTC D610t")) {
                    adapter.cancelDiscovery();
                }
            }
        }
    }
}
