package com.ecovacs.android_cr1301;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DianjiguanliActivity extends Activity {
	private Button queding;
	private Button quxiao;
	private Button gaimi;
	private Button bangzhu;
	private ButtonClickListener btnClickListener = new ButtonClickListener();//°´Å¥
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiangqing_dianji_guanli);
		/**
		 * °´Å¥
		 */
		queding=(Button)findViewById(R.id.queding);
		quxiao=(Button)findViewById(R.id.quxiao);
		gaimi=(Button)findViewById(R.id.gaimi);
		bangzhu=(Button)findViewById(R.id.bangzhu);
		queding.setOnClickListener(btnClickListener);
		quxiao.setOnClickListener(btnClickListener);
		gaimi.setOnClickListener(btnClickListener);
		bangzhu.setOnClickListener(btnClickListener);
	}

	class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){			
			case R.id.queding:
				Intent toMyTabIntent = new Intent(DianjiguanliActivity.this,
						MyTab.class);
				startActivity(toMyTabIntent);
				break;
			case R.id.quxiao:
				break;
			case R.id.gaimi:
				break;
			case R.id.bangzhu:
				break;
			default:;break;
			}
		}
	}
}
