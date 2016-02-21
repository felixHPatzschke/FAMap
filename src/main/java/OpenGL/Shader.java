package OpenGL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

/**
 * Created by bhofmann on 23.09.2015.
 */
public class Shader {
    private int vertShader, fragShader, shaderProgram, objectMatrix, cameraMatrix;

    public void bind(){
        GL20.glUseProgram(shaderProgram);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public int getObjectMatrixLocation() {
        return objectMatrix;
    }

    public int getCameraMatrixLocation() {
        return cameraMatrix;
    }

    public Shader(String name){
        vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertShader, fileToString("/"+name+".vert"));
        GL20.glCompileShader(vertShader);

        fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragShader, fileToString("/"+name+".frag"));
        GL20.glCompileShader(fragShader);
        // =================== OpenGL.Shader Setup =====================

        // =================== OpenGL.Shader Check =====================
        int status = GL20.glGetShaderi(vertShader, GL20.GL_COMPILE_STATUS);
        if (status != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(vertShader));
        }

        int statusN = GL20.glGetShaderi(fragShader, GL20.GL_COMPILE_STATUS);
        if (statusN != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(fragShader));
        }
        // =================== OpenGL.Shader Check =====================

        // =================== OpenGL.Shader Program ===================
        shaderProgram = GL20.glCreateProgram();

        GL20.glAttachShader(shaderProgram, vertShader);
        GL20.glAttachShader(shaderProgram, fragShader);

        GL20.glBindAttribLocation(shaderProgram, 0, "position");
        GL20.glBindAttribLocation(shaderProgram, 1, "color");

        GL20.glLinkProgram(shaderProgram);

        GL20.glUseProgram(shaderProgram);

        objectMatrix =GL20.glGetUniformLocation(shaderProgram,"objectMatrix");
        cameraMatrix =GL20.glGetUniformLocation(shaderProgram,"cameraMatrix");

        GL20.glUseProgram(0);
    }

    private static String fileToString(String path) {
        try (
                InputStreamReader reader = new InputStreamReader(Shader.class.getResourceAsStream(path))
        ) {
            StringBuilder builder = new StringBuilder();

            char read;
            try {
                while ((read = (char) reader.read()) != (char) -1) {
                    builder.append(read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            reader.close();
            return builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void destroy(){
        GL20.glDeleteShader(vertShader);
        GL20.glDeleteShader(fragShader);
        GL20.glDeleteProgram(shaderProgram);
    }
}