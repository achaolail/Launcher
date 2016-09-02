package com.doc.launcher.apps;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
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

import com.doc.launcher.R;

public class AllApps extends Activity {

	private List<ResolveInfo> mApps;
	private LinearLayout left;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.activity_all_apps);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initView();
		
	};
	
	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ResolveInfo info = mApps.get(position);
			//获取应用的包名。
			String pkg = info.activityInfo.packageName;
			//获取应用名称
			String cls=info.activityInfo.name;
			
			ComponentName name = new ComponentName(pkg,cls);
			
			Intent intent = new Intent();
			intent.setComponent(name);
			startActivity(intent);
		}
	};
	
	
	

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
			ViewHolder holder ;
			if (convertView == null) {
				holder =new ViewHolder();
				convertView = View.inflate(getApplication() , R.layout.item_app_info, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.iv_appinfo);
				holder.name = (TextView) convertView.findViewById(R.id.tv_appinfo);
				convertView.setTag(holder);
			
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ResolveInfo info = mApps.get(position);
			holder.icon.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
			holder.name.setText(info.activityInfo.loadLabel(getPackageManager()));
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
	

}
