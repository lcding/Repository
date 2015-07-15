package com.ecovacs.android_cr1301;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecovacs.btutils.BluetoothChat;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
/**
 * 创建一个继承ExpandableListActivity的Activity
 */
public class XiTong extends ExpandableListActivity {
	private Button fanhui = null;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xitong_shezhi_menu);

		

		/**
		 * 按钮句柄
		 */
		fanhui = (Button)findViewById(R.id.fanhui);
		fanhui.setOnClickListener(btnClickListener);
		/**
		 * 先定义组的数据
		 * 定义一个list，该list对象为一级条目提供数据
		 * 几个条目就要几个map数据
		 */
		List<Map<String, String>> groups = new ArrayList<Map<String,String>>();
		Map<String, String> group2 = new HashMap<String, String>();
		group2.put("group", "网络设置");//属于组条目，组条目的第二条
		Map<String, String> group1 = new HashMap<String, String>();
		group1.put("group", "蓝牙设置");//属于组条目，组条目的第一条
		Map<String, String> group3= new HashMap<String, String>();
		group3.put("group", "背光灯设置");//属于组条目，组条目的第三条
		Map<String, String> group4 = new HashMap<String, String>();
		group4.put("group", "语言");//属于组条目，组条目的第四条
		Map<String, String> group5 = new HashMap<String, String>();
		group5.put("group", "关于");//属于组条目，组条目的第五条
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);
		groups.add(group5);

		/**
		 * 蓝牙配置
		 * 定义一个list，该list对象为第一个一级条目（提供二级条目数据）
		 */List<Map<String, String>> child1 = new ArrayList<Map<String,String>>();
		Map<String, String> child2data1 = new HashMap<String, String>();
		child2data1.put("child", "蓝牙");		
		child1.add(child2data1);
		
		/**
		 * 再定义子条目
		 * 网络配置
		 * 定义一个list，该list对象为第二个一级条目（提供二级条目数据）
		 */
		List<Map<String, String>> child2 = new ArrayList<Map<String,String>>();

		Map<String, String> child1data1 = new HashMap<String, String>();
		child1data1.put("child", "WI-FI配置");//属于子条目，子条目的第一条		
		Map<String, String> child1data2 = new HashMap<String, String>();
		child1data2.put("child", "ZigBee配置");//属于子条目，子条目的第二条		
		child2.add(child1data1);
		child2.add(child1data2);
		/**
		 * 背光灯
		 * 定义一个list，该list对象为第三个一级条目（提供二级条目数据）
		 */
		List<Map<String, String>> child3 = new ArrayList<Map<String,String>>();
		Map<String, String> child3data1 = new HashMap<String, String>();
		child3data1.put("child", "背光灯");		
		child3.add(child3data1);
		/**
		 * 语言
		 * 定义一个list，该list对象为第四个一级条目（提供二级条目数据）
		 */
		List<Map<String, String>> child4 = new ArrayList<Map<String,String>>();
		Map<String, String> child4data1 = new HashMap<String, String>();
		child4data1.put("child", "简体中文");	
		Map<String, String> child4data2 = new HashMap<String, String>();
		child4data2.put("child", "繁体中文");
		Map<String, String> child4data3 = new HashMap<String, String>();
		child4data3.put("child", "English");
		child4.add(child4data1);
		child4.add(child4data2);
		child4.add(child4data3);
		/**
		 * 关于
		 * 定义一个list，该list对象为第四个一级条目（提供二级条目数据）
		 */
		List<Map<String, String>> child5 = new ArrayList<Map<String,String>>();
		Map<String, String> child5data1 = new HashMap<String, String>();
		child5data1.put("child", "帮助与反馈");	
		Map<String, String> child5data2 = new HashMap<String, String>();
		child5data2.put("child", "欢迎页");
		Map<String, String> child5data3 = new HashMap<String, String>();
		child5data3.put("child", "功能介绍");
		Map<String, String> child5data4 = new HashMap<String, String>();
		child5data4.put("child", "系统更新");
		child5.add(child5data1);
		child5.add(child5data2);
		child5.add(child5data3);
		child5.add(child5data4);

		/**
		 * 所有二级条目的数据全部放到childs这个list里面
		 */
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String,String>>>();
		childs.add(child1);
		childs.add(child2);
		childs.add(child3);
		childs.add(child4);
		childs.add(child5);

		/**
		 * 生成一个SimpleExpandableListAdapter对象
		 * 参数
		 * 1.context
		 * 2.一级条目的数据
		 * 3.设置一级条目的样式的布局文件
		 * 4.指定一级条目数据的key
		 * 5.指定一级条目数据显示控件的id即group里面的textview
		 * 6.指定二级条目的数据
		 * 7.用来设置二级条目样式的布局文件
		 * 8.指定二级条目的key
		 * 9.指定二级条目控件显示的id
		 */
		SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
				this, groups, R.layout.xitong_shezhi_group, 
				new String[]{"group"}, new int[]{R.id.groupTo}, childs, 
				R.layout.xitong_shezhi_child, new String[]{"child"}, new int[]{R.id.childTo});
		//将SimpleExpandableListAdapter对象设置给当前的ExpandableListActivity
		setListAdapter(sela);
		
		
		/**
		 * 启动蓝牙service
		 */
//		Intent intent = new Intent(this,
//	            BluetoothChatService.class);
//	    startService(intent);
//	    
//	    System.out.println("XiTong---start Service");
	}

	/**
	 * 条目的点击事件
	 */
	@Override    
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
		//组条目一
		case 0:
			//System.out.println("组条目第一个");
			switch (childPosition) {   
			case 0:
				/**
				 * 调用蓝牙工具类
				 * 配对后启动service
				 */
				System.out.println("蓝牙");
			Intent toBTutilsIntent = new Intent(XiTong.this,
					BluetoothChat.class);
			startActivity(toBTutilsIntent);
				break;
			default:;break;
			}
		break; 
		//组条目二
		case 1: 
			//System.out.println("组条目第二个");
			switch (childPosition) {   
			case 0:System.out.println("WIFI");
				break;
			case 1:System.out.println("ZigBee");
				break;
			default:;break;
			}
		break;
		//组条目三
		case 2: 
			//System.out.println("组条目第三个");
			switch (childPosition) {   
			case 0:System.out.println("背光灯");
				break;
			default:;break;
			}
		break;
		//组条目四
		case 3:  
			//System.out.println("组条目第四个");
			switch (childPosition) {   
			case 0:System.out.println("简体中文");
				break;
			case 1:System.out.println("繁体中文");
			break;
			case 2:System.out.println("english");
			break;
			default:;break;
			}
		break;
		
		//组条目五
		case 4:  
			//System.out.println("组条目第五个");
			switch (childPosition) {   
			case 0:System.out.println("帮组与反馈");
				break;
			case 1:System.out.println("欢迎页");
			break;
			case 2:System.out.println("功能介绍");
			break;
			case 3:System.out.println("系统更新");
			break;
			default:;break;
			}
		break;
		default:;break;
		}    
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
	
	/** 
	 * 按钮监听
	 */

	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.fanhui:
				Intent xitongtomainactivityIntent = new Intent(XiTong.this,
						MainActivity.class);
				startActivity(xitongtomainactivityIntent);break;
			default:;break;
			}
		}
	}
	/**
	 * 返回键的重写
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Intent toMainActivityIntent = new Intent(XiTong.this,
				MainActivity.class);
		startActivity(toMainActivityIntent);
		return super.onKeyDown(keyCode, event);
	}
	
	
}
