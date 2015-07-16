package com.ecovacs.uart;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.ecovacs.android_cr1301.XiangQing;
import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;
import com.ecovacs.formutils.TableAdapter.TableCell;

import android.widget.TextView;

interface DataInterface {
	Vector<String> parseCurrentData(Vector<Integer> vector, String str_num); 
}

public class ComData implements DataInterface{
	private BluetoothChatService bluetoothchatservice;
	private  ComThread com_thread;
	public ComData(BluetoothChatService bluetoothchatservice) {
		this.bluetoothchatservice = bluetoothchatservice;
		com_thread = new ComThread(BluetoothChat.mChatService);
		//com_thread.threadStart();
	}

	//������ǰ��2���ֽڣ�+��

	//��ѹǰ/ ��ѹ��/ ��ѹ����/ǰ��й��������/ǰ��й��������/ǰ�����̹���/ǰ�����̹���  / �������/�����ת/��ձù���/��ձö�ת/��ˢ����/��ˢ��ת/˿�˹���/˿�˶�ת
	final String [] str_array3 = {"ǰ�����̹���","ǰ�����̹���","ǰ��й��������","ǰ��й��������","��ѹ����","��ѹ��","��ѹǰ",""};
	final String [] str_array2 = {"˿�˶�ת","˿�˹���","��ˢ��ת","��ˢ����","��ձö�ת","��ձù���","�����ת","�������"};

	//�������̼�й��������/   �������̼�й��������/  ��ձõ������/  ��ձõ����ת/  �м����̹���/  �м����̹���/  ��ת�������/  ��ת�������
	final String [] str_array1={"��ת�������"," ��ת�������"," �м����̹���","�м����̹���",
			"��ձõ����ת","��ձõ������","�������̼�й��������","�������̼�й��������"};

	enum State{
		MainState,ChildState,MotorState,AlarmState,
	};

	static State RecvMsgState;
	String readMsg = BluetoothChat.readMessage;

	private Vector<String> double_current_list;
	private Vector<String> str_press_list;
	private Vector<String> str_child_list;
	private Vector<String> str_main_list;

	enum LoginMonitor {
		Monitor_Widget,Work_Detail,Page1_WIDGET,Page2_WIDGET,Page3_WIDGET,Page4_WIDGET
	}
	public static LoginMonitor login_monitor;
	public static  enum MotorPage {
		Page1,Page2,Page3,Page4
	};
	static public MotorPage Motor_Page=ComData.MotorPage.Page1;


	/*
	 * �ֽ�ת����
	 */
	Vector<Integer> byte2_To_Hex(String str) {
		Vector<Integer> int_list = new Vector<Integer>();
		String sub_string;

		for (int i=0; i<str.length()/2;i++) {
			sub_string=str.substring(i*2, i*2+2);
			int tmp=Integer.valueOf(sub_string, 16).intValue();
			int_list.add(Integer.valueOf(tmp));
		}
		return int_list;
	}

