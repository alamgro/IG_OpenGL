package com.example.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Castillo_Ventanas {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "gl_Position =  vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    static float[] coordenadasVentana = {

            -0.72f, 0.25f, 0.0f, //0
            -0.72f, -0.25f, 0.0f, //1
            -0.15f, -0.25f, 0.0f, //2
            -0.15f, 0.25f, 0.0f, //3

            0.72f, 0.25f, 0.0f, //4
            0.72f, -0.25f, 0.0f, //5
            0.15f, -0.25f, 0.0f, //6
            0.15f, 0.25f, 0.0f, //7

            -0.44f, -0.5f, 0.0f, //8
            -0.44f, -1.0f, 0.0f, //9
            0.44f, -1.0f, 0.0f, //10
            0.44f, -0.5f, 0.0f, //11
    };

    //El órden en el que va a dibujar de vértice a vértice
    private short drawOrder[] = {
            0, 1, 2, 0, 2, 3, //Ventana Izq
            4, 5, 6, 4, 6, 7, //Ventana Der
            8, 9, 10, 8, 10, 11, //Puerta
    };

    float[] color = {0.2f, 0.2f, 0.1f, 1.0f};

    private final int mProgram;

    public Castillo_Ventanas() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coordenadasVentana.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(coordenadasVentana);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.LoadShaders(GLES20.GL_VERTEX_SHADER, vertexShaderCode); //Asignar shader para continuar
        int fragmentShader = MyGLRenderer.LoadShaders(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //Creamos un programa de OpenGL vacío
        mProgram = GLES20.glCreateProgram();

        //añadimos los vertex shader al programa con todos sus atributos
        GLES20.glAttachShader(mProgram, vertexShader);

        //añadimos los fragmentos shader al programa con todos sus atributos
        GLES20.glAttachShader(mProgram, fragmentShader);

        //Creamos ejecutable de OpenGL para poderlo ejecutar
        GLES20.glLinkProgram(mProgram);
    }

    private int positionHandle, colorHandle; //Posición y color a usar

    private final int vertexStride = COORDS_PER_VERTEX * 4; //Vertex stride

    public void draw() {
        //Añadir nuestro programa al entorno de OpenGL
        GLES20.glUseProgram(mProgram);

        //Obtener el identificador del miembro vPosition del sombreado de vértices
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
