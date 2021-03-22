package com.example.myapplication;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class AMoverElCubo {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private final String vertexShaderCode =
            /*"attribute vec4 vPosition;" +
                    "void main() {" +
                    "gl_Position =  vPosition;" +
                    "}";*/
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;
    static float[] coordenadasCubo = {
            //Cordenadas de frente
            0.5f, 0.5f, 0.0f, //0 - Arriba - Derecha
            -0.5f, 0.5f, 0.0f, //1 - Arriba - Izquierda
            -0.5f, -0.5f, 0.0f, //2 - Abajo - Izquierda
            0.5f, -0.5f, 0.0f, //3 - Abajo - Derecha

            //Cordenadas de atrás
            0.5f, 0.5f, -1.0f, //4 - Arriba - Derecha
            -0.5f, 0.5f, -1.0f, //5 - Arriba - Izquierda
            -0.5f, -0.5f, -1.0f, //6 - Abajo - Izquierda
            0.5f, -0.5f, -1.0f //7 - Abajo - Derecha
    };

    private short drawOrder[] = { 0, 1, 2, 3, 0, 4, 5, 6, 7, 4, 5, 1, 2, 6, 7, 3 }; //El órden en el que va a dibujar de vértice a vértice

    float[] color = {0.4f, 0.0f, 0.6f, 1.0f};

    private final int mProgram;

    //Constructor para cuando crea el objeto
    public AMoverElCubo() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coordenadasCubo.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(coordenadasCubo);
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
    private int vPMatrixHandle; //Es para inicializar la matriz

    private final int vertexCount = coordenadasCubo.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; //Vertex stride

    public void draw(){
        //Añadir nuestro programa al entorno de OpenGL
        GLES20.glUseProgram(mProgram);

        //Obtener el identificador del miembro vPosition del sombreado de vértices
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer );
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount); //PARA EL CUADRADO NECESITAREMOS FAN

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public void draw(float[] mvpMatrix) {
        //Añadir nuestro programa al entorno de OpenGL
        GLES20.glUseProgram(mProgram);

        //Obtener el identificador del miembro vPosition del sombreado de vértices
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer );
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        //Aplicamos la matriz mvp a nuestro programa
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        //se aplican los valores de la proyeccion a la vista multiple del cel
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_LINE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
