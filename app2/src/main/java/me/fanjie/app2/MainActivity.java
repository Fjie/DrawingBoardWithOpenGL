package me.fanjie.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyGLSurface surface = (MyGLSurface) findViewById(R.id.surface);
        renderer = new MyRenderer(surface);
    }

    public void test01(View view) {
        renderer.test();
    }

    public void test02(View view) {
        renderer.addGap();
    }

    public void test03(View view) {
        renderer.removeOrthogonality();
    }

    public void test04(View view) {
        renderer.clear();
    }
}
