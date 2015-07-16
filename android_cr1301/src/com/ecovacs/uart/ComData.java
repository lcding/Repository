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

	//报警！前（2个字节）+后

	//气压前/ 气压后/ 气压辅助/前机泄气阀过流/前机泄气阀故障/前机吸盘过流/前机吸盘故障  / 风机过流/风机堵转/真空泵过流/真空泵堵转/滚刷过流/滚刷堵转/丝杆过流/丝杆堵转
	final String [] str_array3 = {"前机吸盘故障","前机吸盘过流","前机泄气阀故障","前机泄气阀过流","气压辅助","气压后","气压前",""};
	final String [] str_array2 = {"丝杆堵转","丝杆过流","滚刷堵转","滚刷过流","真空泵堵转","真空泵过流","风机堵转","风机过流"};

	//辅助吸盘及泄气阀过流/   辅助吸盘及泄气阀故障/  真空泵电机过流/  真空泵电机堵转/  中间吸盘过流/  中间吸盘故障/  旋转电机过流/  旋转电机故障
	final String [] str_array1={"旋转电机故障"," 旋转电机过流"," 中间吸盘故障","中间吸盘过流",
			"真空泵电机堵转","真空泵电机过流","辅助吸盘及泄气阀故障","辅助吸盘及泄气阀过流"};

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
	 * 字节转换！
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

	//提取有效字符串34 12 cf @Z1
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

	
	//34 12 cf *@Z1+长度（1个字节）+告警信息(3字节)+code(1个字节)++运行状态(1字节)+电量(1字节)+校验+>
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
		String header_val=val.substring(index-2,index);//短地址cf
		String val_tmp = val.substring(index+2+1*2+1,index+2+7*2+1);
		Vector<Integer> int_list=byte2_To_Hex(val_tmp);

		
		//匹配测试
