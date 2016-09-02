package com.doc.launcher.utils;

//airecenter
public class Log {
	 public static boolean enDEBUG = false;
//	public static boolean enDEBUG = true;

	static public void d(String Msg) {
		if (enDEBUG) {
			android.util.Log.d("syso", Msg);
		}
	}

	static public void i(String Msg) {
		if (enDEBUG) {
			android.util.Log.i("syso", Msg);
		}
	}

	static public void e(String Msg) {
		if (enDEBUG) {
			android.util.Log.e("syso", Msg);
		}
	}

	static public void w(String Msg) {
		if (enDEBUG)
			android.util.Log.w("syso", Msg);
	}
}
