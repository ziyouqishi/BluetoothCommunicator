package com.zhimei.hardware;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import custom.MyApplication;
import tools.ConnectThread;
import tools.ConnectedThread;
import tools.Constant;
import tools.DisplayString;

public class ControlActivity extends AppCompatActivity {
    private TextView temp,damp;
    private Switch light,window;
    private Button lianjie;
    public ConnectThread connectThread;
    public ConnectedThread ctd;
   // private TextView testData;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.DATAS:
                    /*Toast.makeText(ControlActivity.this, ctd.datas, Toast.LENGTH_SHORT).show();
                    testData.setText(ctd.datas);*/

                    DisplayString displayString=new DisplayString();
                    displayString.setData(ctd.datas);
                    if(displayString.isRight(displayString.getData())){
                        displayString.separate();
                        temp.setText(displayString.getTempAndDamp(displayString.temp));
                        damp.setText(displayString.getTempAndDamp(displayString.damp));

                        if(displayString.getState(displayString.light).equals("1")){
                            light.setChecked(true);
                            light.setText("开");
                        }

                        if(displayString.getState(displayString.light).equals("0")){
                            light.setChecked(false);
                            light.setText("关");
                        }

                        if(displayString.getState(displayString.window).equals("1")){
                            window.setChecked(true);
                            window.setText("开");
                        }

                        if(displayString.getState(displayString.window).equals("0")){
                            window.setChecked(false);
                            window.setText("关");
                        }


                    }else {
                        Toast.makeText(ControlActivity.this, "数据格式错误", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        initView();

    }

    void initView(){
        temp=(TextView)findViewById(R.id.current_temp);
        damp=(TextView)findViewById(R.id.current_damp);
        light=(Switch)findViewById(R.id.switch_light);
        window=(Switch)findViewById(R.id.switch_window);
        lianjie=(Button) findViewById(R.id.connect);
        //testData=(TextView)findViewById(R.id.test_data);
        light.setEnabled(false);
        window.setEnabled(false);

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    light.setText("开");
                    String data = "0xbb0xff" + returnState(true) + returnState(window.isChecked());
                    ctd.write(data.getBytes());
                } else {
                    String data = "0xbb0xff" + returnState(false) + returnState(window.isChecked());
                    ctd.write(data.getBytes());
                    light.setText("关");
                }

            }
        });


        window.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    window.setText("开");
                    String data = "0xbb0xff" + returnState(light.isChecked()) + returnState(true);
                    ctd.write(data.getBytes());
                } else {
                    window.setText("关");
                    String data = "0xbb0xff" + returnState(light.isChecked()) + returnState(false);
                    ctd.write(data.getBytes());
                }
            }
        });

        lianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectThread = new ConnectThread(MyApplication.getBd());
                lianjie.setText("正在连接中...");

                /**
                 * 连接后的监听事件
                 */
                connectThread.setConnectListener(new ConnectThread.ConnectListener() {
                    @Override
                    public void connected() {
                        /**
                         * 连接成功，启动IO流读写数据
                         */

                        ctd = new ConnectedThread(connectThread.mmSocket,handler);


                        ctd.start();
                        Toast.makeText(ControlActivity.this, "已经成功连接", Toast.LENGTH_SHORT).show();
                        light.setEnabled(true);
                        window.setEnabled(true);
                        lianjie.setBackgroundColor(Color.parseColor("#FF00CC2C"));
                        lianjie.setText("已连接");


                    }
                });
                /**
                 * 进行连接，要写在连接监听事件的后面。
                 */
                connectThread.testConnect();
                //getData();


            }
        });


    }




    public String returnState(boolean isChecked){
        if(isChecked){
            return "0x01";
        }else {
            return "0x00";
        }

    }
}
