package com.ecovacs.android_cr1301;
/**
 * 本类的引用
 */
import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface; 
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


/**
 * 表格工具类的引用
 */
import com.ecovacs.android_cr1301.R;
import com.ecovacs.btutils.BluetoothChat;
import com.ecovacs.btutils.BluetoothChatService;
import com.ecovacs.formutils.TableAdapter;
import com.ecovacs.formutils.TableAdapter.TableCell;
import com.ecovacs.formutils.TableAdapter.TableRow;
import com.ecovacs.uart.ComData;
import com.ecovacs.uart.PollData;

/**
 * @author hellogv
 */
public class FormMenu extends Activity {
	ComData com_data = new ComData(BluetoothChat.mChatService);

	//String	address_str=null;
	int index = 0,index1 = 100,index_size = 0;
	public byte[] FormMenu_address;//FormMenu页面的短地址
	private boolean flag;
	public String string;
	private String fmsg = "";    //保存用数据缓存
	private String xindao = "";//信道信息 
	private String filename = "";//已经配对的编号信息，编号需要对应短地址
	private Button menuxinzeng,menushanchu;//添加
	ArrayList<TableRow> table = new ArrayList<TableRow>();

	private ButtonClickListener btnClickListener = new ButtonClickListener();//按钮
	/** Called when the activity is first created. */
	ListView lv;
	//	private TableRowView table_view = new TableRowView();
	private Handler handler = new Handler();
	TableAdapter tableAdapter = new TableAdapter(this, table);

	static Vector<String> address_list=new Vector<String>();
	Vector<TableCell> cell_list0=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list1=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list2=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list3=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list4=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list5=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list6=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list7=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list8=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list9=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list10=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list11=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list12=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list13=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list14=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list15=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list16=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list17=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list18=new Vector<TableCell>();//为添加表格框架的一行
	Vector<TableCell> cell_list19=new Vector<TableCell>();//为添加表格框架的一行
	//Vector<String> address_list = new Vector<String>();//短地址

	/**
	 * 
	 * 接收bluetoothchatservice的 数据
	 */
	public BluetoothChatService bluetoothchatservice =BluetoothChat.mChatService;
	private BluetoothChat bluetoothchat;
	private MyRunnable myrunnable= new MyRunnable();
	class MyRunnable implements Runnable{
		@Override
		public void run() {	
			if (bluetoothchatservice.getState() != BluetoothChatService.STATE_CONNECTED) {}else{
				//判断选择哪个短地址加指令一起发送
				if(!PollData.read("test.txt").isEmpty()){
					if(index < index_size){
						switch (index){
						case 0:simple(0);break;
						case 1:simple(1);break;
						case 2:simple(2);break;
						case 3:simple(3);break;
						case 4:simple(4);break;
						case 5:simple(5);break;
						case 6:simple(6);break;
						case 7:simple(7);break;
						case 8:simple(8);break;
						case 9:simple(9);break;
						case 10:simple(10);break;
						case 11:simple(11);break;
						case 12:simple(12);break;
						case 13:simple(13);break;
						case 14:simple(14);break;
						case 15:simple(15);break;
						case 16:simple(16);break;
						case 17:simple(17);break;
						case 18:simple(18);break;
						case 19:simple(19);break;
						default:break;					
						}					
					}else{
						index = 0;
					}
				}
				tableAdapter.notifyDataSetChanged();//表格刷新数据


				//为了区分表格的哪一行显示
				if(address_list.size()>=1){				
					if(index1 < index_size){
						switch(index1){
						case 0:simple_index1(0, cell_list0);break;//Log.d("1", address_list.get(0)+"@Z902010102>");
						case 1:simple_index1(1, cell_list1);break;//Log.d("2", address_list.get(0)+"@Z902010102>");
						case 2:simple_index1(2, cell_list2);break;//Log.d("3", address_list.get(0)+"@Z902010102>");
						case 3:simple_index1(3, cell_list3);break;
						case 4:simple_index1(4, cell_list4);break;
						case 5:simple_index1(5, cell_list5);break;
						case 6:simple_index1(6, cell_list6);break;
						case 7:simple_index1(7, cell_list7);break;
						case 8:simple_index1(8, cell_list8);break;
						case 9:simple_index1(9, cell_list9);break;
						case 10:simple_index1(10, cell_list10);break;//Log.d("1", address_list.get(0)+"@Z902010102>");
						case 11:simple_index1(11, cell_list11);break;//Log.d("2", address_list.get(0)+"@Z902010102>");
						case 12:simple_index1(12, cell_list12);break;//Log.d("3", address_list.get(0)+"@Z902010102>");
						case 13:simple_index1(13, cell_list13);break;
						case 14:simple_index1(14, cell_list14);break;
						case 15:simple_index1(15, cell_list15);break;
						case 16:simple_index1(16, cell_list16);break;
						case 17:simple_index1(17, cell_list17);break;
						case 18:simple_index1(18, cell_list18);break;
						case 19:simple_index1(19, cell_list19);break;
						default:break;
						}

					}else{
						index1 = 0;
					}
				}
				if(flag){
					handler.postDelayed(myrunnable, 100);//递归调用
				}		
			}
		}		
	}

