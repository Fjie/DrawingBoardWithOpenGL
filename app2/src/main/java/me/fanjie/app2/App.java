package me.fanjie.app2;

import android.app.Application;

/**
 * Created by dell on 2016/12/19.
 */

public class App extends Application {
    private static App app;

    public static App getInstance() {
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

}
