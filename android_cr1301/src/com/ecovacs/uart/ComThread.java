package com.ecovacs.uart;

import com.ecovacs.uart.ComData;
import com.ecovacs.btutils.BluetoothChatService;

public class ComThread extends Thread {
	public ComThread(BluetoothChatService bluetoothchatservice) {
		// TODO Auto-generated constructor stub
		this.bluetoothchatservice = bluetoothchatservice;
	}


	final String[] str_cmd = {"@Z02010102>","@Z02020202>","@Z02030302>",
			"@Z02040402>","@Z02050502>","@Z02060602>","@Z02070702>"};

	boolean isRunning = true;
	private BluetoothChatService bluetoothchatservice;
	public void threadStart() {
		isRunning = true;
		this.start();
	}
	public void threadStop() throws InterruptedException {
		isRunning = false;
		this.stop();
		wait();
	}
	public void run() {
		//0x0100 0200 0300
		super.run(); 

		try {
			while (isRunning){
				this.currentThread().sleep(1000);
				//state msg!!
				switch (ComData.login_monitor) {
				case Monitor_Widget:
					Util.writeMsg(bluetoothchatservice,str_cmd[0]);

					break;
				case Work_Detail:
					//zigbee_config_data->sendRemoteActionDate();
					Util.writeMsg(bluetoothchatservice,str_cmd[1]);
					//   readData();
					break;
				case Page1_WIDGET://setting 2
					//  zigbee_config_data->sendRemotePressDate();
					Util.writeMsg(bluetoothchatservice,str_cmd[2]);

					//  readData();
					break;
					//settings widget!
					//child monitor 2015@0204
				case Page2_WIDGET:
					//  zigbee_config_data->sendRemoteSetting1_Date();
					Util.writeMsg(bluetoothchatservice,str_cmd[3]);
				case Page3_WIDGET:
					//  zigbee_config_data->sendRemoteSetting1_Date();
					Util.writeMsg(bluetoothchatservice,str_cmd[4]);	
				case Page4_WIDGET:
					//  zigbee_config_data->sendRemoteSetting1_Date();
					Util.writeMsg(bluetoothchatservice,str_cmd[5]);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

