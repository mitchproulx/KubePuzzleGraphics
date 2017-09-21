package com.kube.kubepuzzletimer.presentation;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.kube.kubepuzzletimer.business.KubeRenderer;

public class PreviewActivity extends Activity {

    GLSurfaceView view;
    KubeRenderer renderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        renderer = new KubeRenderer();
        view = new GLSurfaceView(this);
        view.setRenderer(renderer);
        setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        float dx, dy;

        renderer.stopRotation();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                renderer.currX = x - renderer.mPreviousX;
                renderer.currY = y - renderer.mPreviousY;
                dx = renderer.currX;
                dy = renderer.currY;

                // reverse direction of rotation above the mid-line
                if (y > renderer.getHeight() / 2) {
                    dx = dx * -1 ;
                }
                // reverse direction of rotation to left of the mid-line
                if (x < renderer.getWidth() / 2) {
                    dy = dy * -1 ;
                }
                renderer.setAngle(renderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
        }
        renderer.mPreviousX = x;
        renderer.mPreviousY = y;
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

}

