package com.aire.launcher;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aire.launcher.adapter.ViewPagerAdapter;
import com.aire.launcher.apps.AllApps;
import com.aire.launcher.listener.HomeWatcher;

public class Manager extends FragmentActivity implements OnClickListener {

	protected static final int SWITCH_PAGER = 1;
	private Button onLine_education;
	private Button health;
	private Button family_all;
	private Button home_safe;
	private Button move_play;
	private Button online_doctor;
	private List<ResolveInfo> mApps;
	private HomeWatcher homeWatcher;
	private ViewPager viewPager;
	private ImageButton startApps;
	private ArrayList<ImageView> mImageViews;
	private int[] mImageIds = new int[] { R.drawable.image1, R.drawable.image2,
			R.drawable.image3};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SWITCH_PAGER:
				switchViePpager();
				break;

			default:
				break;
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.activity_main);
		initView();
		
		homeWatcher = new HomeWatcher(this);
		homeWatcher.startWatch();

	}

	private void initView() {

		startApps = (ImageButton) findViewById(R.id.startapps);
		onLine_education = (Button) findViewById(R.id.onLine_education);
		health = (Button) findViewById(R.id.health);
		family_all = (Button) findViewById(R.id.family_call);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		home_safe = (Button) findViewById(R.id.home_safe);
		move_play = (Button) findViewById(R.id.move_play);
		online_doctor = (Button) findViewById(R.id.doctor);

		onLine_education.setOnClickListener(this);
		health.setOnClickListener(this);
		family_all.setOnClickListener(this);
		viewPager.setOnClickListener(this);
		home_safe.setOnClickListener(this);
		move_play.setOnClickListener(this);
		online_doctor.setOnClickListener(this);
		startApps.setOnClickListener(this);

		mImageViews = new ArrayList<ImageView>();
		mImageViews.clear();

		// 将图片放到Imageview中，在把imageview放到集合中，通过pageradapter显示出来
		for (int i = 0; i < mImageIds.length; i++) {
			// 将图片放到ImageView对象中
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]);
			// 把ImageView放入集合中
			mImageViews.add(imageView);
		}

		// 给mViewPager填充数据
		viewPager.setAdapter(new ViewPagerAdapter(mImageViews));
		viewPager.setCurrentItem(viewPager.getAdapter().getCount() / 2);
		handler.sendEmptyMessageDelayed(SWITCH_PAGER, 3000);

		// 设置view的触摸监听
		viewPager.setOnTouchListener(onTouchListener);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.health) {
			startApp("养生堂");
		}
		if (v.getId() == R.id.onLine_education) {
			startApp("唐颂智慧学堂");
		}

		if (v.getId() == R.id.family_call) {
			startApp("AireTV");
		}
		if (v.getId() == R.id.viewpager) {
			startApp("图库");
		}
		if (v.getId() == R.id.home_safe) {
			startApp("家庭保安");
		}
		if (v.getId() == R.id.move_play) {
			startApp("银河·奇异果");

		}
		if (v.getId() == R.id.doctor) {
			startApp("Dicomite");
		}
		if (v.getId() == R.id.startapps) {
			Intent intent = new Intent(this,AllApps.class);
			startActivity(intent);
		}
	}

	private void startApp(String labelName) {
		for (ResolveInfo info : mApps) {
			if (labelName.equals(info.activityInfo
					.loadLabel(getPackageManager()))) {

				// 获取应用的包名。
				String pkg = info.activityInfo.packageName;
				// 获取应用名称
				String cls = info.activityInfo.name;

				ComponentName name = new ComponentName(pkg, cls);

				Intent intent = new Intent();
				intent.setComponent(name);
				startActivity(intent);
			}
		}
	}

	private void loadApps() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	private void switchViePpager() {
		int currentItem = viewPager.getCurrentItem();
		currentItem++;
		viewPager.setCurrentItem(currentItem);
		handler.removeMessages(SWITCH_PAGER);
		handler.sendEmptyMessageDelayed(SWITCH_PAGER, 3000);
	}

	@Override
	protected void onDestroy() {
		homeWatcher.stopWatch();
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	OnTouchListener onTouchListener = new OnTouchListener() {
		private long downTimeMillis;
		private long upTimeMillis;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downTimeMillis = System.currentTimeMillis();
				// 停止自动滚动
				handler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_UP:
				upTimeMillis = System.currentTimeMillis();
				if ((upTimeMillis - downTimeMillis) > 1500) {
					startApp("图库");
				}
				// 继续自动滚动
				switchViePpager();
				break;
			case MotionEvent.ACTION_CANCEL:
				// 事件取消的调用
				switchViePpager();
				break;
			default:
				break;
			}
			return true;
		}
	};
	
}
