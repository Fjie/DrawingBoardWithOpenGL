package me.fanjie.app2;

import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by dell on 2016/12/19.
 */

public class ToastUtils {
    public static void showToast(String toast){
        if(!TextUtils.isEmpty(toast)){
            Toast.makeText(App.getInstance(),toast,Toast.LENGTH_SHORT).show();
        }
    }
}
