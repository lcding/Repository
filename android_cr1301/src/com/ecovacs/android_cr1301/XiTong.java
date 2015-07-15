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
 * ����һ���̳�ExpandableListActivity��Activity
 */
public class XiTong extends ExpandableListActivity {
	private Button fanhui = null;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//��ť
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xitong_shezhi_menu);

		

		/**
		 * ��ť���
		 */
		fanhui = (Button)findViewById(R.id.fanhui);
		fanhui.setOnClickListener(btnClickListener);
		/**
		 * �ȶ����������
		 * ����һ��list����list����Ϊһ����Ŀ�ṩ����
		 * ������Ŀ��Ҫ����map����
		 */
		List<Map<String, String>> groups = new ArrayList<Map<String,String>>();
		Map<String, String> group2 = new HashMap<String, String>();
		group2.put("group", "��������");//��������Ŀ������Ŀ�ĵڶ���
		Map<String, String> group1 = new HashMap<String, String>();
		group1.put("group", "��������");//��������Ŀ������Ŀ�ĵ�һ��
		Map<String, String> group3= new HashMap<String, String>();
		group3.put("group", "���������");//��������Ŀ������Ŀ�ĵ�����
		Map<String, String> group4 = new HashMap<String, String>();
		group4.put("group", "����");//��������Ŀ������Ŀ�ĵ�����
		Map<String, String> group5 = new HashMap<String, String>();
		group5.put("group", "����");//��������Ŀ������Ŀ�ĵ�����
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
		groups.add(group4);
		groups.add(group5);

		/**
		 * ��������
		 * ����һ��list����list����Ϊ��һ��һ����Ŀ���ṩ������Ŀ���ݣ�
		 */List<Map<String, String>> child1 = new ArrayList<Map<String,String>>();
		Map<String, String> child2data1 = new HashMap<String, String>();
		child2data1.put("child", "����");		
		child1.add(child2data1);
		
		/**
		 * �ٶ�������Ŀ
		 * ��������
		 * ����һ��list����list����Ϊ�ڶ���һ����Ŀ���ṩ������Ŀ���ݣ�
		 */
		List<Map<String, String>> child2 = new ArrayList<Map<String,String>>();

		Map<String, String> child1data1 = new HashMap<String, String>();
		child1data1.put("child", "WI-FI����");//��������Ŀ������Ŀ�ĵ�һ��		
		Map<String, String> child1data2 = new HashMap<String, String>();
		child1data2.put("child", "ZigBee����");//��������Ŀ������Ŀ�ĵڶ���		
		child2.add(child1data1);
		child2.add(child1data2);
		/**
		 * �����
		 * ����һ��list����list����Ϊ������һ����Ŀ���ṩ������Ŀ���ݣ�
		 */
		List<Map<String, String>> child3 = new ArrayList<Map<String,String>>();
		Map<String, String> child3data1 = new HashMap<String, String>();
		child3data1.put("child", "�����");		
		child3.add(child3data1);
		/**
		 * ����
		 * ����һ��list����list����Ϊ���ĸ�һ����Ŀ���ṩ������Ŀ���ݣ�
		 */
		List<Map<String, String>> child4 = new ArrayList<Map<String,String>>();
		Map<String, String> child4data1 = new HashMap<String, String>();
		child4data1.put("child", "��������");	
		Map<String, String> child4data2 = new HashMap<String, String>();
		child4data2.put("child", "��������");
		Map<String, String> child4data3 = new HashMap<String, String>();
		child4data3.put("child", "English");
		child4.add(child4data1);
		child4.add(child4data2);
		child4.add(child4data3);
		/**
		 * ����
		 * ����һ��list����list����Ϊ���ĸ�һ����Ŀ���ṩ������Ŀ���ݣ�
		 */
		List<Map<String, String>> child5 = new ArrayList<Map<String,String>>();
		Map<String, String> child5data1 = new HashMap<String, String>();
		child5data1.put("child", "�����뷴��");	
		Map<String, String> child5data2 = new HashMap<String, String>();
		child5data2.put("child", "��ӭҳ");
		Map<String, String> child5data3 = new HashMap<String, String>();
		child5data3.put("child", "���ܽ���");
		Map<String, String> child5data4 = new HashMap<String, String>();
		child5data4.put("child", "ϵͳ����");
		child5.add(child5data1);
		child5.add(child5data2);
		child5.add(child5data3);
		child5.add(child5data4);

		/**
		 * ���ж�����Ŀ������ȫ���ŵ�childs���list����
		 */
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String,String>>>();
		childs.add(child1);
		childs.add(child2);
		childs.add(child3);
		childs.add(child4);
		childs.add(child5);

		/**
		 * ����һ��SimpleExpandableListAdapter����
		 * ����
		 * 1.context
		 * 2.һ����Ŀ������
		 * 3.����һ����Ŀ����ʽ�Ĳ����ļ�
		 * 4.ָ��һ����Ŀ���ݵ�key
		 * 5.ָ��һ����Ŀ������ʾ�ؼ���id��group�����textview
		 * 6.ָ��������Ŀ������
		 * 7.�������ö�����Ŀ��ʽ�Ĳ����ļ�
		 * 8.ָ��������Ŀ��key
		 * 9.ָ��������Ŀ�ؼ���ʾ��id
		 */
		SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(
				this, groups, R.layout.xitong_shezhi_group, 
				new String[]{"group"}, new int[]{R.id.groupTo}, childs, 
				R.layout.xitong_shezhi_child, new String[]{"child"}, new int[]{R.id.childTo});
		//��SimpleExpandableListAdapter�������ø���ǰ��ExpandableListActivity
		setListAdapter(sela);
		
		
		/**
		 * ��������service
		 */
//		Intent intent = new Intent(this,
//	            BluetoothChatService.class);
//	    startService(intent);
//	    
//	    System.out.println("XiTong---start Service");
	}

	/**
	 * ��Ŀ�ĵ���¼�
	 */
	@Override    
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
		//����Ŀһ
		case 0:
			//System.out.println("����Ŀ��һ��");
			switch (childPosition) {   
			case 0:
				/**
				 * ��������������
				 * ��Ժ�����service
				 */
				System.out.println("����");
			Intent toBTutilsIntent = new Intent(XiTong.this,
					BluetoothChat.class);
			startActivity(toBTutilsIntent);
				break;
			default:;break;
			}
		break; 
		//����Ŀ��
		case 1: 
			//System.out.println("����Ŀ�ڶ���");
			switch (childPosition) {   
			case 0:System.out.println("WIFI");
				break;
			case 1:System.out.println("ZigBee");
				break;
			default:;break;
			}
		break;
		//����Ŀ��
		case 2: 
			//System.out.println("����Ŀ������");
			switch (childPosition) {   
			case 0:System.out.println("�����");
				break;
			default:;break;
			}
		break;
		//����Ŀ��
		case 3:  
			//System.out.println("����Ŀ���ĸ�");
			switch (childPosition) {   
			case 0:System.out.println("��������");
				break;
			case 1:System.out.println("��������");
			break;
			case 2:System.out.println("english");
			break;
			default:;break;
			}
		break;
		
		//����Ŀ��
		case 4:  
			//System.out.println("����Ŀ�����");
			switch (childPosition) {   
			case 0:System.out.println("�����뷴��");
				break;
			case 1:System.out.println("��ӭҳ");
			break;
			case 2:System.out.println("���ܽ���");
			break;
			case 3:System.out.println("ϵͳ����");
			break;
			default:;break;
			}
		break;
		default:;break;
		}    
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}
	
	/** 
	 * ��ť����
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
	 * ���ؼ�����д
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
