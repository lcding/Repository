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
 * 动画activity
 * @author lee
 *
 */
public class SplashActivity extends Activity {

	
	private ImageView IV = null;
	private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qidong_splash);	
		/**
		 * 延时跳转
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

		//找到控件
		IV=(ImageView)findViewById(R.id.welcome_img);//为了动画图片显示
		//设置动画背景
		IV.setBackgroundResource(R.drawable.splash_img);
		//获得动画对象
		AnimationDrawable _animaition = (AnimationDrawable)IV.getBackground(); 
		//之后可以启动动画效果
		_animaition.setOneShot(false); //是否仅仅启动一次  
//		if(_animaition.isRunning()) { //是否正在运行？ 
//			_animaition.stop();//停止 
//		}   
		_animaition.start();//启动 
	}
}
