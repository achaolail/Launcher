package com.us.launcher;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.us.launcher.adapter.ViewPagerAdapter;
import com.us.launcher.apps.AllApps;
import com.us.launcher.utils.MyDate;

public class Launcher extends FragmentActivity implements OnClickListener {

	protected static final int SWITCH_PAGER = 1;
	public static final int UPDATA = -6;
	private ImageButton onLine_education;
	private ImageButton health;
	private ImageButton family_all;
	private ImageButton home_safe;
	private ImageButton move_play;
	private ImageButton online_doctor;
	private List<ResolveInfo> mApps;
	private ViewPager viewPager;
	private ImageButton startApps;
	private ArrayList<ImageView> mImageViews;
	// private OnFocusChangeListener listener;
	private int[] mImageIds = new int[] { R.drawable.ads01,R.drawable.ads02,
			R.drawable.ads03,R.drawable.ads04 };

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SWITCH_PAGER:
				switchViePpager();
				break;
			case UPDATA:
				MyDate.setSystemTime() ;
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
		setContentView(R.layout.launcher);
		initView();
	}

	@Override
	protected void onResume() {
		loadApps();
		handler.sendEmptyMessageDelayed(UPDATA, 3000);
		super.onResume();
	}

	private void initView() {

		startApps = (ImageButton) findViewById(R.id.startapps);
		onLine_education = (ImageButton) findViewById(R.id.onLine_education);
		health = (ImageButton) findViewById(R.id.health);
		family_all = (ImageButton) findViewById(R.id.family_call);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		home_safe = (ImageButton) findViewById(R.id.home_safe);
		move_play = (ImageButton) findViewById(R.id.move_play);
		online_doctor = (ImageButton) findViewById(R.id.doctor);

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
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				startActivity(new Intent(Launcher.this, AireWebViewActivity.class))	;
				}
			});
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
			startApp("健康管理");
			startApp("aireHealth");
		}
		if (v.getId() == R.id.onLine_education) {
			// startApp("养生堂");
			startActivity(new Intent(Launcher.this, AddActivity.class));
		}

		if (v.getId() == R.id.family_call) {
			startApp("AireTV");
		}
		if (v.getId() == R.id.viewpager) {
			startApp("图库");
			startApp("Gallery");
		}
		if (v.getId() == R.id.home_safe) {
			startApp("家庭保安");
			startApp("SECURITY");
		}
		if (v.getId() == R.id.move_play) {
			startActivity(new Intent(this, TVProugramActivity.class));
			// startApp("电视家2.0");
			// startApp("TV Plus 2.0");

		}
		if (v.getId() == R.id.doctor) {
			startSystemSetting();
			// startApp("设置");
			// startApp("SettingsMbox");
		}
		if (v.getId() == R.id.startapps) {
			Intent intent = new Intent(this, AllApps.class);
			startActivity(intent);
		}

	}

	
	private void startSystemSetting() {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings","com.android.settings.Settings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		startActivityForResult( intent , 0);
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
//				if ((upTimeMillis - downTimeMillis) > 2000) {
//					startApp("图库");
//				}
				
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

}
