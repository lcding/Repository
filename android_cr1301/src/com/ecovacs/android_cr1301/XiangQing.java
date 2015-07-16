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
	//����BluetoothChat��handler������Ϣ
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
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{//������������
				XiangQing_address = StrToHex(receivedata);
				bluetoothchatservice.write(MyTab.SendMessage("@Z902020202>",XiangQing_address));
				if(BluetoothChat.readMessage != null && !BluetoothChat.readMessage.isEmpty())
				{						 
					com_data.diplayChildUi(BluetoothChat.readMessage, view_list,"");

				}
				if(xiangqing_flag){
					handler.postDelayed(myrunnable, 100);//�ݹ����
				}
			}
		}		
	}


	//private Button shanchu;
	//private Button xiangqing_shanchu;
	//��������������ť
	public static String receivedata;//����ǰ��ҳ��Ĵ�ֵ
	//��ť
	private Vector<Button> button_list = new Vector<Button>();
	public  int []r_button_list = {R.id.xiangqing_shanchu,R.id.left,R.id.right,R.id.dingwei,
			R.id.xieqi, R.id.shang, R.id.xia,R.id.zhong,R.id.zuo,R.id.you,R.id.dianjiguanli};
	private Button xiangqing_shanchu,left,right,dingwei,xieqi,shang,xia,zhong,zuo,you,dianjiguanli;
	//textview��ʾ
	private TextView bianhao,yunxing,jiangao,dianchi,ZHUJI,xuliehao,qingxiejiaodu,saomian,shijian,yuji;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//��ť

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dantai_gongzuo_xiangqing);//ÿһ̨����������ϸdantai_gongzuo_xiangqing
		//xiangqing_flag = true;

		/**
		 * ��ť���
		 */
		for (int i=0;i<11;i++){
			button_list.add((Button)findViewById(r_button_list[i]));
			button_list.get(i).setOnClickListener(btnClickListener);
		}		
		/**
		 * textview ��ʾ���
		 */
		bianhao = (TextView)findViewById(R.id.bianhao);
		Intent intent = this.getIntent();
		Bundle bundlemain = intent.getExtras();
		if (bundlemain != null) {
			// ��ȡMainActivity���ݵ�ֵ
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
		//���textview��list 

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
			case R.id.left://�������
				bluetoothchatservice.write(MyTab.SendMessage("@Z202818102>", address));
				break;
			case R.id.right://�������
				bluetoothchatservice.write(MyTab.SendMessage("@Z202828202>", address));
				break;
			case R.id.dingwei://���ж�λ
				break;
			case R.id.xieqi://����й����---����й����ʾ��				 
				LayoutInflater factory01 = LayoutInflater.from(XiangQing.this); // ͼ��ģ�����������
				final View DialogxieqiView = factory01.inflate(R.layout.popup_box_jinggao, null); // ��sname.xmlģ��������ͼģ��
				new AlertDialog.Builder(XiangQing.this).setTitle("Tips:")
				.setView(DialogxieqiView) // ������ͼģ��
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() // ȷ��������Ӧ����
				{
					public void onClick(DialogInterface dialog,
							int whichButton) {//й��
						bluetoothchatservice.write(MyTab.SendMessage("@Z202606002>", StrToHex(receivedata)));
					}
				}).setNegativeButton("ȡ��", // ȡ��������Ӧ����,ֱ���˳��Ի������κδ���
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						//��й��

					}
				}).show();
				break;
			case R.id.dianjiguanli:			
				//��ת������������
				Intent todianjiIntent = new Intent(XiangQing.this,
						DianjiguanliActivity.class);
				startActivity(todianjiIntent);
				break;

			case R.id.xiangqing_shanchu:
				LayoutInflater factory02 = LayoutInflater.from(XiangQing.this); // ͼ��ģ�����������
				final View DialogshanchuView = factory02.inflate(R.layout.popup_box_shanchu, null); // ��sname.xmlģ��������ͼģ��
				new AlertDialog.Builder(XiangQing.this).setTitle("Tips:")
				.setView(DialogshanchuView) // ������ͼģ��
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() // ȷ��������Ӧ����
				{
					public void onClick(DialogInterface dialog,
							int whichButton) {
						file_name_list = PollData.read("test.txt");
						//ɾ��ȫ���̵�ַ
						PollData.deleteAllFile("test");
						if(file_name_list.size()!=0)
					    //��Ҫ���Ƽ��ز��ظ���ɾ������
						for(int i= 0;i<file_name_list.size();i++){
							if(file_name_list.get(i).equals(receivedata)){//ɾ��ѡ��Ķ̵�ַ
								//System.out.print(i);System.out.println("<--delete this address");															
								file_name_list.remove(i);							
							}
						}
						for(int  k= 0; k<file_name_list.size(); k++){
							//System.out.println(file_name_list.get(k));
							PollData.save(file_name_list.get(k), "test");//����ʣ�µĶ̵�ַ
						}
						Intent toFormMenuIntent = new Intent(XiangQing.this,
								FormMenu.class);
						startActivity(toFormMenuIntent);
					}
				}).setNegativeButton("ȡ��", // ȡ��������Ӧ����,ֱ���˳��Ի������κδ���
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {						
						 //ȡ������ת������ҳ��
					}
				}).show(); // ��ʾ�Ի���
				break;
			default:break;
			}
		}
	}


	/**
	 * ���ؼ�����д
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
