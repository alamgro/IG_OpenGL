package com.example.myapplication;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20; //Compatibilidad con GL 2.0
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;


public class MyGLRenderer implements GLSurfaceView.Renderer {
    public volatile float mAngle;

    private Triangulo m_triangulo;
    private Cuadrado m_cuadrado;
    private Poligono m_poligono;
    private TriangulosLocos m_trianguLocos;
    private Castillo m_castillo;
    private Castillo_Ventanas m_ventanas;
    private Piramide m_piramide;
    private AMoverElCubo m_cubo;

    //Variables para la MVP
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16]; //Matriz de proyeccion
    private final float[] viewMatrix = new float[16]; //Matriz de vista
    private float[] rotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1f);
        m_triangulo = new Triangulo();
        m_cuadrado = new Cuadrado();
        m_poligono = new Poligono();
        m_trianguLocos = new TriangulosLocos();
        m_castillo = new Castillo();
        m_ventanas = new Castillo_Ventanas();
        m_piramide = new Piramide();
        m_cubo = new AMoverElCubo();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        //long time = SystemClock.uptimeMillis() % 4000L;
        //float angle = 0.09f * ((int) time);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //Posición de la cámara para la matriz de proyeccion
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //aplicamos la matriz de rotacion
        Matrix.setRotateM(rotationMatrix, 0, mAngle, 1, 0, -1.0f);
        //Se hace la convolucion de la proyeccion y la vista (Y forman la MVP)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        m_cubo.draw(scratch);

        //m_cuadrado.draw();
        //m_poligono.draw();
        //m_trianguLocos.draw();
        //m_castillo.draw();
        //m_ventanas.draw();
        //m_piramide.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0, width, height);

        //Aplicación de la proyección a las coordenadas del objeto en el metodo onSurfaceChanged()
        //convolucion
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 7); //Es el área que se delimita para que no se deforme
    }

    public static int LoadShaders(int _type, String _shaderCode){
        int shader = GLES20.glCreateShader(_type); //Crear el shader
        GLES20.glShaderSource(shader, _shaderCode); //Decirle el source de nuestro shader
        GLES20.glCompileShader(shader); //Agregar todo_ a nuestro código para compilar
        return shader;
    }

    public float getAngle(){
        return mAngle;
    }

    public void setAngle(float _angle){
        mAngle = _angle;
    }

}