//		if(MainActivity.matchbyte(header_val, str_num))
//			for(int i = 0; i<5;i++){
//				System.out.println("匹配成功*****************");
//			}
		//s = XiangQing.StrToHex(str_num).toString();
		try {
			s = new String(XiangQing.StrToHex(str_num),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//, "UTF-8"可加编码格式
		 
		//System.out.println("before"+int_list);
		if (int_list.get(3).intValue() != 0x01 ||!header_val.equals(s)) return;//||  matchbyte(header_val,str_num)
		int_list.remove(3);
		//add zigbee signal!
		int_list.add(Integer.valueOf(header_val.charAt(1)));//2 问题？

		//System.out.println("after"+int_list);
		parseMainUiData(int_list, str_num);
		setMainUiView(cell, str_num);
		
//		System.out.print( System.getProperty("sun.jnu.encoding"));
	}

	//包含报警！
	//34 12 cf @Z1+size+告警信息+code+运行状态(03清扫结束，0手动， 1清扫中，2调试中)+电池电量+主机电流(空？)+角度(2个字节)+jiaoyan+>
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
	//不包含报警！
	//34 12 cf @Z1+size+告警信息+code->5+电流1+电流2+3+4+5+6+校验+>
	//displayMotorUi(recv_str,view,MotorPage.Page1,"");
	public void displayMotorUi(String recv_str, Vector<TextView> view, MotorPage Page, String str_num) {
		String val = splitString(recv_str);
		if (val == null) return;


		int index=val.indexOf("@Z1");

		String header_val=val.substring(index-2,index);//3
		String val_tmp ;//不包含报警！
		switch(Page) {
		case Page2://前+后+辅助
			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("04")){
				return;
			}
			val_tmp =val.substring(index+2+4*2+1,index+2+8*2+1);
			break;
		case Page1://电流1+电流3+4+2(+3+4) //前机/后机/辅助! 前机直线1电流+前机泄气阀电流+后机电流+辅助电流！
			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("03")){
				return;
			}
			val_tmp = val.substring(index+2+4*2+1,index+2+9*2+1);
			break;
		case Page3://电流1+电流2+3+4

			if (!val.substring(index+2+4*2+1, index+2+5*2+1).equals("05")){
				return;
			}
			val_tmp = val.substring(index+2+4*2+1,index+2+9*2+1);
			break;
		case Page4:	//电流1+电流2         //丝杆+滚刷 
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
	 * 解析电流数据
	 * @vector:要显示的int！
	 * @str_num:对应于短地址！
	 * return： 要显示的数值list！
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
	//解析hex对应alarm_list!返回报警！
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

	//34 12 cf @Z1+size+告警信息+code+运行状态(03清扫结束，0手动， 1清扫中，2调试中)+电池电量+主机电流(空？)+角度(2个字节)
	private Vector<String> parseChildData(Vector<Integer> vector, String str_num) {
		Vector<String> str_list = new Vector<String>();
		String tmp_str; 
		//if (vector.get(3).intValue() !=0x01) return null;

		//0
		switch(vector.get(3).intValue()) {
		case 0x00:  
			str_list.add("手动");
			break;
		case 0x01:
			str_list.add("清扫中");
			break;
		case 0x02:
			str_list.add("调试中");
			break;
		case 0x03:
			str_list.add("清扫结束");
			break;
		default: break;
		}

		//1 alarm
		int []hex =new int[]{vector.get(2).intValue(),vector.get(1).intValue(),vector.get(0).intValue()};

		Vector<String>alarm_list = getAlarmString(hex);
		if(vector.get(4).intValue() < 10) {
			alarm_list.add("电池电量不足");
		}
		//添加alarm 
		String str_alarm=alarm_list.toString();
		str_list.add(str_alarm);
		
		//2 电量
		tmp_str = String.valueOf(vector.get(4).intValue())+"%";
		str_list.add(tmp_str);
		//3 电流
		//		tmp_str = String.valueOf(vector.get(5).intValue()/100);
		//		str_list.add(tmp_str);

		str_list.add("");

		//4 角度
		tmp_str = String.valueOf((vector.get(5).intValue()*16*16+vector.get(6).intValue())/100-90);
		str_list.add(tmp_str);
		this.str_child_list=str_list;
		return str_list;
	}


	// *@Z1+长度+告警信息(3字节)+code++运行状态(1字节)+电量(1字节)+校验+> 
	private Vector<String> parseMainUiData(Vector<Integer> vector, String str_num) {
		Vector<String> str_list = new Vector<String>();
		String tmp_str; 
		//code!
		//	if (vector.get(3).intValue() !=0x01) return null;

		//0
		switch(vector.get(3).intValue()) {
		case 0x00:  
			str_list.add("手动");
			break;
		case 0x01:
			str_list.add("清扫中");
			break;
		case 0x02:
			str_list.add("调试中");
			break;
		case 0x03:
			str_list.add("清扫结束");
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
			alarm_list.add("电池电量不足");
		}
		//添加alarm
//		for (int i=0;i<alarm_list.size();i++) {
//			str_list.add(alarm_list.get(i));
//		}
		String str_alarm = alarm_list.toString();
		str_list.add(str_alarm);
		str_main_list = str_list;
		return  str_list;
	}

	//报警中的电池电量！

	private Vector<String> getAlarmString(int []hex) {
		Vector<String> alarm_list3=parseAlarm(hex[0],str_array3);
		Vector<String> alarm_list2=parseAlarm(hex[1],str_array2);
		Vector<String> alarm_list1=parseAlarm(hex[2],str_array1);
		Vector<String> alarm_string = new Vector<String>();

		for (int i=0;i<alarm_list3.size();i++) {
			if (alarm_list3.elementAt(i).contains("气")){
				alarm_string.add("气压异常");
			}
		}

		for (int i=0;i<alarm_list2.size();i++) {
			if (alarm_list2.elementAt(i).contains("机")){
				alarm_string.add("后机电机异常");
			}
		}

		for (int i=0;i<alarm_list1.size();i++) {
			if (alarm_list1.elementAt(i).contains("机")){
				alarm_string.add("前机电机异常");
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
