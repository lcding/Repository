package com.ecovacs.android_cr1301;


import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{

	public BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;
	
	//private Handler handler = new Handler();
	private Button jiankong;    //监控页面
	private Button xitongshezhi;//系统设置页面
	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shouye_activity_main);//设置画面为主画面 shouye_activity_main.xml

		jiankong = (Button)findViewById(R.id.jiankong);//监控按钮句柄
		jiankong.setOnClickListener(btnClickListener);
		xitongshezhi = (Button)findViewById(R.id.xitongshezhi);//系统设置按钮句柄
		xitongshezhi.setOnClickListener(btnClickListener);
		
//		int i,j;
//		i = Integer.valueOf("AA",16);
//		j = Integer.valueOf("CC",16);
		
//		System.out.println(i==j);
//		System.out.println(i);
//		System.out.println(j);
		
//		System.out.println(a);
		
		
	}

	
	//短地址匹配
	public static boolean matchbyte(String str1, String str2){//str1
		
		return false;	
	}
	/**
	 * 实现按钮功能
	 */
	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {			
			case R.id.jiankong:
				/**
				 * 此处发送@Z902010102>
				 * 为了方便下一页面formactivity接收数据
				 */

//				String message = "main activity need to send";
//				byte[] send = message.getBytes();
//				bluetoothchatservice.write(send);
//				Log.e("print", message);
				
				Intent toFormMenuIntent = new Intent(MainActivity.this,
						FormMenu.class);
				startActivity(toFormMenuIntent);
				break;
			case R.id.xitongshezhi:
				Intent toshezhiIntent = new Intent(MainActivity.this,
						XiTong.class);
				//				Intent toshezhiIntent = new Intent(MainActivity.this,
				//						XiTong.class);
				startActivity(toshezhiIntent);
				break;
			default:break;
			}
		}
	}
	
//	/**
//	 * 返回键的重写
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		Intent toBluetoothChatIntent = new Intent(MainActivity.this,
//				BluetoothChat.class);
//		startActivity(toBluetoothChatIntent);
//		//		if (keyCode == KeyEvent.KEYCODE_BACK) {  
//		//			moveTaskToBack(true);
//		//			return true;
//		//		} 
//		return super.onKeyDown(keyCode, event);
//	}
}	
