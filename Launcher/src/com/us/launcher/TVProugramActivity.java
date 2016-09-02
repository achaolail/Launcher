package com.us.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.launcher.app.MyApplication;
import com.us.launcher.utils.Log;
import com.us.launcher.utils.MyPreference;

public class TVProugramActivity extends Activity {

	private List<ResolveInfo> mApps;
	private LinearLayout left;
	private GridView gridView;
	private HashSet<String> infoSet;
	private File file;
	private final int REFRESH = 0;
	private AppsAdapter adapter;
	private MyPreference pf;
	private ArrayList<ResolveInfo> useInfo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH:
				if (adapter == null) {
					adapter = new AppsAdapter();
				} else {
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.activity_tvprougram);

		infoSet = new HashSet<String>();
		pf = new MyPreference(this);
		useInfo = new ArrayList<ResolveInfo>();

		readCacheFile();
		for (ResolveInfo info : mApps) {
			if (infoSet.contains(info.activityInfo.packageName)) {
				useInfo.add(info);
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();

	};

	@Override
	protected void onDestroy() {
		writeInObjectOutPutFile(infoSet);
		destroyAllObj();
		super.onDestroy();

	};

	private void destroyAllObj() {
		handler.removeCallbacksAndMessages(null);
		mApps = null;
		left = null;
		gridView = null;
		infoSet = null;
		file = null;
		adapter = null;
		pf = null;
		useInfo = null;
		System.gc();
	}

	OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// 根据点击的item 得到position对应的 应用info信息。
			ResolveInfo info = useInfo.get(position);

			// 弹出对话框，是否要将这个应用，添加到个人喜好中心去。
			createFinishDialog(info, useInfo);

			return true;
		}
	};

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ResolveInfo info = useInfo.get(position);
			// 获取应用的包名。
			String pkg = info.activityInfo.packageName;
			// 获取应用名称
			String cls = info.activityInfo.name;

			ComponentName name = new ComponentName(pkg, cls);

			// 启动对应包名和应用名的app
			startApp(name);
		}
	};

	private void startApp(ComponentName name) {
		Intent intent = new Intent();
		intent.setComponent(name);
		startActivity(intent);

	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.apps_list);
		left = (LinearLayout) findViewById(R.id.ll_left);
		if (adapter == null) {
			adapter = new AppsAdapter();
			gridView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		gridView.setLongClickable(true);
		gridView.setOnItemLongClickListener(mOnItemLongClickListener);
		gridView.setOnItemClickListener(listener);

		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void loadApps() {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
	}

	public class AppsAdapter extends BaseAdapter {

		public AppsAdapter() {
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(getApplication(),
						R.layout.item_preferences_center, null);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.iv_appinfo);
				holder.name = (TextView) convertView
						.findViewById(R.id.tv_appinfo);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ResolveInfo info = useInfo.get(position);

			holder.icon.setImageDrawable(info.activityInfo
					.loadIcon(getPackageManager()));
			holder.name.setText(info.activityInfo
					.loadLabel(getPackageManager()));
			return convertView;
		}

		public final int getCount() {
			return useInfo.size();
		}

		public final Object getItem(int position) {
			return useInfo.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	class ViewHolder {
		public ImageView icon;
		public TextView name;

	}

	private void createFinishDialog(final ResolveInfo info,
			final ArrayList<ResolveInfo> list) {
		// TODO Auto-generated method stub

		// 创建对话框 AlertDialog.Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 设置标题
		builder.setTitle("Delete this application from personal preferences ");

		CharSequence name = info.activityInfo.loadLabel(getPackageManager());
		builder.setMessage("If delete this '" + name +
				"', the application will disappear in the Program fast start page");

		builder.setPositiveButton("delete", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pkgName = info.activityInfo.packageName;
					infoSet.remove(pkgName);
					useInfo.remove(info);
					
				handler.sendEmptyMessage(REFRESH);
				writeInObjectOutPutFile(infoSet);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("cancel", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();

	};

	private void writeInObjectOutPutFile(HashSet<String> set) {
		String spf = "";
		// StringBuilder sbpf = new StringBuilder();
		for (String packageName : set) {
			// sbpf.append(packageName+"&");
			spf += packageName + "&";
		}
		pf.write(MyApplication.USESOFT, spf);
		Log.e("SPF="+spf);
	}

	private void readCacheFile() {
		String soft = pf.read(MyApplication.USESOFT);
		if (soft != null) {
			String[] allSoft = soft.split("&");
			for (String str : allSoft) {
				infoSet.add(str);
			}
		}
	}

}
