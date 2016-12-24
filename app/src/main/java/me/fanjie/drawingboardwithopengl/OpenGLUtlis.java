package me.fanjie.drawingboardwithopengl;

import android.opengl.GLES20;

/**
 * Created by dell on 2016/12/13.
 */

public class OpenGLUtlis {

    public static int loadShader(int type, String shaderCode){

        // create x vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or x fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
