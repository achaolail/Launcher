package com.us.launcher.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Drawable>> imageCache;
	private Context _context;

	public AsyncImageLoader(Context context) {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
		_context = context;
	}

	public Drawable loadDrawable(final String path,
			final ImageCallback imageCallback) {
		if (imageCache.containsKey(path)) {
			SoftReference<Drawable> softReference = imageCache.get(path);
			Drawable bitmap = softReference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, path);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadBitmapFromPath(path);
				imageCache.put(path, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	public void displayImage(final ImageView imageView, final String path, final Drawable defaultDrawable) {
		if (imageCache.containsKey(path)) {
			SoftReference<Drawable> softReference = imageCache.get(path);
			Drawable bitmap = softReference.get();
			if (bitmap != null) {
				imageView.setImageDrawable(bitmap);
				return ;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				String tag = (String) imageView.getTag();
				if(tag != null  && message.obj != null){
					if( tag.equals(path) )
						imageView.setImageDrawable((Drawable) message.obj);
				}else{
					if(defaultDrawable !=null )
						imageView.setImageDrawable(defaultDrawable);
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadBitmapFromPath(path);
				imageCache.put(path, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return ;
	}

	
	public Drawable loadBitmapFromPath(String path)
	{
		/*
		if (path==null) return null;
		int division=1;
		if (path.endsWith("b.jpg"))
			division=3;
		return ImageUtil.getBitmapAsRoundCorner(path,division,5);
		*/
		Drawable photo=null;
		Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
		if (bitmapOrg!=null)
			photo=new BitmapDrawable(bitmapOrg);
//		else
//			photo=_context.getResources().getDrawable(R.drawable.bighead);
		return photo;
	}
	
	public Bitmap loadImageFromRes(int id) {
		Bitmap friendPhoto;
		
		friendPhoto = ImageUtil.toRoundCorner(
				ImageUtil.drawableToBitmap(_context.getResources().getDrawable(
						id)), 8);
		return friendPhoto;
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String path);
	}

}