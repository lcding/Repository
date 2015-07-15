package com.ecovacs.android_cr1301;

import com.ecovacs.android_cr1301.FormMenu.MyRunnable;
import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;
import com.ecovacs.uart.Config_ZigBee;
import com.ecovacs.uart.PollData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class XinZeng extends Activity {
	AlertDialog.Builder builder;
	Dialog dialog_fail,dialog_config,dialog_success ;
	
	public BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;
	public static boolean flag = false ;
	private static boolean run_flag = false;
	int index = 0 , index_count=0 , run_index = 0;
	public static String ADDRESS = "" ,NETWORK;
	private Button button_config01,button_config02,button_setting01,button_xinzeng01,btn;
	private EditText wangduan01,duandizhi01;
	private TextView TVxznumbershow,TVxznetworkshow,TVxzaddressshow;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮

	private Handler handler_xinzeng = new Handler();
	private MyRunnable myrunnable= new MyRunnable();
	class MyRunnable implements Runnable{
		@Override
		public void run() {	
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
				if(index!=2){
					send_order(index);
				}else{
					send_order(index);
				}
			}
			if(index==0){
				index_count=0;
				Intent toFormIntent = new Intent(XinZeng.this,
						FormMenu.class);
				startActivity(toFormIntent);
			}
			//System.out.println(run_index);
			if(run_index>10){//添加十次加不上认为加不上
				peizhi(0);
				handler_xinzeng.removeCallbacks(myrunnable1);
				handler_xinzeng.removeCallbacks(myrunnable);
			}
			//调用另一个线程
			if(index!=0)
			handler_xinzeng.postDelayed(myrunnable1, 1000);
		}
	}

	private MyRunnable1 myrunnable1= new MyRunnable1();//用于判断筛选信息，确定成功再调用
	class MyRunnable1 implements Runnable{
		@Override
		public void run() {	
			int i = 0;
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{						
				switch(index){
				case 1 :i=config_result(find_config(BluetoothChat.readMessage));//判断1
				if(i == 1){
					handler_xinzeng.postDelayed(myrunnable, 1000);//发送2
					handler_xinzeng.removeCallbacks(myrunnable1);
					i=0;
				};break;
				case 2 :i=config_result(find_rabot_config(BluetoothChat.readMessage));//判断2
				if(i == 1){
					run_index=0;i=0;
					handler_xinzeng.postDelayed(myrunnable, 1000);//发送3					
					handler_xinzeng.removeCallbacks(myrunnable1);
				}else{
					index = 2;
					run_index++;		
					handler_xinzeng.postDelayed(myrunnable, 1000);//发送2
					handler_xinzeng.removeCallbacks(myrunnable1);					
				};break;

				case 3 :i=config_result(find_config(BluetoothChat.readMessage));//判断3				
				if(i == 1){
					handler_xinzeng.postDelayed(myrunnable, 1000);//发送4	
					i=0;
					handler_xinzeng.removeCallbacks(myrunnable1);
				};break;
				case 4 :i=config_result(find_config(BluetoothChat.readMessage));//判断4	
				if(i==1){
					dialog_config.dismiss();//移除popup_box_tianjiazhong子框
					flag = false;
					i=0;//调用发送指令线程
					PollData.save(ADDRESS, "test");//存短地址
					handler_xinzeng.postDelayed(myrunnable, 2000);					
					peizhi(1);
					index = 0;
					run_index = 0;
					index_count = 0;
				};break;
				default:;break;
				}
			}
			
			
			
			if(index_count<=60){
				handler_xinzeng.postDelayed(myrunnable1, 1000);
			}else{
				dialog_config.dismiss();//移除popup_box_tianjiazhong子框
				peizhi(0);
				handler_xinzeng.removeCallbacks(myrunnable1);
				handler_xinzeng.removeCallbacks(myrunnable);
			}

		}
	}
	public void wait_time(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();           
		}
	}
	
	//配置弹出窗口的选择
	public void peizhi(int i){
			
			//		final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_tianjiazhong, null); //xml模板生成视图模板
			//		new AlertDialog.Builder(XinZeng.this).setTitle("Tips:")
			//		.setView(DialogtianjiaView).show() ;// 设置视图模板
			if(i==0){
				run_index = 0;
				LayoutInflater factory_win = LayoutInflater.from(XinZeng.this); // 图层模板生成器句柄
				final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_shibai,null); //xml模板生成视图模板
				dialog_fail = builder.setTitle("Fail:").setView(DialogtianjiaView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,
							int whichButton) {
						index_count=0;
						dialog_config.dismiss();//配置中的框隐藏
					}
				}).setNegativeButton("取消", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						index_count=0;
						dialog_config.dismiss();//配置中的框隐藏
					}
				}).show();

			}
			if(i==1){
				LayoutInflater factory_win = LayoutInflater.from(XinZeng.this); // 图层模板生成器句柄
				final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_success, null); //xml模板生成视图模板
				dialog_success =builder.setTitle("Success:").setView(DialogtianjiaView).show() ;// 设置视图模板
				dialog_config.dismiss();//配置中的框隐藏
			}
		}
	//利用生命周期管理线程
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		flag = true;
		handler_xinzeng.removeCallbacks(myrunnable);
		handler_xinzeng.removeCallbacks(myrunnable1);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		flag = false;
		handler_xinzeng.removeCallbacks(myrunnable);
		handler_xinzeng.removeCallbacks(myrunnable1);
		super.onPause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gongzuo_jiqi_xinzeng);//设置画面为主画面 gongzuo_xiangqing_xinzeng.xml

		builder = new AlertDialog.Builder(XinZeng.this);

		button_config01 = (Button) findViewById(R.id.button_config01);
		button_config01.setOnClickListener(btnClickListener);
		button_config02 = (Button) findViewById(R.id.button_config02);
		button_config02.setOnClickListener(btnClickListener);
		button_setting01 = (Button) findViewById(R.id.button_setting01);
		button_setting01.setOnClickListener(btnClickListener);
		button_xinzeng01 = (Button) findViewById(R.id.button_xinzeng01);
		button_xinzeng01.setOnClickListener(btnClickListener);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(btnClickListener);

		wangduan01 = (EditText) findViewById(R.id.wangduan01);
		duandizhi01 = (EditText) findViewById(R.id.duandizhi01);
		TVxznumbershow = (TextView) findViewById(R.id.TVxznumbershow);
		TVxznetworkshow = (TextView) findViewById(R.id.TVxznetworkshow);
		TVxzaddressshow = (TextView) findViewById(R.id.TVxzaddressshow);
		//PollData.save(msg, filename);//(要保存的字符串，要保存的文件名)
		//PollData.read(filename);//默认路径下的文件名
	}

	/**
	 * 按钮监听响应
	 */
	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {//蓝牙必须连接

			}else{
				switch (v.getId()) {
				case R.id.button_config01:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						String config1 = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//自己想配置的协调器
						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600"配置成目标网络的协调器
						sendMessageStr(BluetoothChat.hexxor(config1));	

					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}
					break;					
				case R.id.button_config02:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){					
						String config = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601";//自己想配置的路由器		
						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601"配置成目标网络的路由器
						sendMessageStr(BluetoothChat.hexxor(config));						
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.button_setting01:
					String set1 = "@ZA0A400400190000FFFF0600";//默认配置

					sendMessageStr(BluetoothChat.hexxor(set1));			
					break;
				case R.id.button_xinzeng01:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						String add = "@Z90AF1"+NETWORK+"1A"+ADDRESS+"000002AB";//新增机器配置的					
						sendMessageStr(BluetoothChat.hexxor(add));
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}

					break;	
				case R.id.btn:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						//						String config1 = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//自己想配置的
						//						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600"配置成目标网络的协调器
						//						sendMessageStr(BluetoothChat.hexxor(config1));
						flag = false ;index = 1;//执行第一步配置
						//send_order(index);
						handler_xinzeng.postDelayed(myrunnable,50);//启动线程

						LayoutInflater factory_win = LayoutInflater.from(XinZeng.this);
						final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_tianjiazhong, null); //xml模板生成视图模板
						dialog_config = builder.setTitle("Config:").setView(DialogtianjiaView).show() ;// 设置视图模板
						dialog_config.setCanceledOnTouchOutside(false);//外部点击不消失
						//配置缓冲窗口全屏
