package com.ecovacs.btutils;


import java.io.UnsupportedEncodingException;

import com.ecovacs.android_cr1301.MainActivity;
import com.ecovacs.android_cr1301.R;
import com.ecovacs.uart.PollData;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
	public static String Shortaddress,Network;
	// Debugging
	public static String readMessage;
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// Layout Views
	private ListView mConversationView;
	private EditText mOutEditText;
	private static EditText wangduan;
	private static EditText duandizhi;


	private Button mSendButton;
	private Button setting,button_config03,config,add;

	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	public static BluetoothChatService mChatService = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//if(D) Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		setContentView(R.layout.bt_chat_activity);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

	}

	/**
	 * 确定是否打开蓝牙
	 */
	@Override
	public void onStart() {
		super.onStart();
		//if(D) Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null) setupChat();
		}
	}

	/**
	 * 启动蓝牙service****************************************************************************************
	 */
	@Override
	public synchronized void onResume() {
		super.onResume();
		//if(D) Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		//Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.bt_message);
		mConversationView = (ListView) findViewById(R.id.in);
		mConversationView.setAdapter(mConversationArrayAdapter);

		wangduan = (EditText) findViewById(R.id.wangduan);
		duandizhi = (EditText) findViewById(R.id.duandizhi);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(btnClickListener);
		config = (Button)findViewById(R.id.button_config);
		config.setOnClickListener(btnClickListener);
		button_config03 = (Button)findViewById(R.id.button_config03);
		button_config03.setOnClickListener(btnClickListener);
		setting = (Button)findViewById(R.id.button_setting);
		setting.setOnClickListener(btnClickListener);
		add = (Button)findViewById(R.id.button_xinzeng);
		add.setOnClickListener(btnClickListener);
		//        mSendButton.setOnClickListener(new OnClickListener() {
		//            public void onClick(View v) {
		//                // Send a message using content of the edit text widget
		//                TextView view = (TextView) findViewById(R.id.edit_text_out);
		//                String message = view.getText().toString();
		//                sendMessage(message);
		//                /**
		//                 * 连接完成后切换到主界面
		//                 */
		//        		Intent mainintent = new Intent(this,MainActivity.class);
		//                startService(mainintent);
		//                
		//                System.out.println("BluetoothChat--to--start MainActivity");
		//            }
		//        });

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}


	/**
	 * 判断短地址和信道输入的方法
	 */
	public static boolean checkNum(String a)
	{   a = a.replace('a','A');
		a = a.replace('b','B');
		a = a.replace('c','C');
		a = a.replace('d','D');
		a = a.replace('e','E');
		a = a.replace('f','F');
	boolean flag = false;
	if(a.length()!=4){
		flag = false;
	}else if(a.equals("ffff")){
		flag = false;
	}
	
	for (int i = 0; i < a.length(); i++) 
	{
		//与'0'和'9'比较，不是0,9.
		if(a.charAt(i)>='0'&&a.charAt(i)<='9'||a.charAt(i)>='a'&&a.charAt(i)<='f'||a.charAt(i)>='A'&&a.charAt(i)<='F')
		{
			flag = true;
		}else{

			flag = false;
		}
	}
			
	return flag;
	}

	public static String captital(String str){
		
		str = str.replace('a', 'A');
		str = str.replace('b', 'B');
		str = str.replace('c', 'C');
		str = str.replace('d', 'D');
		str = str.replace('e', 'E');
		str = str.replace('f', 'F');
		//System.out.println(str);
		return str;		
	}
	public static String hexxor(String str){
		String xor="";
		char a[]=str.toCharArray();
		int c=0,b=0;
		b=a.length;
		for(int i=3;i<b;i++){
			c ^= a[i];
		}
		//replace方法并不能改变字符串本身的，只是replace方法能返回一个处理后的字符串而已
		xor = Long.toHexString(c);
		xor = xor.replace('a', 'A');
		xor = xor.replace('b', 'B');
		xor = xor.replace('c', 'C');
		xor = xor.replace('d', 'D');
		xor = xor.replace('e', 'E');
		xor = xor.replace('f', 'F');
		//xor.valueOf(c);
		if(xor.length()<2){
			xor="0"+xor;
		}
		//System.out.println(xor+">");
		return str + xor + ">";
	}
	/**
	 * 发送按钮
	 */
	class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			/**
			 * 这里只要按下按钮，把编辑框中的数据更新到shortaddress和network里面
			 */
			switch(v.getId()){
			case R.id.button_send:
				getAddress();
				if(checkNum(Network)&&checkNum(Shortaddress)){
					TextView view = (TextView) findViewById(R.id.edit_text_out);
					String message = view.getText().toString();
					sendMessage(message);
					//sendMessage("you must connect bluetooth");
					/**
					 * 连接完成后切换到主界面
					 */
					if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
//						String config = "@ZA0A47"+1111+"1A0000"+1111+"0601";//中继器配置路由器					
//						sendMessage(hexxor(config));
						if(!Shortaddress.equals("1111"))
						    PollData.save(Shortaddress, "test");//编号+"*"+网段 +"*"+短地址
						Intent mainintent = new Intent(BluetoothChat.this,MainActivity.class);
					    startActivity(mainintent);
					}
				}else{
					Toast.makeText(getApplicationContext(), "Network Segment and Short Address must be 0~FFFE"
							, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.button_config03:
				getAddress();
				if(checkNum(Network)&&checkNum(Shortaddress)){	
					if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
						String config = "@ZA0A47"+Network+"1A0000"+Shortaddress+"0601";//中继器配置路由器					
						sendMessage(hexxor(config));
					}	
				}else{
					Toast.makeText(getApplicationContext(), "Network Segment and Short Address must be 0~FFFE"
							, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.button_config:
				getAddress();
				if(checkNum(Network)&&checkNum(Shortaddress)){	
					if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
						String config = "@ZA0A47"+Network+"1A0000"+Shortaddress+"0600";//中继器配置协调器					
						sendMessage(hexxor(config));
					}	
				}else{
					Toast.makeText(getApplicationContext(), "Network Segment and Short Address must be 0~FFFE"
							, Toast.LENGTH_SHORT).show();
				}

				break;

			case R.id.button_setting:
				if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
					String set= "@ZA0A400400190000FFFF0600";//默认适配器
			        sendMessage(hexxor(set));		
				}
				break;
			case R.id.button_xinzeng:
				getAddress();
				if(checkNum(Network)&&checkNum(Shortaddress)){
					if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
						String add = "@Z90AF1"+Network+"1A"+Shortaddress+"000002AB";//新增机器配置的					
						sendMessage(hexxor(add));
					}
				}else{
					Toast.makeText(getApplicationContext(), "Network Segment and Short Address must be 0~FFFE"
							, Toast.LENGTH_SHORT).show();
				}

				break;
			default:break;
			}
		}
	}


	private static void getAddress(){//获得输入的网段和短地址
		Network = wangduan.getText().toString();
		Network = captital(Network);
		if(Network.length()==4){
			Network = Network.substring(2, 4)+Network.substring(0, 2);
		}else{
			Network = "1111";
		}
		
		Shortaddress = duandizhi.getText().toString();
		Shortaddress = captital(Shortaddress);
		if(Shortaddress.length()==4){
			Shortaddress = Shortaddress.substring(2, 4)+Shortaddress.substring(0, 2);
		}else{
			Shortaddress = "1111";
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		//if(D) Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		//if(D) Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null) mChatService.stop();
		//if(D) Log.e(TAG, "--- ON DESTROY ---");
	}

	/**
	 * 设置蓝牙可见
	 */
	private void ensureDiscoverable() {
		//if(D) Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() !=
				BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * 蓝牙发送消息
	 * Sends a message.
	 * @param message  A string of text to send.
	 */
	public void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);//"@Z1020101>"

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener =
			new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// If the action is a key-up event on the return key, send the message
			if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			//if(D) Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	private final void setStatus(int resId) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subTitle);
	}


	/**
	 *广播行不通
	 *需要学习handler，在线程中发送信息，以便更新UI界面
	 *************************************************************************************
	 */
	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) { 
			case MESSAGE_STATE_CHANGE:
				//if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				
				case BluetoothChatService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
					mConversationArrayAdapter.clear();
					break;	
				case BluetoothChatService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;	
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
				/**********************自己发送信息显示list*****************************/
			case MESSAGE_WRITE://写入buff
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				//String writeMessage = writeBuf.toString();
				String writeMessage = null;
				try {
					writeMessage = new String(writeBuf,"ISO-8859-1");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mConversationArrayAdapter.add("Me:" + writeMessage);
				if(D) Log.e("write", writeMessage);
				break;
				/*********************显示蓝牙接受信息list*****************************/
			case MESSAGE_READ://从buff读出
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				try {
					readMessage = new String(readBuf,0, msg.arg1,"ISO-8859-1");
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//此处添加final
				int b=msg.arg1;
				mConversationArrayAdapter.add(mConnectedDeviceName+":" +readMessage);
				if(D) Log.e("read", readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(), "Connected to "
						+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
				/**
				 * 这里添加切换，自动切换页面formactivity
				 */
						
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occurred
				//Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras()
				.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		//这里连接的时候要传参数（自己写的connect方法）
		//mChatService.connect(device, secure);
		mChatService.connect(device, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}


	/**
	 * 返回键的重写
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			moveTaskToBack(true);  
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}

}
