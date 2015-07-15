package com.ecovacs.android_cr1301;

import com.ecovacs.btutils.BluetoothChat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
/**
 * ����activity
 * @author lee
 *
 */
public class SplashActivity extends Activity {

	
	private ImageView IV = null;
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�3��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qidong_splash);	
		/**
		 * ��ʱ��ת
		 */
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent BluetoothChatIntent = new Intent(SplashActivity.this,
						BluetoothChat.class);
				SplashActivity.this.startActivity(BluetoothChatIntent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				SplashActivity.this.finish();
				
			}

		}, SPLASH_DISPLAY_LENGHT);

		//�ҵ��ؼ�
		IV=(ImageView)findViewById(R.id.welcome_img);//Ϊ�˶���ͼƬ��ʾ
		//���ö�������
		IV.setBackgroundResource(R.drawable.splash_img);
		//��ö�������
		AnimationDrawable _animaition = (AnimationDrawable)IV.getBackground(); 
		//֮�������������Ч��
		_animaition.setOneShot(false); //�Ƿ��������һ��  
//		if(_animaition.isRunning()) { //�Ƿ��������У� 
//			_animaition.stop();//ֹͣ 
//		}   
		_animaition.start();//���� 
	}
}
