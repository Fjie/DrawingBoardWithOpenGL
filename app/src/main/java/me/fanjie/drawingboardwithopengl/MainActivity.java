package me.fanjie.drawingboardwithopengl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (MyGLSurfaceView) findViewById(R.id.sv_gl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        surfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes x paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is x good place to re-allocate them.
        surfaceView.onResume();
    }

    public void addGap(View view) {
        surfaceView.myGLRenderer.shape.addGap();
    }

    public void removeOrthogonality(View view) {
        surfaceView.myGLRenderer.shape.removeOrthogonality();
    }

    public void recreate(View view) {
        surfaceView.myGLRenderer.shape.clear();
    }
}
