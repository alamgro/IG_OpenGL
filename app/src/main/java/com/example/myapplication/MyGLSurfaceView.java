package com.example.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;
    private final float TOUCH_SCALE_FACTOR = 180f / 320f;
    private float previousX;
    private float previousY;

    //Context lo necesita android para configuraciones
    public MyGLSurfaceView  (Context context){
        super(context);
        setEGLContextClientVersion(2);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        renderer = new MyGLRenderer();
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //return super.onTouchEvent(event);
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;

                //Girar la figura al lado contrario en Y
                if(y > getHeight() / 2f)
                    dx *= (-1);
                //Girar la figura al lado contrario en X
                if(x < getWidth() / 2f)
                    dy *= (-1);

                renderer.setAngle(renderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
                break;
        }

        previousX = x;
        previousY = y;

        return true;
    }
}
