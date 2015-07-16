package com.ecovacs.uart;

import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;

public class Util{
	//"3412"->0x34 0x12
	static public byte [] StrToHex(String str)
	{
		String str_address1 = str.substring(0,2);
		String str_address2 = str.substring(2,4);
          
		byte []address = new byte [2]; 
		address[0]=Integer.valueOf(str_address1,16).byteValue();  
		address[1]=Integer.valueOf(str_address2,16).byteValue();  
		return address;
	}
	//writeBluetooth
	public static void writeMsg(BluetoothChatService bluetoothchatservice,String str) {
		byte []address =  StrToHex(BluetoothChat.Shortaddress);
		String messageshang = str;
		//String message = "@Z902010102>";		
		if (bluetoothchatservice== null) return;
		bluetoothchatservice.write(address);
		byte[] sendshang = messageshang.getBytes();
		bluetoothchatservice.write(sendshang);
	}
}