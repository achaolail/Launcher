package com.aire.launcher.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {

	ArrayList<ImageView> mImageViews;

	public ViewPagerAdapter(ArrayList<ImageView> img) {
		super();
		this.mImageViews = img;
	}

	// 销毁条目
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	// 给viewPager添加条目
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position = position % mImageViews.size();
		// 根据添加的条目位置获取ImageView
		ImageView v = mImageViews.get(position);
		
		ViewGroup parent = (ViewGroup) v.getParent();
		 if (parent != null) {
		parent.removeAllViews();
		 } 
		// 把ImageView给ViewPager
		container.addView(v);
		// 添加什么就返回什么
		return v;
	}

	// 返回条目数量
	@Override
	public int getCount() {
		return mImageViews.size()*10000*500;
	}

	// 判断viewpager的页面的view对象是否和object一致
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}