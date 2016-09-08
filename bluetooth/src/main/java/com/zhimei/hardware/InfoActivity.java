package com.zhimei.hardware;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import custom.MyApplication;
import custom.VerticalSeekBar;
import server.ServerPort;

public class InfoActivity extends AppCompatActivity {
    private TextView display;
    private VerticalSeekBar seekbar;
    private TextView display_2;
    private TextView current_tmp;
    private TextView current_light;
    private VerticalSeekBar seekbar_2;
    private Button startserver;
    private Button filllight;
    private BluetoothAdapter blue=null;
    private BluetoothDevice bd=null;
    private int progress_temp;
    private int progress_light;
    private String data_temp;
    private String data_light;
    private String send_temp;
    private String send_light;
    private static final int WENDU=1;
    private static final int GUANG=2;
    private static final int ERROR=3;
    private static final int NULL=4;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WENDU:
                    current_tmp.setText(data_temp);
                    break;
                case GUANG:
                    current_light.setText(data_light);
                    break;
                case ERROR:
                    Toast.makeText(InfoActivity.this, "数据格式错误", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case NULL:
                    Toast.makeText(InfoActivity.this, "数据为空", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        init();
        display();
        lastObject();
        openserver();//从客户端接收数据
        //ClientData.connect(bd);//连接主机
        ServerPort.connect(bd, current_tmp, current_light);
        filllight();//补光
        getData();
    }


    /*
 * 控件初始化
 */
    void init(){
        display=(TextView)findViewById(R.id.display);
        seekbar=(VerticalSeekBar)findViewById(R.id.seekbar);
        display_2=(TextView)findViewById(R.id.display_2);
        seekbar_2=(VerticalSeekBar)findViewById(R.id.seekbar_2);
        current_tmp=(TextView)findViewById(R.id.current);
        current_light=(TextView)findViewById(R.id.current_2);
        startserver=(Button)findViewById(R.id.accept);
        filllight=(Button)findViewById(R.id.fillLight);
    }

    /*
     * 拖动条设置，使其数值显示在文本中
     */
    void display(){
        seekbar.setMax(50);


        seekbar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener(){


            @Override
            public void onProgressChanged(VerticalSeekBar VerticalSeekBar,
                                          int progress, boolean fromUser) {
                progress_temp=progress;
                display.setText(progress+"℃");

            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                send_temp="0X55"+progress_temp+"0X8C";
                //ClientData.send_2(send_temp);//将温度数据以约定的格式发送出去
                ServerPort.sendData(send_temp);
            }} );

        seekbar_2.setMax(100);
        seekbar_2.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(VerticalSeekBar VerticalSeekBar,
                                          int progress, boolean fromUser) {
                progress_light=progress;
                display_2.setText(progress+"%");

            }

            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                send_light="0X8A"+progress_light+"0X8C";

                //ClientData.send_2(send_light);//将光照数据以约定的格式发送出去
                ServerPort.sendData(send_light);
            }


        });
    }


    /*
 * 得到上一个Activity中的对象
 */
    void lastObject(){
        blue= MyApplication.getBlueadapter();
        bd=MyApplication.getBd();

        if(blue.isEnabled()){
            Log.i("liang", "正常");
        }

    }

    /*
     * 打开客户端
     */
    void openserver(){
        startserver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("liang", "此方法已经执行");
                accept_data();
                //ServerPort.operate_temp(blue);
            }
        });

    }

	/*
	 * 接收数据
	 */

    void accept_data(){
        ServerPort.operate_temp(blue,current_tmp,current_light);//以BluetoothAdapter为参数，这个对象是在上一个ActivityHongkong获取的


        Log.i("liang", "吕施良");
        new Thread(new Runnable(){

            @Override
            public void run() {


                while(true){
                    try {

                        Thread.sleep(1000);
                        Log.i("liang", "在无限循环执行");
                        if(ServerPort.data.contains("RT")){
                            data_temp=ServerPort.data.substring(2,ServerPort.data.length()-1);
                            Message message1 = new Message();
                            message1.what = WENDU;
                            handler.sendMessage(message1);
                        }
                        else if(ServerPort.data.contains("RG")){
                            data_light=ServerPort.data.substring(2,ServerPort.data.length()-1);
                            Message message2 = new Message();
                            message2.what = GUANG;
                            handler.sendMessage(message2);
                        }
                        else if(ServerPort.data==""){
                            Message message4 = new Message();
                            message4.what = NULL;
                            handler.sendMessage(message4);
                            break;

                        }
                        else{
                            Log.i("liang", ServerPort.data);
                            Toast.makeText(InfoActivity.this, ServerPort.data+"", Toast.LENGTH_SHORT)
                                    .show();
                            Log.i("liang",ServerPort.data);
                            Message message3 = new Message();
                            message3.what = ERROR;
                            handler.sendMessage(message3);
                            break;
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }}).start();
    }


    	/*
	 * 补光
	 */

    void filllight(){
        filllight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ClientData.send_2("TB1*");
                ServerPort.sendData("TB1*");
            }
        });
    }


    void getData(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(100);
                        if(ServerPort.string!=""){
                            if(ServerPort.string.contains("0XAA")&&ServerPort.string.contains("0X8C")){
                                data_temp=ServerPort.data.substring(3,7);
                                Message message1 = new Message();
                                message1.what = WENDU;
                                handler.sendMessage(message1);
                            }
                            else if(ServerPort.string.contains("0XB8")&&ServerPort.string.contains("0X8C")){
                                data_light=ServerPort.data.substring(3,5);
                                Message message2 = new Message();
                                message2.what = GUANG;
                                handler.sendMessage(message2);
                            }
                            else{
                                Log.i("liang", "数据格式错误");
                            }
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
