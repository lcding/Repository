package com.ecovacs.uart;

import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;

public class Config_ZigBee{
	public static boolean flag = false ;
	private BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;

	public static  void config_parameter(String address,String network){
		String set_one = "@ZA0A400400190000FFFF0600";//1����Ĭ��
		config_one(BluetoothChat.hexxor(set_one));
//		String set_two = "@Z90AF1"+network+"1A"+address+"000002AB";//2��ӻ���
//		set_two = BluetoothChat.hexxor(set_two);
//		String set_three = "@ZA0A47"+network+"1A0000"+address+"0601";//3����·����
//		set_three = BluetoothChat.hexxor(set_three);
//		String set_four = "@ZA0A47"+network+"1A0000"+address+"0600";//4����Э����
//		set_four = BluetoothChat.hexxor(set_four);
	}
	private static void config_one(String step_one){//��һ������Ĭ��
		if(step_one==null || step_one.isEmpty()) return;//Ҫ���͵�����Ϊ���򷵻�
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

	private void config_two(String step_two){//��ӻ��������
		if(step_two==null || step_two.isEmpty()) return;//Ҫ���͵�����Ϊ���򷵻�
		flag = true ;

	}

	private void config_three(String step_three){//���ó�·����
		if(step_three==null || step_three.isEmpty()) return;//Ҫ���͵�����Ϊ���򷵻�
		flag = true ;

	}

	private void config_four(String step_four){//���ó�Э����
		if(step_four==null || step_four.isEmpty()) return;//Ҫ���͵�����Ϊ���򷵻�
		flag = true ;

	}
	private void config_success(){//���óɹ���ʱ�����
		flag = false ;
		
	}
	private void config_fail(){//����ʧ�ܵ�ʱ�����
		flag = true ;
		
	}
	
	
	
}