	//��ȡ��Ч�ַ���34 12 cf @Z1
	private String splitString(String recv_str) {
		String val=null;
		if (!recv_str.contains(">"))return val;
		String [] str_arr = recv_str.split(">");

		int index=0;
		for(int i=0;i<str_arr.length;i++) {
			if (str_arr[i].contains("@Z1")) {
				index=str_arr[i].indexOf("@Z1");
				if (index >= 2) {
					val=str_arr[i];
					break;
				}
			}
		}
		return val;

	}

	
	//34 12 cf *@Z1+���ȣ�1���ֽڣ�+�澯��Ϣ(3�ֽ�)+code(1���ֽ�)++����״̬(1�ֽ�)+����(1�ֽ�)+У��+>
	public void diplayMainUi(String recv_str,Vector<TableCell> cell,String str_num) {
	
	 if (recv_str == null ||recv_str.isEmpty() ) return;
		//System.out.println("before");
		String val = splitString(recv_str);
		String s="";

		if (val == null) return;

		int index=val.indexOf("@Z1");

		if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("01")){
			return;
		}
		String header_val=val.substring(index-2,index);//�̵�ַcf
		String val_tmp = val.substring(index+2+1*2+1,index+2+7*2+1);
		Vector<Integer> int_list=byte2_To_Hex(val_tmp);

		
		//ƥ�����
//		if(MainActivity.matchbyte(header_val, str_num))
//			for(int i = 0; i<5;i++){
//				System.out.println("ƥ��ɹ�*****************");
//			}
		//s = XiangQing.StrToHex(str_num).toString();
		try {
			s = new String(XiangQing.StrToHex(str_num),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//, "UTF-8"�ɼӱ����ʽ
		 
		//System.out.println("before"+int_list);
		if (int_list.get(3).intValue() != 0x01 ||!header_val.equals(s)) return;//||  matchbyte(header_val,str_num)
		int_list.remove(3);
		//add zigbee signal!
		int_list.add(Integer.valueOf(header_val.charAt(1)));//2 ���⣿

		//System.out.println("after"+int_list);
		parseMainUiData(int_list, str_num);
		setMainUiView(cell, str_num);
		
//		System.out.print( System.getProperty("sun.jnu.encoding"));
	}

	//����������
	//34 12 cf @Z1+size+�澯��Ϣ+code+����״̬(03��ɨ������0�ֶ��� 1��ɨ�У�2������)+��ص���+��������(�գ�)+�Ƕ�(2���ֽ�)+jiaoyan+>
	public void diplayChildUi(String recv_str,Vector<TextView> view,String str_num) {
		String val = splitString(recv_str);
		if (val == null ) return;

		int index=val.indexOf("@Z1");
		if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("02")){
			return;
		}
		String header_val=val.substring(index-2,index);//3

		String val_tmp = val.substring(index+2+1*2+1,index+2+9*2+1);
		Vector<Integer> int_list=byte2_To_Hex(val_tmp);

		if (int_list.get(3).intValue() != 0x02) return;
		int_list.remove(3);
		parseChildData(int_list, str_num);
		setChildView(view);
	}

