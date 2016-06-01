package OpenGL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

/**
 * Created by bhofmann on 23.09.2015.
 */
public class Shader extends AbstractShader{
    private int objectMatrix, cameraMatrix, transparency;

    public int getObjectMatrixLocation() {
        return objectMatrix;
    }

    public int getCameraMatrixLocation() {
        return cameraMatrix;
    }

    public int getTransparencyLocation(){
        return transparency;
    }

    public Shader(String name){
        vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertShader, fileToString("/"+name+".vert"));
        GL20.glCompileShader(vertShader);

        fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragShader, fileToString("/"+name+".frag"));
        GL20.glCompileShader(fragShader);


        int status = GL20.glGetShaderi(vertShader, GL20.GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(vertShader));
        }

        int statusN = GL20.glGetShaderi(fragShader, GL20.GL_COMPILE_STATUS);
        if (statusN != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(fragShader));
        }


        shaderProgram = GL20.glCreateProgram();

        GL20.glAttachShader(shaderProgram, vertShader);
        GL20.glAttachShader(shaderProgram, fragShader);

        GL20.glBindAttribLocation(shaderProgram, 0, "position");
        GL20.glBindAttribLocation(shaderProgram, 1, "color");

        GL20.glLinkProgram(shaderProgram);

        GL20.glUseProgram(shaderProgram);

        objectMatrix = GL20.glGetUniformLocation(shaderProgram,"objectMatrix");
        cameraMatrix = GL20.glGetUniformLocation(shaderProgram,"cameraMatrix");
        transparency = GL20.glGetUniformLocation(shaderProgram,"transparency");

        GL20.glUseProgram(0);
    }

}