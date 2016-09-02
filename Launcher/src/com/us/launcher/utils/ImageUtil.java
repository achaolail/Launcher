package com.us.launcher.utils;


import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {   
       
    public static Bitmap drawableToBitmap(Drawable drawable) {   
  
        Bitmap bitmap = Bitmap   
                .createBitmap(   
                        drawable.getIntrinsicWidth(),   
                        drawable.getIntrinsicHeight(),   
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);   
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),   
                drawable.getIntrinsicHeight());   
        drawable.draw(canvas);   
        return bitmap;   
    }   
       
    public static Bitmap toGrayscale(BitmapDrawable bm1) {   
        int width, height;   
        Bitmap bitmap = bm1.getBitmap(); 
        height = bitmap.getHeight();   
        width = bitmap.getWidth();       
   
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);   
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm); 
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
    }   
           
    public static BitmapDrawable toGrayscale(BitmapDrawable bm1, int pixels) {   
        return  new BitmapDrawable(toRoundCorner(toGrayscale(bm1), pixels));   
    }   

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {   
   
    	try{
    		int w=bitmap.getWidth();
    		int h=bitmap.getHeight();
	        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);   
	   
	        final int color = 0xff424242;   
	        final Paint paint = new Paint();   
	        final Rect rect = new Rect(0, 0, w, h);   
	        final RectF rectF = new RectF(rect);   
	        final float roundPx = pixels;   
	   
	        paint.setAntiAlias(true);   
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);   
	   
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));   
	        canvas.drawBitmap(bitmap, rect, rect, paint); 
	   
	        return output;
    	}catch(OutOfMemoryError e)
    	{
    		return bitmap;
    	}
    }   
    
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {   
        Bitmap bitmap = bitmapDrawable.getBitmap();   
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));   
        return bitmapDrawable;   
    }
    
    public static BitmapDrawable toRoundCorner2(Bitmap bitmap, int pixels) {   
    	BitmapDrawable bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));   
        return bitmapDrawable;   
    }
    
    public static Drawable loadBitmapSafe(String photoPath, int divid)
	{
    	Drawable photo=null;
    	try{
    		Bitmap bitmapOrg=null;
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options(); 
			bitmapOptions.inSampleSize=divid;
			bitmapOptions.inPurgeable=true; 
			bitmapOrg = BitmapFactory.decodeFile(photoPath, bitmapOptions);
			if (bitmapOrg!=null && bitmapOrg.getHeight()>0)
				photo=new BitmapDrawable(bitmapOrg);
    	}catch(Exception e){
    	}catch(OutOfMemoryError e){
    		System.gc();
			System.gc();
    	}
    	return photo;
	}
    
    public static Bitmap loadBitmapSafe(int divid, String photoPath)
	{
    	Bitmap bitmapOrg=null;
    	try{
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options(); 
			bitmapOptions.inSampleSize=divid;
			bitmapOptions.inPurgeable=true; 
			bitmapOrg = BitmapFactory.decodeFile(photoPath, bitmapOptions);
    	}catch(Exception e){
    	}catch(OutOfMemoryError e){
    		System.gc();
			System.gc();
    	}
    	return bitmapOrg;
	}
    
    public static Bitmap loadBitmapSafeWithAdaptiveDivision(int divid, String photoPath)
	{
    	Bitmap bitmapOrg=null;
    	try{
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inPurgeable=true;
			bitmapOptions.inJustDecodeBounds=true;
			BitmapFactory.decodeFile(photoPath, bitmapOptions);
			if (bitmapOptions.outHeight>640 || bitmapOptions.outWidth>640)
				divid*=2;
			
			bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize=divid;
			bitmapOptions.inPurgeable=true;
			bitmapOrg = BitmapFactory.decodeFile(photoPath, bitmapOptions);
    	}catch(Exception e){
    	}catch(OutOfMemoryError e){
    		System.gc();
			System.gc();
    	}
    	return bitmapOrg;
	}
    
    static public Drawable getBitmapAsRoundCorner(String path, int division, int radius)
	{
		Bitmap photo = ImageUtil.loadBitmapSafe(division, path);//alec
		if (photo!=null) return ImageUtil.toRoundCorner2(photo, radius);
		return null;
	}
    
    static public Bitmap getBitmapAsRoundCorner(int division, int radius, String path)
	{
		Bitmap photo = ImageUtil.loadBitmapSafe(division, path);//alec
		if (photo!=null) return ImageUtil.toRoundCorner(photo, radius);
		return null;
	}
    
    static public Bitmap getBitmapAsRoundCornerWithAdaptiveDivision(int division, int radius, String path)
	{
		Bitmap photo = ImageUtil.loadBitmapSafeWithAdaptiveDivision(division, path);//alec
		if (photo!=null) return ImageUtil.toRoundCorner(photo, radius);
		return null;
	}
    
    static public Bitmap makeFriendPhotoAsRoundCorner(String photoPath){
    	return null;
    }
    
    public static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
    
    
    
    static public Bitmap combineImages(Context context, Bitmap base, Bitmap top, boolean center) { 
    	Bitmap cs = null; 

        int width, height = 0; 

        if (base.getWidth() > top.getWidth()) { 
        	width = base.getWidth();
        	height = base.getHeight(); 
        }else{ 
        	width = top.getWidth();
        	height = top.getHeight(); 
        } 

        cs = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs); 
        Rect src=new Rect(0,0,top.getWidth(),top.getHeight());
        Rect dst;
        if (center)
        {
        	int s=(int)((float)width/5);
            dst=new Rect((width-s)/2,(height-s)/2,(width-s)/2+s,(height-s)/2+s);
        }
        else{
        	int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        	int s=(int)(60f/screenWidth*width);
            dst=new Rect(0,height-s,s,height);
        }

        comboImage.drawBitmap(base, 0f, 0f, null); 
        comboImage.drawBitmap(top, src, dst, null);

        return cs; 
 	}
    /** 
     *  处理图片  
     * @param bm 所要转换的bitmap 
     * @param newWidth新的宽 
     * @param newHeight新的高   
     * @return 指定宽高的bitmap 
     */ 
     public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){   
        // 获得图片的宽高   
        int width = bm.getWidth();   
        int height = bm.getHeight();   
        // 计算缩放比例   
        float scaleWidth = ((float) newWidth) / width;   
        float scaleHeight = ((float) newHeight) / height;   
        // 取得想要缩放的matrix参数   
        Matrix matrix = new Matrix();   
        matrix.postScale(scaleWidth, scaleHeight);   
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
        return newbm;   
    }  
}