//						getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
//								WindowManager.LayoutParams.FLAG_FULLSCREEN);  
//						setContentView(R.layout.popup_box_tianjiazhong);
						
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}

					//					PollData.save(ADDRESS, "test");//存短地址
					//					Intent toFormIntent = new Intent(XinZeng.this,
					//							FormMenu.class);
					//					startActivity(toFormIntent);
					break;
				default:break;
				}
			}
		}
	}

	//找出@ZB02030302> 0表示没找到，1表示找到
	public int find_config(String readmessage){
		if (readmessage != null && readmessage.contains("@ZB02030302>")) return 1;
		return 0;	
	}
	//找出@Z902F11F02> 0表示没找到，1表示找到
	public int find_rabot_config(String readmessage){
		if (readmessage != null && readmessage.contains("@Z902F11F02>")) return 1;
		return 0;	
	}
	//配置是否成功  0表示配置失败，1表示配置成功
	public int config_result(int i){
		BluetoothChat.readMessage = null;//清空
		if(i == 1){
			System.out.println("配置成功"+index);
			index++;flag = false;//线程时间等待8s——>1s
			return 1;
		}
		System.out.println("配置失败");
		flag = false;//线程时间等待8s——>1s
		index_count++;
		//handler_xinzeng.removeCallbacks(myrunnable);//关闭线程
		return 0;
	}
	//接收找出短地址，判断是否真的添加成功
	public int find_address(){
		if(BluetoothChat.readMessage.contains(ADDRESS))return 1;
		return 0;

	}
	//发送指令的方法
	public void send_order(int index_flag){
		switch (index_flag){
		case 1:flag = true;String set_one = "@ZA0A400400190000FFFF0600";//1配置默认
		sendMessageStr(BluetoothChat.hexxor(set_one));break;
		case 2:flag = false;String set_two = "@Z90AF1"+NETWORK+"1A"+ADDRESS+"000002AB";//2添加机器
		sendMessageStr(BluetoothChat.hexxor(set_two));break;
		case 3:flag = true;String set_three = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601";//3配置路由器
		sendMessageStr(BluetoothChat.hexxor(set_three));break;
		case 4:flag = true;String set_four = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//4配置协调器
		sendMessageStr(BluetoothChat.hexxor(set_four));break;
		default:break;
		}
	}
	private  void getAddress(){//获得输入的网段和短地址
		NETWORK = wangduan01.getText().toString();
		NETWORK = BluetoothChat.captital(NETWORK);
		if(NETWORK.length()==4){
			NETWORK = NETWORK.substring(2, 4)+NETWORK.substring(0, 2);
			TVxznetworkshow.setText(NETWORK);
		}else{
			NETWORK = "1111";
			TVxznetworkshow.setText(NETWORK);
		}

		ADDRESS = duandizhi01.getText().toString();
		ADDRESS = BluetoothChat.captital(ADDRESS);
		if(ADDRESS.length()==4){
			ADDRESS = ADDRESS.substring(2, 4)+ADDRESS.substring(0, 2);
			TVxzaddressshow.setText(ADDRESS);
		}else{
			ADDRESS = "1111";
			TVxzaddressshow.setText(ADDRESS);
		}
	}
	/**
	 * 返回键的重写
	 */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			Intent xinzengtoFormMenuIntent = new Intent(XinZeng.this,
					FormMenu.class);
			startActivity(xinzengtoFormMenuIntent);
			return super.onKeyDown(keyCode, event);
		}

	public void sendMessageStr(String message) {//发送字符串的方法
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			bluetoothchatservice.write(send);
		}

	}
}
