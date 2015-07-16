package com.ecovacs.android_cr1301;

//import android.app.TabActivity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TabHost;
//import android.widget.TabHost.OnTabChangeListener;
import java.util.Vector;

import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;
import com.ecovacs.uart.ComData;

import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
public class MyTab extends TabActivity implements OnTabChangeListener {
	public byte []MyTab_address;
	private boolean isRunning;
	private int flag ;
	private BluetoothChat bluetoothchat;
	private TabHost myTabhost;
	private Button first_key,second_key,third_key,fourth_key;//停止按钮
	private Button zhidian01btn,zhidian02btn,zhidian03btn,xieqi01btn,xieqi02btn,xieqi03btn,//直线电机
	qianxipanbtn,zhuxipanbtn,fuxibtn,//吸盘
	siganqianjinbtn,siganhoutuibtn,xuanzhuanleftbtn,xuanzhuanrightbtn,//丝杆与直线电机
	qianqibengbtn,houqibengbtn,fengjibtn,gunshuabtn;//前后气泵

	private TextView zhidian01,zhidian02,zhidian03,xieqi01,xieqi02,xieqi03,//直线电机
	qianxipan,zhuxipan,fuxi,//吸盘
	siganqianjin,siganhoutui,xuanzhuanleft,xuanzhuanright,//丝杆与直线电机
	qianqibeng,houqibeng,fengji,gunshua;//前后气泵

	private ButtonClickListener btnClickListener = new ButtonClickListener();
	protected int myMenuSettingTag=0;
	protected Menu myMenu;
	private static final int myMenuResources[] = { R.menu.a_menu,
		R.menu.b_menu, R.menu.c_menu,R.menu.d_menu};
	//蓝牙
	public BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;

	private Handler handler = new Handler();
	Vector<TextView> view_list=new Vector<TextView>();
	ComData com_data = new ComData(BluetoothChat.mChatService);

	private MyRunnable myrunnable= new MyRunnable();
	class MyRunnable implements Runnable{