	public void blueSend(){
		//Thread.sleep(50);
		if(index<=index_size){
			bluetoothchatservice.write(MyTab.SendMessage("@Z902010102>",FormMenu_address));
		}			
		//Thread.sleep(50);			
	}
	public void simple(int i){
		FormMenu_address = XiangQing.StrToHex(address_list.get(i));
		table.get(i).getCellValue(0).value=address_list.get(i);index1 = i;index++;
	}
	public void simple_index1(int i,Vector<TableCell> cell_list){
		blueSend();com_data.diplayMainUi(BluetoothChat.readMessage, cell_list, address_list.get(i));index1++;
	}
	
	/**
	 * onCreate方法里面调用runnable
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//super.onSaveInstanceState(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jiankong_form_menu);

		FormMenu_address = XiangQing.StrToHex("1111");
		address_list = PollData.read(("test.txt"));
		index_size = address_list.size();		
		/**
		 * 按钮句柄
		 */
		menuxinzeng = (Button)findViewById(R.id.menuxinzeng);//添加按钮句柄
		menuxinzeng.setOnClickListener(btnClickListener);
		menushanchu = (Button)findViewById(R.id.menushanchu);
		menushanchu.setOnClickListener(btnClickListener);
		lv = (ListView) this.findViewById(R.id.ListView01);
	
		/**
		 * 定义行
		 */
		TableCell[] cells0 = new TableCell[5];addRow(cells0);// 每行5个单元
		TableCell[] cells1 = new TableCell[5];addRow(cells1);// 每行5个单元
		TableCell[] cells2 = new TableCell[5];addRow(cells2);// 每行5个单元
		TableCell[] cells3 = new TableCell[5];addRow(cells3);// 每行5个单元
		TableCell[] cells4 = new TableCell[5];addRow(cells4);// 每行5个单元
		TableCell[] cells5 = new TableCell[5];addRow(cells5);// 每行5个单元
		TableCell[] cells6 = new TableCell[5];addRow(cells6);// 每行5个单元
		TableCell[] cells7 = new TableCell[5];addRow(cells7);// 每行5个单元
		TableCell[] cells8 = new TableCell[5];addRow(cells8);// 每行5个单元
		TableCell[] cells9 = new TableCell[5];addRow(cells9);// 每行5个单元
		TableCell[] cells10 = new TableCell[5];addRow(cells10);// 每行5个单元
		TableCell[] cells11 = new TableCell[5];addRow(cells11);// 每行5个单元
		TableCell[] cells12 = new TableCell[5];addRow(cells12);// 每行5个单元
		TableCell[] cells13 = new TableCell[5];addRow(cells13);// 每行5个单元
		TableCell[] cells14 = new TableCell[5];addRow(cells14);// 每行5个单元
		TableCell[] cells15 = new TableCell[5];addRow(cells15);// 每行5个单元
		TableCell[] cells16 = new TableCell[5];addRow(cells16);// 每行5个单元
		TableCell[] cells17 = new TableCell[5];addRow(cells17);// 每行5个单元
		TableCell[] cells18 = new TableCell[5];addRow(cells18);// 每行5个单元
		TableCell[] cells19 = new TableCell[5];addRow(cells19);// 每行5个单元
		/**
		 * 赋值为图片
		 */
		//		cells[cells.length - 1] = new TableCell(R.drawable.jiankong_dian1,
		//				titles[cells.length - 1].width, 
		//				LayoutParams.WRAP_CONTENT,
		//				TableCell.IMAGE);
		lv.setAdapter(tableAdapter);
		lv.setOnItemClickListener(new ItemClickEvent());
		
