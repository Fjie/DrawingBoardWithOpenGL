package me.fanjie.drawingboardwithopengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.fanjie.drawingboardwithopengl.test.Panel;
import me.fanjie.drawingboardwithopengl.test.mapping.Mapper;

public class TestActivity extends AppCompatActivity {

    private Mapper mapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Panel panel = (Panel) findViewById(R.id.sv_gl);
        mapper = new Mapper(panel);
    }

    public void addGap(View view) {
        mapper.addGap();
    }

    public void removeOrthogonality(View view) {
        mapper.removeOrthogonality();
    }

    public void recreate(View view) {
        mapper.clear();
    }

    public void test(View view) {
        mapper.test();
    }
}