	//splitString">"->byte2_To_Hex->parseCurrentData->setMotorView
	//������������
	//34 12 cf @Z1+size+�澯��Ϣ+code->5+����1+����2+3+4+5+6+У��+>
	//displayMotorUi(recv_str,view,MotorPage.Page1,"");
	public void displayMotorUi(String recv_str, Vector<TextView> view, MotorPage Page, String str_num) {
		String val = splitString(recv_str);
		if (val == null) return;


		int index=val.indexOf("@Z1");

		String header_val=val.substring(index-2,index);//3
		String val_tmp ;//������������
		switch(Page) {
		case Page2://ǰ+��+����
			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("04")){
				return;
			}
			val_tmp =val.substring(index+2+4*2+1,index+2+8*2+1);
			break;
		case Page1://����1+����3+4+2(+3+4) //ǰ��/���/����! ǰ��ֱ��1����+ǰ��й��������+�������+����������
			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("03")){
				return;
			}
			val_tmp = val.substring(index+2+4*2+1,index+2+9*2+1);
			break;
		case Page3://����1+����2+3+4

			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("05")){
				return;
			}
			val_tmp = val.substring(index+2+4*2+1,index+2+9*2+1);
			break;
		case Page4:	//����1+����2         //˿��+��ˢ 
			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("06")){
				return;
			}
			val_tmp = val.substring(index+2+4*2+1,index+2+7*2+1);//from code to end current!
			break;
		default:val_tmp = null;
		break;
		}

		//assign!
		Vector<Integer> int_list=byte2_To_Hex(val_tmp);
		if (int_list.get(0).intValue() == 0x04){ //03,04(press),05,06
			int_list.remove(0);
			parsePressData(int_list, str_num);
			setMotorView(view, Page);
		} else if (int_list.get(0).intValue() == 0x03 ||
				int_list.get(0).intValue() == 0x05 || 
				int_list.get(0).intValue() == 0x06) {
			int_list.remove(0);
			parseCurrentData(int_list, str_num);
			setMotorView(view, Page);
		}

	}

	/*
	 * ������������
	 * @vector:Ҫ��ʾ��int��
	 * @str_num:��Ӧ�ڶ̵�ַ��
	 * return�� Ҫ��ʾ����ֵlist��
	 */
	public  Vector<String> parseCurrentData(Vector<Integer> vector , String str_num) {
		Vector<String> double_list = new Vector<String>();
		for(int i=0; i<vector.size();i++) {
			String tmp_str=String.valueOf(vector.get(i).intValue()/100.00);
			double_list.add(tmp_str);
		}
		this.double_current_list = double_list;
		return double_list;
	}
	/*PressPage
	 * 
	 */
	private Vector<String> parsePressData(Vector<Integer> vector , String str_num) {
		Vector<String> str_list = new Vector<String>();
		for(int i=0; i<vector.size();i++) {
			String tmp_str = String.valueOf(vector.get(i).intValue());
			str_list.add(tmp_str);
		}
		this.str_press_list = str_list;
		return str_list;
	}
	//����hex��Ӧalarm_list!���ر�����
	private Vector<String> parseAlarm(int hex, String [] alarm_list) {
		Vector<String> str_list = new Vector<String>();
		if (hex==0) {
			str_list.add("");
			return str_list;
		} 

		for(int i=0;i<8;i++) {
			if (((hex>>i) & 0x01) == 1){
				str_list.add(alarm_list[i]);
			}
		}
		return str_list;

	}

	//34 12 cf @Z1+size+�澯��Ϣ+code+����״̬(03��ɨ������0�ֶ��� 1��ɨ�У�2������)+��ص���+��������(�գ�)+�Ƕ�(2���ֽ�)
	private Vector<String> parseChildData(Vector<Integer> vector, String str_num) {
		Vector<String> str_list = new Vector<String>();
		String tmp_str; 
		//if (vector.get(3).intValue() !=0x01) return null;

		//0
		switch(vector.get(3).intValue()) {
		case 0x00:  
			str_list.add("�ֶ�");
			break;
		case 0x01:
			str_list.add("��ɨ��");
			break;
		case 0x02:
			str_list.add("������");
			break;
		case 0x03:
			str_list.add("��ɨ����");
			break;
		default: break;
		}

		//1 alarm
		int []hex =new int[]{vector.get(2).intValue(),vector.get(1).intValue(),vector.get(0).intValue()};

		Vector<String>alarm_list = getAlarmString(hex);
		if(vector.get(4).intValue() < 10) {
			alarm_list.add("��ص�������");
		}
		//���alarm 
		String str_alarm=alarm_list.toString();
		str_list.add(str_alarm);
		
		//2 ����
		tmp_str = String.valueOf(vector.get(4).intValue())+"%";
		str_list.add(tmp_str);
		//3 ����
		//		tmp_str = String.valueOf(vector.get(5).intValue()/100);
		//		str_list.add(tmp_str);

		str_list.add("");

		//4 �Ƕ�
		tmp_str = String.valueOf((vector.get(5).intValue()*16*16+vector.get(6).intValue())/100-90);
		str_list.add(tmp_str);
		this.str_child_list=str_list;
		return str_list;
	}


	// *@Z1+����+�澯��Ϣ(3�ֽ�)+code++����״̬(1�ֽ�)+����(1�ֽ�)+У��+> 
	private Vector<String> parseMainUiData(Vector<Integer> vector, String str_num) {
		Vector<String> str_list = new Vector<String>();
		String tmp_str; 
		//code!
		//	if (vector.get(3).intValue() !=0x01) return null;

		//0
		switch(vector.get(3).intValue()) {
		case 0x00:  
			str_list.add("�ֶ�");
			break;
		case 0x01:
			str_list.add("��ɨ��");
			break;
		case 0x02:
			str_list.add("������");
			break;
		case 0x03:
			str_list.add("��ɨ����");
			break;
		default: break;
		}

		//zigbee sigal 1
		if (vector.get(5).intValue()>0){
			str_list.add("zigbee");
		}

		//2
		tmp_str = String.valueOf(vector.get(4).intValue())+"%";
		str_list.add(tmp_str);


		//3 ?
		int []hex =new int[]{vector.get(2).intValue(),vector.get(1).intValue(),vector.get(0).intValue()};

		Vector<String>alarm_list = getAlarmString(hex);

		if(vector.get(4).intValue() < 10) {
			alarm_list.add("��ص�������");
		}
		//���alarm
//		for (int i=0;i<alarm_list.size();i++) {
//			str_list.add(alarm_list.get(i));
//		}
		String str_alarm = alarm_list.toString();
		str_list.add(str_alarm);
		str_main_list = str_list;
		return  str_list;
	}

	//�����еĵ�ص�����

	private Vector<String> getAlarmString(int []hex) {
		Vector<String> alarm_list3=parseAlarm(hex[0],str_array3);
		Vector<String> alarm_list2=parseAlarm(hex[1],str_array2);
		Vector<String> alarm_list1=parseAlarm(hex[2],str_array1);
		Vector<String> alarm_string = new Vector<String>();

		for (int i=0;i<alarm_list3.size();i++) {
			if (alarm_list3.elementAt(i).contains("��")){
				alarm_string.add("��ѹ�쳣");
			}
		}

		for (int i=0;i<alarm_list2.size();i++) {
			if (alarm_list2.elementAt(i).contains("��")){
				alarm_string.add("�������쳣");
			}
		}

		for (int i=0;i<alarm_list1.size();i++) {
			if (alarm_list1.elementAt(i).contains("��")){
				alarm_string.add("ǰ������쳣");
			}
		}
		return alarm_string;

	}
	/*
	 * display 
	 */
	private boolean setMotorView(Vector<TextView> view, MotorPage Page) {
		if (view.size() < 3)
			return false;
		switch(Page) {
		case Page2: 		
			for (int i=0;i<str_press_list.size();i++) {
				view.get(i).setText(str_press_list.get(i));
			}
			break;
		case Page1:
			view.get(0).setText(double_current_list.get(0));
			view.get(1).setText(double_current_list.get(2));
			view.get(2).setText(double_current_list.get(3));
			view.get(3).setText(double_current_list.get(1));
			view.get(4).setText(double_current_list.get(2));
			view.get(5).setText(double_current_list.get(3));

			break;

		case Page3:
			for (int i=0;i<double_current_list.size();i++) {
				view.get(i).setText(double_current_list.get(i));
			}
			break;

		case Page4:	
			view.get(0).setText(double_current_list.get(0));
			view.get(1).setText(double_current_list.get(0));
			view.get(2).setText(double_current_list.get(1));
			view.get(3).setText(double_current_list.get(1));
			break;
		default:break;	
		}

		return true;
	}

	//state+signal+power+alarm!
	private void setMainUiView(Vector<TableCell> cell, String str_num) {
		for (int i=0;i<3;i++) {
			cell.get(i).value = str_main_list.get(i);
		}
		//alarm
		cell.get(3).value=(str_main_list.get(3));
	}

	//state+alarm +power+current+angel!
	private void setChildView(Vector<TextView> view) {

//		view.get(0).setText(str_child_list.get(0));
//		view.get(1).setText(str_child_list.get(1)+str_child_list.get(2)+str_child_list.get(3)+str_child_list.get(4));

		for (int i=0;i<5;i++) {
			view.get(i).setText(str_child_list.get(i));
		}
	}



}