		//添加表格单元到list，以便赋值
		newTableCell(cell_list0,0);newTableCell(cell_list1,1);newTableCell(cell_list2,2);
		newTableCell(cell_list3,3);newTableCell(cell_list4,4);newTableCell(cell_list5,5);
		newTableCell(cell_list6,6);newTableCell(cell_list7,7);newTableCell(cell_list8,8);
		newTableCell(cell_list9,9);newTableCell(cell_list10,10);newTableCell(cell_list11,11);
		newTableCell(cell_list12,12);newTableCell(cell_list13,13);newTableCell(cell_list14,14);
		newTableCell(cell_list15,15);newTableCell(cell_list16,16);newTableCell(cell_list17,17);
		newTableCell(cell_list18,18);newTableCell(cell_list19,19);
		//table.get(0).getCellValue(0).value="001";//添加成功的时候赋值编号
		flag = true;
		handler.postDelayed(myrunnable, 300);
	}

	//添加行
	//@SuppressWarnings("deprecation")
	public void addRow(TableCell[] cell){
		int width = this.getWindowManager().getDefaultDisplay().getWidth()/5;
		for (int i = 0; i < cell.length; i++) {
			cell[i] = new TableCell("",width, 
					LayoutParams.FILL_PARENT, 
					TableCell.STRING);
		}
		table.add(new TableRow(cell));
	}
	//把表格元素添加到list
	public void newTableCell(Vector<TableCell> cell_list,int j){
		cell_list.clear();
		for (int i=1;i<5;i++) {//第一行，除编号以外的后四个元素
			cell_list.add(table.get(j).getCellValue(i));
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		FormMenu_address = XiangQing.StrToHex("1111");
		address_list = PollData.read(("test.txt"));
		index_size = address_list.size();
		flag = true;
		handler.postDelayed(myrunnable, 500);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		flag = false;
		handler.removeCallbacks(myrunnable);
	}
	/**
	 * 选中这一行，将这一行的数据传递给下一个页面
	 * 下一个页面是公用的页面，即只是把数据传递过去
	 */
	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			/**
			 * 将选中行的信息取出来传递
			 * 需要传递选中行的所有信息到下一页面
			 */

			///TableAdapter.TableRow.this.setBackgroundColor(Color.BLUE);
			//arg0.setDescendantFocusability(ViewGroup.GONE); //获得焦点 
			if(!PollData.read("test.txt").isEmpty()){
				if(arg2<index_size){
					//tableAdapter.getView(arg2, arg0, arg0).setBackgroundColor(Color.RED);
					flag = false;
					handler.removeCallbacks(myrunnable);
		
					Intent toXiangQingIntent = new Intent(FormMenu.this,
							XiangQing.class);
					Bundle bundle = new Bundle();
					//table.get(arg2).getCellValue(1).getCellValue(0).value=String.valueOf(arg2+1);//添加成功的时候赋值编号
					//考虑添加获得焦点时的颜色变化
					
					bundle.putString("transdata",address_list.get(arg2));
					//System.out.println(String.valueOf(arg2)+"********************");
					toXiangQingIntent.putExtras(bundle);
					startActivity(toXiangQingIntent);
				}else{
					Toast.makeText(FormMenu.this, "第"+String.valueOf(arg2+1)+"行没有可控制的机器", 50).show();
				}

			}else{
				Toast.makeText(FormMenu.this, "第"+String.valueOf(arg2+1)+"行没有可控制的机器", 50).show();
			}
			//Toast.makeText(FormMenu.this, "第"+String.valueOf(arg2)+"行的详细数据", 500).show();
		}
	}
	/**
	 * 返回 新增按钮
	 */
	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {			

			case R.id.menuxinzeng:
//				//切换到新增页面的时候给中继器配置成默认
//				String set1 = "@ZA0A400400190000FFFF0600";//默认配置
//				bluetoothchatservice.write(BluetoothChat.hexxor(set1).getBytes());
				
				Intent toXinZengIntent = new Intent(FormMenu.this,
						XinZeng.class);
				startActivity(toXinZengIntent);break;

			case R.id.menushanchu:

				LayoutInflater factory01 = LayoutInflater.from(FormMenu.this); // 图层模板生成器句柄
				final View DialogxieqiView = factory01.inflate(R.layout.popup_box_allshanchu, null); // 用sname.xml模板生成视图模板
				new AlertDialog.Builder(FormMenu.this).setTitle("Tips:")
				.setView(DialogxieqiView) // 设置视图模板
				.setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
				{
					public void onClick(DialogInterface dialog,
							int whichButton) {
						//删除
						PollData.deleteAllFile("test");						
						address_list = PollData.read(("test.txt"));
						index_size = address_list.size();	
						//清空所有数据
						for(int i = 0 ; i<10;i++){
							for(int j = 0 ; j <5 ; j++){
								table.get(i).getCellValue(j).value = "";
							}
						}
						//tableAdapter.notifyDataSetChanged();//表格刷新数据

					}
				}).setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						//不删除

					}
				}).show();
				break;
			default:break;
			}
		}
	}
	/**
	 * 重写方法实现页面数据切换之后还保留
	 * onSaveInstanceState仅在Activity变成非Active状态时调用，
	 * 但不在调用finish来关闭它或用户按下Back按钮时调用。
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save the current data, for instance when changing screen orientation
		//	 outState.putSerializable("dataset", mDataset);
		//	 outState.putSerializable("renderer", mRenderer);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		// restore the current data, for instance when changing the screen
		// orientation
		//	 mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
		//	 mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
	}
	//过时的方法
	//	@Override
	//	public Object onRetainNonConfigurationInstance() {
	//		// TODO Auto-generated method stub
	//		return super.onRetainNonConfigurationInstance();
	//	}
	//
	//	@Override
	//	public Object getLastNonConfigurationInstance() {
	//		// TODO Auto-generated method stub
	//		return super.getLastNonConfigurationInstance();
	//	}
	/**
	 * 返回键的重写
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
}