package com.ecovacs.uart;

import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;

public class Config_ZigBee{
	public static boolean flag = false ;
	private BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;

	public static  void config_parameter(String address,String network){
		String set_one = "@ZA0A400400190000FFFF0600";//1配置默认
		config_one(BluetoothChat.hexxor(set_one));
//		String set_two = "@Z90AF1"+network+"1A"+address+"000002AB";//2添加机器
//		set_two = BluetoothChat.hexxor(set_two);
//		String set_three = "@ZA0A47"+network+"1A0000"+address+"0601";//3配置路由器
//		set_three = BluetoothChat.hexxor(set_three);
//		String set_four = "@ZA0A47"+network+"1A0000"+address+"0600";//4配置协调器
//		set_four = BluetoothChat.hexxor(set_four);
	}
	private static void config_one(String step_one){//第一步配置默认
		if(step_one==null || step_one.isEmpty()) return;//要发送的数据为空则返回
		flag = true ;	
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String s = BluetoothChat.readMessage;
		System.out.println(s);
	}

	private void config_two(String step_two){//添加机器到表格
		if(step_two==null || step_two.isEmpty()) return;//要发送的数据为空则返回
		flag = true ;

	}

	private void config_three(String step_three){//配置成路由器
		if(step_three==null || step_three.isEmpty()) return;//要发送的数据为空则返回
		flag = true ;

	}

	private void config_four(String step_four){//配置成协调器
		if(step_four==null || step_four.isEmpty()) return;//要发送的数据为空则返回
		flag = true ;

	}
	private void config_success(){//配置成功的时候调用
		flag = false ;
		
	}
	private void config_fail(){//配置失败的时候调用
		flag = true ;
		
	}
	
	
	
}
