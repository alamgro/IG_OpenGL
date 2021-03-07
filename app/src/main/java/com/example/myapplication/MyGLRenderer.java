package com.example.myapplication;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20; //Compatibilidad con GL 2.0
import android.opengl.GLSurfaceView;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangulo m_triangulo;
    private Cuadrado m_cuadrado;
    private Poligono m_poligono;
    private TriangulosLocos m_trianguLocos;
    private Castillo m_castillo;
    private Castillo_Ventanas m_ventanas;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1f);
        m_triangulo = new Triangulo();
        m_cuadrado = new Cuadrado();
        m_poligono = new Poligono();
        m_trianguLocos = new TriangulosLocos();
        m_castillo = new Castillo();
        m_ventanas = new Castillo_Ventanas();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //m_triangulo.draw();
        //m_cuadrado.draw();
        //m_poligono.draw();
        //m_trianguLocos.draw();
        m_castillo.draw();
        m_ventanas.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0, width, height);
    }

    public static int LoadShaders(int _type, String _shaderCode){
        int shader = GLES20.glCreateShader(_type); //Crear el shader
        GLES20.glShaderSource(shader, _shaderCode); //Decirle el source de nuestro shader
        GLES20.glCompileShader(shader); //Agregar todo_ a nuestro código para compilar
        return shader;
    }

}



