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
	private ButtonClickListener btnClickListener = new ButtonClickListener();//��ť

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
			if(run_index>10){//���ʮ�μӲ�����Ϊ�Ӳ���
				peizhi(0);
				handler_xinzeng.removeCallbacks(myrunnable1);
				handler_xinzeng.removeCallbacks(myrunnable);
			}
			//������һ���߳�
			if(index!=0)
			handler_xinzeng.postDelayed(myrunnable1, 1000);
		}
	}

	private MyRunnable1 myrunnable1= new MyRunnable1();//�����ж�ɸѡ��Ϣ��ȷ���ɹ��ٵ���
	class MyRunnable1 implements Runnable{
		@Override
		public void run() {	
			int i = 0;
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{						
				switch(index){
				case 1 :i=config_result(find_config(BluetoothChat.readMessage));//�ж�1
				if(i == 1){
					handler_xinzeng.postDelayed(myrunnable, 1000);//����2
					handler_xinzeng.removeCallbacks(myrunnable1);
					i=0;
				};break;
				case 2 :i=config_result(find_rabot_config(BluetoothChat.readMessage));//�ж�2
				if(i == 1){
					run_index=0;i=0;
					handler_xinzeng.postDelayed(myrunnable, 1000);//����3					
					handler_xinzeng.removeCallbacks(myrunnable1);
				}else{
					index = 2;
					run_index++;		
					handler_xinzeng.postDelayed(myrunnable, 1000);//����2
					handler_xinzeng.removeCallbacks(myrunnable1);					
				};break;

				case 3 :i=config_result(find_config(BluetoothChat.readMessage));//�ж�3				
				if(i == 1){
					handler_xinzeng.postDelayed(myrunnable, 1000);//����4	
					i=0;
					handler_xinzeng.removeCallbacks(myrunnable1);
				};break;
				case 4 :i=config_result(find_config(BluetoothChat.readMessage));//�ж�4	
				if(i==1){
					dialog_config.dismiss();//�Ƴ�popup_box_tianjiazhong�ӿ�
					flag = false;
					i=0;//���÷���ָ���߳�
					PollData.save(ADDRESS, "test");//��̵�ַ
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
				dialog_config.dismiss();//�Ƴ�popup_box_tianjiazhong�ӿ�
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
	
	//���õ������ڵ�ѡ��
	public void peizhi(int i){
			
			//		final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_tianjiazhong, null); //xmlģ��������ͼģ��
			//		new AlertDialog.Builder(XinZeng.this).setTitle("Tips:")
			//		.setView(DialogtianjiaView).show() ;// ������ͼģ��
			if(i==0){
				run_index = 0;
				LayoutInflater factory_win = LayoutInflater.from(XinZeng.this); // ͼ��ģ�����������
				final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_shibai,null); //xmlģ��������ͼģ��
				dialog_fail = builder.setTitle("Fail:").setView(DialogtianjiaView)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,
							int whichButton) {
						index_count=0;
						dialog_config.dismiss();//�����еĿ�����
					}
				}).setNegativeButton("ȡ��", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						index_count=0;
						dialog_config.dismiss();//�����еĿ�����
					}
				}).show();

			}
			if(i==1){
				LayoutInflater factory_win = LayoutInflater.from(XinZeng.this); // ͼ��ģ�����������
				final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_success, null); //xmlģ��������ͼģ��
				dialog_success =builder.setTitle("Success:").setView(DialogtianjiaView).show() ;// ������ͼģ��
				dialog_config.dismiss();//�����еĿ�����
			}
		}
	//�����������ڹ����߳�
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
		setContentView(R.layout.gongzuo_jiqi_xinzeng);//���û���Ϊ������ gongzuo_xiangqing_xinzeng.xml

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
		//PollData.save(msg, filename);//(Ҫ������ַ�����Ҫ������ļ���)
		//PollData.read(filename);//Ĭ��·���µ��ļ���
	}

	/**
	 * ��ť������Ӧ
	 */
	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {//������������

			}else{
				switch (v.getId()) {
				case R.id.button_config01:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						String config1 = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//�Լ������õ�Э����
						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600"���ó�Ŀ�������Э����
						sendMessageStr(BluetoothChat.hexxor(config1));	

					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}
					break;					
				case R.id.button_config02:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){					
						String config = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601";//�Լ������õ�·����		
						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601"���ó�Ŀ�������·����
						sendMessageStr(BluetoothChat.hexxor(config));						
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.button_setting01:
					String set1 = "@ZA0A400400190000FFFF0600";//Ĭ������

					sendMessageStr(BluetoothChat.hexxor(set1));			
					break;
				case R.id.button_xinzeng01:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						String add = "@Z90AF1"+NETWORK+"1A"+ADDRESS+"000002AB";//�����������õ�					
						sendMessageStr(BluetoothChat.hexxor(add));
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}

					break;	
				case R.id.btn:
					getAddress();
					if(BluetoothChat.checkNum(NETWORK)&&BluetoothChat.checkNum(ADDRESS)){
						//						String config1 = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//�Լ������õ�
						//						//"@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600"���ó�Ŀ�������Э����
						//						sendMessageStr(BluetoothChat.hexxor(config1));
						flag = false ;index = 1;//ִ�е�һ������
						//send_order(index);
						handler_xinzeng.postDelayed(myrunnable,50);//�����߳�

						LayoutInflater factory_win = LayoutInflater.from(XinZeng.this);
						final View DialogtianjiaView = factory_win.inflate(R.layout.popup_box_tianjiazhong, null); //xmlģ��������ͼģ��
						dialog_config = builder.setTitle("Config:").setView(DialogtianjiaView).show() ;// ������ͼģ��
						dialog_config.setCanceledOnTouchOutside(false);//�ⲿ�������ʧ
						//���û��崰��ȫ��
//						getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
//								WindowManager.LayoutParams.FLAG_FULLSCREEN);  
//						setContentView(R.layout.popup_box_tianjiazhong);
						
					}else{
						Toast.makeText(getApplicationContext(), "NETWORK Segment and Short Address must be 0~FFFE"
								, Toast.LENGTH_SHORT).show();
					}

					//					PollData.save(ADDRESS, "test");//��̵�ַ
					//					Intent toFormIntent = new Intent(XinZeng.this,
					//							FormMenu.class);
					//					startActivity(toFormIntent);
					break;
				default:break;
				}
			}
		}
	}

	//�ҳ�@ZB02030302> 0��ʾû�ҵ���1��ʾ�ҵ�
	public int find_config(String readmessage){
		if (readmessage != null && readmessage.contains("@ZB02030302>")) return 1;
		return 0;	
	}
	//�ҳ�@Z902F11F02> 0��ʾû�ҵ���1��ʾ�ҵ�
	public int find_rabot_config(String readmessage){
		if (readmessage != null && readmessage.contains("@Z902F11F02>")) return 1;
		return 0;	
	}
	//�����Ƿ�ɹ�  0��ʾ����ʧ�ܣ�1��ʾ���óɹ�
	public int config_result(int i){
		BluetoothChat.readMessage = null;//���
		if(i == 1){
			System.out.println("���óɹ�"+index);
			index++;flag = false;//�߳�ʱ��ȴ�8s����>1s
			return 1;
		}
		System.out.println("����ʧ��");
		flag = false;//�߳�ʱ��ȴ�8s����>1s
		index_count++;
		//handler_xinzeng.removeCallbacks(myrunnable);//�ر��߳�
		return 0;
	}
	//�����ҳ��̵�ַ���ж��Ƿ������ӳɹ�
	public int find_address(){
		if(BluetoothChat.readMessage.contains(ADDRESS))return 1;
		return 0;

	}
	//����ָ��ķ���
	public void send_order(int index_flag){
		switch (index_flag){
		case 1:flag = true;String set_one = "@ZA0A400400190000FFFF0600";//1����Ĭ��
		sendMessageStr(BluetoothChat.hexxor(set_one));break;
		case 2:flag = false;String set_two = "@Z90AF1"+NETWORK+"1A"+ADDRESS+"000002AB";//2��ӻ���
		sendMessageStr(BluetoothChat.hexxor(set_two));break;
		case 3:flag = true;String set_three = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0601";//3����·����
		sendMessageStr(BluetoothChat.hexxor(set_three));break;
		case 4:flag = true;String set_four = "@ZA0A47"+NETWORK+"1A0000"+ADDRESS+"0600";//4����Э����
		sendMessageStr(BluetoothChat.hexxor(set_four));break;
		default:break;
		}
	}
	private  void getAddress(){//�����������κͶ̵�ַ
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
	 * ���ؼ�����д
	 */
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			Intent xinzengtoFormMenuIntent = new Intent(XinZeng.this,
					FormMenu.class);
			startActivity(xinzengtoFormMenuIntent);
			return super.onKeyDown(keyCode, event);
		}

	public void sendMessageStr(String message) {//�����ַ����ķ���
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			bluetoothchatservice.write(send);
		}

	}
}
