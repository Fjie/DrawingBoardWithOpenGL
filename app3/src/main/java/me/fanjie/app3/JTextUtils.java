package me.fanjie.app3;

import android.text.TextUtils;

/**
 * Created by dell on 2017/2/13.
 */

public class JTextUtils {

    public static boolean isEmpty(String...args){
        for (String str : args){
            if (TextUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }
}
