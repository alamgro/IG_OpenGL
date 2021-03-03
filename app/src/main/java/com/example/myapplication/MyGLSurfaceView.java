package com.example.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    //Context lo necesita android para configuraciones
    public MyGLSurfaceView  (Context context){
        super(context);
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer();
        setRenderer(renderer);
    }

}
