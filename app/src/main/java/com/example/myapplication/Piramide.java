package com.example.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Piramide {
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
    static float[] coordenadasPiramide = {
            -0.7f, -0.5f, -0.5f, //0
            0.25f, -0.75f, 0.5f, //1
            0.75f, -0.4f, -0.5f, //2
            0.0f, 0.5f, 0.0f, //3
    };

    //El órden en el que va a dibujar de vértice a vértice
    private short drawOrder[] = {0, 1, 2, 0, 3, 1, 3, 2};

    float[] color = {0.2f, 0.2f, 0.1f, 1.0f};

    private final int mProgram;

    public Piramide() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coordenadasPiramide.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(coordenadasPiramide);
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

        GLES20.glDrawElements(GLES20.GL_LINE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
