package com.doc.launcher.utils;


import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;


public class MyDate {

    private static OkHttpClient okHttpClient;
    private static String utc;

    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date(System.currentTimeMillis()));
        return date;// 2012年10月03日 23:41:31  
    }

    public static String getDateEN() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31  
    }

    public static void setSystemTime() {
        Log.d("HWH AireApp 设置系统时间");
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://php.xingfafa.com.cn/onair/gettime.php").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    utc = new JSONObject(result).optString("UTC");
                    Log.d("HWH AireApp 时间 MyDate:" + utc);

                    Request request02 = new Request.Builder().url("http://ip-api.com/json/wikipedia.org?fields=timezone").build();
                    Call newCall = okHttpClient.newCall(request02);
                    newCall.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Process process = Runtime.getRuntime().exec("su");
                                        SimpleDateFormat dspFmt = new SimpleDateFormat("yyyyMMdd.HHmmss");
                                        long aLong = Long.parseLong(utc);

                                        DataOutputStream os = new DataOutputStream(process.getOutputStream());
                                        os.writeBytes("setprop persist.sys.timezone " + "Asia/Shanghai" + "\n");
                                        Log.d("HWH AireApp 时间 aLong:" + aLong + " 当前时间:" + System.currentTimeMillis());

                                        String datetime = dspFmt.format(aLong * 1000);
                                        Log.d("HWH AireApp 时间 datetime: utc " + datetime);

                                        dspFmt.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                                        Date parse = null;
                                        try {
                                            parse = dspFmt.parse(datetime);
                                        } catch (ParseException e1) {
                                            Log.e("解析时间出错!");
                                            e1.printStackTrace();
                                            parse = new Date();

                                        }
                                        dspFmt.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                                        datetime = dspFmt.format(parse.getTime());
                                        Log.d("HWH AireApp 时间 datetime: " + datetime);

                                        os.writeBytes("/system/bin/date -s " + datetime + "\n");
                                        os.writeBytes("clock -w\n");
                                        os.writeBytes("exit\n");
                                        os.flush();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String timezone = new JSONObject(response.body().string()).optString("timezone");
                                        Process process = Runtime.getRuntime().exec("su");
                                        SimpleDateFormat dspFmt = new SimpleDateFormat("yyyyMMdd.HHmmss");
                                        Log.d("HWH AireApp 时间    " + timezone);

                                        long aLong = Long.parseLong(utc);

                                        DataOutputStream os = new DataOutputStream(process.getOutputStream());
                                        os.writeBytes("setprop persist.sys.timezone " + timezone + "\n");
                                        String datetime = dspFmt.format(aLong * 1000);
                                        Log.d("HWH AireApp 时间 datetime: utc " + datetime);

                                        dspFmt.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                                        Date parse = null;

                                        try {
                                            parse = dspFmt.parse(datetime);
                                        } catch (ParseException e1) {
                                            Log.e("解析时间出错!");
                                            e1.printStackTrace();
                                            parse = new Date();

                                        }

                                        dspFmt.setTimeZone(TimeZone.getTimeZone(timezone));
                                        dspFmt.format(parse.getTime());

                                        Log.d("HWH AireApp 时间 datetime: " + datetime);

                                        os.writeBytes("/system/bin/date -s " + datetime + "\n");
                                        os.writeBytes("clock -w\n");
                                        os.writeBytes("exit\n");
                                        os.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

}  