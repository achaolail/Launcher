package com.us.launcher;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.launcher.app.MyApplication;
import com.us.launcher.utils.Log;
import com.us.launcher.utils.MyPreference;

/*
 * 
 * 8.2遇到的问题，做完所有的逻辑之后，发现infolist中并没有现在或者之前添加的 list信息。
 *    可以做的工作： 梳理逻辑，看看是不是，后续添加进list集合中的info 信息，没有在infoList集合中。
 * */
public class AddActivity extends Activity {

	private List<ResolveInfo> mApps;
	private LinearLayout left;
	private GridView gridView;
	private HashSet<String> infoSet;
	private File file;
	private MyPreference pf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.activity_add);
		infoSet = new HashSet<String>();
		pf = new MyPreference(this);

		readCacheFile();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();

	};

	@Override
	protected void onDestroy() {
		Log.e("此时infoList中的信息：");
		writeInObjectOutPutFile(infoSet);
		destroyAllObj();
		super.onDestroy();
	};

	private void destroyAllObj() {
		mApps = null;
		left = null;
		gridView = null;
		infoSet = null;
		file = null;
		pf = null;
		System.gc();
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ResolveInfo info = mApps.get(position);
			// 获取应用的包名。
			String pkg = info.activityInfo.packageName;
			// 获取应用名称
			String cls = info.activityInfo.name;

			ComponentName name = new ComponentName(pkg, cls);

			// 启动对应包名和应用名的app
			// startApp(name);

			// 弹出对话框，是否要将这个应用，添加到个人喜好中心去。
			infoSet = createFinishDialog(info, infoSet);
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

		gridView.setAdapter(new AppsAdapter());
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
						R.layout.item_preferences_choose, null);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.iv_appinfo);
				holder.name = (TextView) convertView
						.findViewById(R.id.tv_appinfo);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ResolveInfo info = mApps.get(position);
			holder.icon.setImageDrawable(info.activityInfo
					.loadIcon(getPackageManager()));
			holder.name.setText(info.activityInfo
					.loadLabel(getPackageManager()));
			return convertView;
		}

		public final int getCount() {
			return mApps.size();
		}

		public final Object getItem(int position) {
			return mApps.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	class ViewHolder {
		public ImageView icon;
		public TextView name;

	}

	private HashSet<String> createFinishDialog(final ResolveInfo info,
			final HashSet<String> set) {
		// TODO Auto-generated method stub

		// 创建对话框 AlertDialog.Builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 设置标题
		builder.setTitle("Whether or not to add to your personal preferences ");

		CharSequence name = info.activityInfo.loadLabel(getPackageManager());
		builder.setMessage("if add  '"
				+ name
				+ "' in personal preferences, you can quickly start this application in Program ");

		builder.setPositiveButton("yes", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String infoPkg = info.activityInfo.packageName;
				if (set.contains(infoPkg)) {
					return;
				} else {
					set.add(infoPkg);
				}

				writeInObjectOutPutFile(set);

				dialog.dismiss();
				Toast.makeText(getBaseContext(), "Add success !", 0).show();
				// finish();
			}
		});
		builder.setNegativeButton("no", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				// finish();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		return set;

	};

	private void writeInObjectOutPutFile(HashSet<String> set) {
		String spf = "";
		// StringBuilder sbpf = new StringBuilder();
		for (String packageName : set) {
			// sbpf.append(packageName+"&");
			spf += packageName + "&";
		}
		pf.write(MyApplication.USESOFT, spf);
		Log.e(spf + "**************");
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
