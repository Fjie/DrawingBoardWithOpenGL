package me.fanjie.app3;

import android.util.Log;

/**
 * Created by dell on 2017/2/8. 基础日志类
 */

public class JLog {
    public static void d(Object obj){
        Log.d(Thread.currentThread().getStackTrace()[3].toString(),String.valueOf(obj));
    }
    public static void e(Object obj){
        Log.e(Thread.currentThread().getStackTrace()[3].toString(),String.valueOf(obj));
    }
    public static void v(Object obj){
        Log.v(Thread.currentThread().getStackTrace()[3].toString(),String.valueOf(obj));
    }
    public static void w(Object obj){
        Log.w(Thread.currentThread().getStackTrace()[3].toString(),String.valueOf(obj));
    }
    public static void i(Object obj){
        Log.i(Thread.currentThread().getStackTrace()[3].toString(),String.valueOf(obj));
    }

}