		@Override
		public void run() {
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
				MyTab_address = XiangQing.StrToHex(XiangQing.receivedata);
				if(flag == 0){
					bluetoothchatservice.write(MyTab.SendMessage("@Z902030302>",MyTab_address));
				}else if(flag == 1){
					bluetoothchatservice.write(MyTab.SendMessage("@Z902040402>",MyTab_address));
				}else if(flag == 2){
					bluetoothchatservice.write(MyTab.SendMessage("@Z902050502>",MyTab_address));
				}else if(flag == 3){
					bluetoothchatservice.write(MyTab.SendMessage("@Z902060602>",MyTab_address));
				}else{
					System.out.println("are you sb");
				}



				if(BluetoothChat.readMessage != null && !BluetoothChat.readMessage.isEmpty())
				{						 
					com_data.displayMotorUi(BluetoothChat.readMessage, view_list,ComData.Motor_Page, "");

				}
				if (isRunning) {
					handler.postDelayed(myrunnable, 100);//递归调用
				}
			}

		}		
	}

	//直线电机检测
	private Vector<Button> button_zhidian_list = new Vector<Button>();
	public  int []r_zhidian_list = {R.id.zhidian01btn,R.id.zhidian02btn,R.id.zhidian03btn,
			R.id.xieqi01btn,R.id.xieqi02btn, R.id.xieqi03btn, R.id.first_key};
	//吸盘真空度检测
	private Vector<Button> button_xipan_list = new Vector<Button>();
	public  int []r_xipan_list = {R.id.qianxipanbtn,R.id.zhuxipanbtn,R.id.fuxibtn,R.id.second_key};
	//丝杆与旋转电机检测
	private Vector<Button> button_sigan_list = new Vector<Button>();
	public  int []r_sigan_list = {R.id.siganqianjinbtn,R.id.siganhoutuibtn,R.id.xuanzhuanleftbtn,R.id.xuanzhuanrightbtn,R.id.third_key};
	//前后气泵检测
	private Vector<Button> button_qibeng_list = new Vector<Button>();
	public  int []r_qibeng_list = {R.id.qianqibengbtn,R.id.houqibengbtn,R.id.fengjibtn,R.id.gunshuabtn,R.id.fourth_key};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		myTabhost=this.getTabHost();


		//get Tabhost
		LayoutInflater.from(this).inflate(R.layout.tab_main, myTabhost.getTabContentView(), true);
		myTabhost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		//直线电机页面
		for (int i1=0;i1<7;i1++){
			button_zhidian_list.add((Button)findViewById(r_zhidian_list[i1]));
			button_zhidian_list.get(i1).setOnClickListener(btnClickListener);
		}
		zhidian01 = (TextView)findViewById(R.id.zhidian01);
		zhidian02 = (TextView)findViewById(R.id.zhidian02);
		zhidian03 = (TextView)findViewById(R.id.zhidian03);
		xieqi01 = (TextView)findViewById(R.id.xieqi01);
		xieqi02 = (TextView)findViewById(R.id.xieqi02);
		xieqi03 = (TextView)findViewById(R.id.xieqi03);
		//吸盘真空度页面
		for (int i2=0;i2<4;i2++){
			button_xipan_list.add((Button)findViewById(r_xipan_list[i2]));
			button_xipan_list.get(i2).setOnClickListener(btnClickListener);
		}
		qianxipan = (TextView)findViewById(R.id.qianxipan);
		zhuxipan = (TextView)findViewById(R.id.zhuxipan);
		fuxi = (TextView)findViewById(R.id.fuxi);
		//丝杆与旋转电机检测
		for (int i3=0;i3<5;i3++){
			button_sigan_list.add((Button)findViewById(r_sigan_list[i3]));
			button_sigan_list.get(i3).setOnClickListener(btnClickListener);
		}
		siganqianjin = (TextView)findViewById(R.id.siganqianjin);
		siganhoutui = (TextView)findViewById(R.id.siganhoutui);
		xuanzhuanleft = (TextView)findViewById(R.id.xuanzhuanleft);
		xuanzhuanright = (TextView)findViewById(R.id.xuanzhuanright);
		//前后气泵检测
		for (int i4=0;i4<5;i4++){
			button_qibeng_list.add((Button)findViewById(r_qibeng_list[i4]));
			button_qibeng_list.get(i4).setOnClickListener(btnClickListener);
		}		
		qianqibeng = (TextView)findViewById(R.id.qianqibeng);
		houqibeng = (TextView)findViewById(R.id.houqibeng);
		fengji = (TextView)findViewById(R.id.fengji);
		gunshua = (TextView)findViewById(R.id.gunshua);		
		/**
		 * 对应页面
		 */
		myTabhost
		.addTab(myTabhost.newTabSpec("One")// make a new Tab
				.setIndicator("直线电机",null)
				// set the Title and Icon
				.setContent(R.id.jiance_zhixian_dianji));
		// set the layout


		myTabhost
		.addTab(myTabhost.newTabSpec("Two")// make a new Tab
				.setIndicator("真空度",null)
				// set the Title and Icon
				.setContent(R.id.jiance_xipan_air));
		// set the layout

		myTabhost
		.addTab(myTabhost.newTabSpec("Three")// make a new Tab
				.setIndicator("前后气泵",null)
				// set the Title and Icon
				.setContent(R.id.jiance_qianhou_qibeng));
		// set the layout

		myTabhost
		.addTab(myTabhost.newTabSpec("Four")// make a new Tab
				.setIndicator("丝杆与旋转电机",null)
				// set the Title and Icon
				.setContent(R.id.jiance_sigan_xuanzhuan));

		myTabhost.setOnTabChangedListener(this);


	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isRunning = true;
		handler.postDelayed(myrunnable, 500);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isRunning = false;
		handler.removeCallbacks(myrunnable);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Hold on to this		
		myMenu = menu;
		myMenu.clear();
		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();    
		switch (myMenuSettingTag) {
		case 1:
			inflater.inflate(myMenuResources[0], menu);
			view_list.clear();
			//zhidian01,zhidian02,zhidian03,xieqi01,xieqi02,xieqi03,//直线电机
			view_list.add(zhidian01);
			view_list.add(zhidian02);
			view_list.add(zhidian03);
			view_list.add(xieqi01);
			view_list.add(xieqi02);
			view_list.add(xieqi03);	
			ComData.Motor_Page = ComData.MotorPage.Page1;
			flag = 0;
			break;
		case 2:
			inflater.inflate(myMenuResources[1], menu);			
			view_list.clear();
			//qianxipan,zhuxipan,fuxi,//吸盘
			view_list.add(zhuxipan);
			view_list.add(qianxipan);
			view_list.add(fuxi);
			ComData.Motor_Page = ComData.MotorPage.Page2;
			flag = 1;
			break;
		case 3:
			inflater.inflate(myMenuResources[2], menu);
			view_list.clear();
			//qianqibeng,houqibeng,fengji,gunshua;//前后气泵
			view_list.add(qianqibeng);
			view_list.add(houqibeng);
			view_list.add(fengji);
			view_list.add(gunshua);
			ComData.Motor_Page = ComData.MotorPage.Page3;
			flag = 2;
			break;

		case 4:
			inflater.inflate(myMenuResources[3], menu);
			view_list.clear();
			//siganqianjin,siganhoutui,xuanzhuanleft,xuanzhuanright,//丝杆与直线电机
			view_list.add(siganqianjin);
			view_list.add(siganhoutui);
			view_list.add(xuanzhuanleft);
			view_list.add(xuanzhuanright);
			ComData.Motor_Page = ComData.MotorPage.Page4;
			flag = 3;
			break;	
		default:
			inflater.inflate(myMenuResources[0], menu);
			view_list.clear();
			//zhidian01,zhidian02,zhidian03,xieqi01,xieqi02,xieqi03,//直线电机
			view_list.add(zhidian01);
			view_list.add(zhidian02);
			view_list.add(zhidian03);
			view_list.add(xieqi01);
			view_list.add(xieqi02);
			view_list.add(xieqi03);	
			flag = 0;
			break;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onTabChanged(String tagString) {
		// TODO Auto-generated method stub
		if (tagString.equals("One")) {
			myMenuSettingTag = 1;
		}
		if (tagString.equals("Two")) {
			myMenuSettingTag = 2;
		}
		if (tagString.equals("Three")) {
			myMenuSettingTag = 3;
		}
		if (tagString.equals("Four")) {
			myMenuSettingTag = 4;
		}
		if (myMenu != null) {
			onCreateOptionsMenu(myMenu);
		}
	}

	public   class ButtonClickListener implements OnClickListener{



		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			MyTab_address = XiangQing.StrToHex(XiangQing.receivedata);
			switch (v.getId()){
			//直线电机
			case R.id.zhidian01btn:
				judgeTest(button_zhidian_list,0,"@Z202B6B602>","@Z202C6C602>");
				//bluetoothchatservice.write(SendMessage("@Z202B6B602>",address));
				break;
			case R.id.zhidian02btn:
				judgeTest(button_zhidian_list,1,"@Z202B9B902>","@Z202C9C902>");	 
				//	bluetoothchatservice.write(SendMessage("@Z202B9B902>",address));
				break;
			case R.id.zhidian03btn:
				judgeTest(button_zhidian_list,2,"@Z202BBBB02>","@Z202CBCB02>");
				//bluetoothchatservice.write(SendMessage("@Z202BBBB02>",address));
				break;
			case R.id.xieqi01btn:
				judgeTest(button_zhidian_list,3,"@Z202B7B702>","@Z202C7C702>");
				//bluetoothchatservice.write(SendMessage("@Z202B7B702>",address));
				break;
			case R.id.xieqi02btn:
				judgeTest(button_zhidian_list,4,"@Z202B7B702>","@Z202CACA02>");
				//bluetoothchatservice.write(SendMessage("@Z202BABA02>",address));				
				break;				
			case R.id.xieqi03btn:
				judgeTest(button_zhidian_list,5,"@Z202BCBC02>","@Z202CCCC02>");
				//bluetoothchatservice.write(SendMessage("@Z202BCBC02>",address));			
				break;	
			case R.id.first_key:
				onKeyStop(button_zhidian_list);
				break;
				//吸盘
			case R.id.qianxipanbtn:
				judgeTest(button_xipan_list,0,"@Z202BDBD02>","@Z202CDCD02>");
				//bluetoothchatservice.write(SendMessage("@Z202BDBD02>",address));
				break;
			case R.id.zhuxipanbtn:
				judgeTest(button_xipan_list,1,"@Z202BEBE02>","@Z202CECE02>");
				//bluetoothchatservice.write(SendMessage("@Z202BEBE02>",address));
				break;
			case R.id.fuxibtn:
				judgeTest(button_xipan_list,2,"@Z202BFBF02>","@Z202CFCF02>");
				//bluetoothchatservice.write(SendMessage("@Z202BFBF02>",address));
				break;
			case R.id.second_key:
				onKeyStop(button_xipan_list);
				break;
				//丝杆与旋转电机	
			case R.id.siganqianjinbtn:
				judgeTest(button_sigan_list,0,"@Z202B1B102>","@Z202B0B002>");
				//bluetoothchatservice.write(SendMessage("@Z202B1B102>",address));
				break;
			case R.id.siganhoutuibtn:
				judgeTest(button_sigan_list,1,"@Z202B2B202>","@Z202B0B002>");
				//bluetoothchatservice.write(SendMessage("@Z202B2B202>",address));
				break;
			case R.id.xuanzhuanleftbtn:
				judgeTest(button_sigan_list,2,"@Z202C1C102>","@Z202C0C002>");
				//bluetoothchatservice.write(SendMessage("@Z202C2C202>",address));
				break;
			case R.id.xuanzhuanrightbtn:
				judgeTest(button_sigan_list,3,"@Z202C2C202>","@Z202C0C002>");
				//bluetoothchatservice.write(SendMessage("@Z202C1C102>",address));
				break;
			case R.id.third_key:
				onKeyStop(button_sigan_list);
				break;				
				//前后气泵
			case R.id.qianqibengbtn:
				judgeTest(button_qibeng_list,0,"@Z202B5B502>","@Z202C5C502>");
				//bluetoothchatservice.write(SendMessage("@Z202B5B502>",address));
				break;
			case R.id.houqibengbtn:
				judgeTest(button_qibeng_list,1,"@Z202B8B802>","@Z202C8C802>");
				//bluetoothchatservice.write(SendMessage("@Z202B8B802>",address));
				break;
			case R.id.fengjibtn:
				judgeTest(button_qibeng_list,2,"@Z202B4B402>","@Z202C4C402>");
				//bluetoothchatservice.write(SendMessage("@Z202B4B402>",address));
				break;
			case R.id.gunshuabtn:
				judgeTest(button_qibeng_list,3,"@Z202B3B302>","@Z202C3C302>");
				//bluetoothchatservice.write(SendMessage("@Z202B3B302>",address));
				break;
			case R.id.fourth_key:
				onKeyStop(button_qibeng_list);
				break;	
			default:break;				
			}
		}
		//一键停止，整体遍历setText("检测");
		private void onKeyStop(Vector<Button> vector) {

			for (int i=0;i<vector.size()-1 ;i++) {
				//System.out.println(vector.size()); 
				vector.get(i).setText("检测");
			}
			bluetoothchatservice.write(SendMessage("@Z202AAAA02>", MyTab_address));
		}
		//判断按钮的检测还是停止
		private void judgeTest(Vector<Button> vector,int num, String str_start, String str_stop){

			//			if(bluetoothchat.Shortaddress=="1234"){
			//				address = XiangQing.StrToHex(XinZeng.ADDRESS);
			//			}
			if (vector.get(num).getText().equals("检测")) {
				vector.get(num).setText("停止");
				bluetoothchatservice.write(SendMessage(str_start,MyTab_address));
			} else {
				vector.get(num).setText("检测");
				bluetoothchatservice.write(SendMessage(str_stop,MyTab_address));
			}
		}

	}
	/**
	 * 蓝牙发送指令方法
	 */
	public static byte[] SendMessage(String str,byte[] address_array){

		/**
		 *对应检测页面的检测指令list
		 *vector.addElement(byte[]);
		 *vector.size();
		 *vecotr.removeElementAt(0);
		 */
		byte[] send0 = str.getBytes();

		byte[] send = new byte[14];//指令加短地址的长度

		send[0]=address_array[0];
		send[1]=address_array[1];

		for(int i=0;i<14-2;i++){
			send[i+2] = send0[i];
		}
		//System.out.println(send);
		return send;		
	}



	/**
	 * 返回键的重写
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		//		Intent FormMenutoMainActivityIntent = new Intent(FormMenu.this,
		//				MainActivity.class);
		//		startActivity(FormMenutoMainActivityIntent);		
		return super.onKeyDown(keyCode, event);
	}		
}
