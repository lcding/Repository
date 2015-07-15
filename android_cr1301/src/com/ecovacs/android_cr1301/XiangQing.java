package com.ecovacs.android_cr1301;

import java.util.Vector;

import com.ecovacs.android_cr1301.MyTab.MyRunnable;
import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;
import com.ecovacs.uart.ComData;
import com.ecovacs.uart.PollData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class XiangQing extends Activity{
	private boolean xiangqing_flag;

	public BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;
	//利用BluetoothChat的handler发送消息
	public byte[] XiangQing_address;
	private Handler handler = new Handler();
	private BluetoothChat bluetoothchat;
	Vector<TextView> view_list=new Vector<TextView>();
	Vector<String> file_name_list=new Vector<String>();
	ComData com_data = new ComData(BluetoothChat.mChatService);

	private MyRunnable myrunnable= new MyRunnable();
	class MyRunnable implements Runnable{
		@Override
		public void run() {
			//view_list.notifyAll();
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{//蓝牙必须连接
				XiangQing_address = StrToHex(receivedata);
				bluetoothchatservice.write(MyTab.SendMessage("@Z902020202>",XiangQing_address));
				if(BluetoothChat.readMessage != null && !BluetoothChat.readMessage.isEmpty())
				{						 
					com_data.diplayChildUi(BluetoothChat.readMessage, view_list,"");

				}
				if(xiangqing_flag){
					handler.postDelayed(myrunnable, 100);//递归调用
				}
			}
		}		
	}


	//private Button shanchu;
	//private Button xiangqing_shanchu;
	//机器操作动作按钮
	public static String receivedata;//接收前面页面的传值
	//按钮
	private Vector<Button> button_list = new Vector<Button>();
	public  int []r_button_list = {R.id.xiangqing_shanchu,R.id.left,R.id.right,R.id.dingwei,
			R.id.xieqi, R.id.shang, R.id.xia,R.id.zhong,R.id.zuo,R.id.you,R.id.dianjiguanli};
	private Button xiangqing_shanchu,left,right,dingwei,xieqi,shang,xia,zhong,zuo,you,dianjiguanli;
	//textview显示
	private TextView bianhao,yunxing,jiangao,dianchi,ZHUJI,xuliehao,qingxiejiaodu,saomian,shijian,yuji;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dantai_gongzuo_xiangqing);//每一台机器工作明细dantai_gongzuo_xiangqing
		//xiangqing_flag = true;

		/**
		 * 按钮句柄
		 */
		for (int i=0;i<11;i++){
			button_list.add((Button)findViewById(r_button_list[i]));
			button_list.get(i).setOnClickListener(btnClickListener);
		}		
		/**
		 * textview 显示句柄
		 */
		bianhao = (TextView)findViewById(R.id.bianhao);
		Intent intent = this.getIntent();
		Bundle bundlemain = intent.getExtras();
		if (bundlemain != null) {
			// 获取MainActivity传递的值
			receivedata = bundlemain.getString("transdata");
			bianhao.setText(receivedata);
		}
		yunxing = (TextView)findViewById(R.id.yunxing);
		jiangao = (TextView)findViewById(R.id.jiangao);
		dianchi = (TextView)findViewById(R.id.dianchi);
		ZHUJI = (TextView)findViewById(R.id.ZHUJI);
		xuliehao = (TextView)findViewById(R.id.xuliehao);
		qingxiejiaodu = (TextView)findViewById(R.id.qingxiejiaodu);
		saomian = (TextView)findViewById(R.id.saomian);
		shijian = (TextView)findViewById(R.id.shijian);
		yuji = (TextView)findViewById(R.id.yuji);
		//添加textview到list 

		view_list.addElement(yunxing);
		view_list.addElement(jiangao);
		view_list.addElement(dianchi);
		view_list.addElement(ZHUJI);
		view_list.addElement(qingxiejiaodu);

		handler.postDelayed(myrunnable, 500);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		xiangqing_flag = true;
		handler.postDelayed(myrunnable, 300);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		xiangqing_flag = false;
		handler.removeCallbacks(myrunnable);
	}
	static public byte [] StrToHex(String str)
	{
		//System.out.println(str);
		String str_address1 = str.substring(0,2);
		String str_address2 = str.substring(2,4);

		byte []address = new byte [2]; 
		address[0]=Integer.valueOf(str_address1,16).byteValue();	
		address[1]=Integer.valueOf(str_address2,16).byteValue(); 
		return address;
	}
	class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(receivedata==null||receivedata.isEmpty())return;
			byte[] address = StrToHex(receivedata);
			switch (v.getId()) {
			case R.id.shang:				
				bluetoothchatservice.write(MyTab.SendMessage("@Z202020202>", address));
				break;
			case R.id.xia:
				bluetoothchatservice.write(MyTab.SendMessage("@Z202040402>", address));
				break;
			case R.id.zuo:
				bluetoothchatservice.write(MyTab.SendMessage("@Z202080802>", address));
				break;
			case R.id.you:
				bluetoothchatservice.write(MyTab.SendMessage("@Z202202002>", address));
				break;
			case R.id.zhong:
				bluetoothchatservice.write(MyTab.SendMessage("@Z202404002>", address));
				break;
			case R.id.left://向左清洁
				bluetoothchatservice.write(MyTab.SendMessage("@Z202818102>", address));
				break;
			case R.id.right://向右清洁
				bluetoothchatservice.write(MyTab.SendMessage("@Z202828202>", address));
				break;
			case R.id.dingwei://呼叫定位
				break;
			case R.id.xieqi://开启泄气阀---弹出泄气提示框				 
				LayoutInflater factory01 = LayoutInflater.from(XiangQing.this); // 图层模板生成器句柄
				final View DialogxieqiView = factory01.inflate(R.layout.popup_box_jinggao, null); // 用sname.xml模板生成视图模板
				new AlertDialog.Builder(XiangQing.this).setTitle("Tips:")
				.setView(DialogxieqiView) // 设置视图模板
				.setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
				{
					public void onClick(DialogInterface dialog,
							int whichButton) {//泄气
						bluetoothchatservice.write(MyTab.SendMessage("@Z202606002>", StrToHex(receivedata)));
					}
				}).setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						//不泄气

					}
				}).show();
				break;
			case R.id.dianjiguanli:			
				//跳转到电机管理界面
				Intent todianjiIntent = new Intent(XiangQing.this,
						DianjiguanliActivity.class);
				startActivity(todianjiIntent);
				break;

			case R.id.xiangqing_shanchu:
				LayoutInflater factory02 = LayoutInflater.from(XiangQing.this); // 图层模板生成器句柄
				final View DialogshanchuView = factory02.inflate(R.layout.popup_box_shanchu, null); // 用sname.xml模板生成视图模板
				new AlertDialog.Builder(XiangQing.this).setTitle("Tips:")
				.setView(DialogshanchuView) // 设置视图模板
				.setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
				{
					public void onClick(DialogInterface dialog,
							int whichButton) {
						file_name_list = PollData.read("test.txt");
						//删除全部短地址
						PollData.deleteAllFile("test");
						if(file_name_list.size()!=0)
					    //需要限制加载查重复，删除正常
						for(int i= 0;i<file_name_list.size();i++){
							if(file_name_list.get(i).equals(receivedata)){//删除选择的短地址
								//System.out.print(i);System.out.println("<--delete this address");															
								file_name_list.remove(i);							
							}
						}
						for(int  k= 0; k<file_name_list.size(); k++){
							//System.out.println(file_name_list.get(k));
							PollData.save(file_name_list.get(k), "test");//保存剩下的短地址
						}
						Intent toFormMenuIntent = new Intent(XiangQing.this,
								FormMenu.class);
						startActivity(toFormMenuIntent);
					}
				}).setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {						
						 //取消后跳转到详情页面
					}
				}).show(); // 显示对话框
				break;
			default:break;
			}
		}
	}


	/**
	 * 返回键的重写
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//			Intent XiangQingtoFormMenuIntent = new Intent(XiangQing.this,
		//					FormMenu.class);
		//			startActivity(XiangQingtoFormMenuIntent);
		//handler.removeCallbacks(myrunnable);
		return super.onKeyDown(keyCode, event);
	}
}
