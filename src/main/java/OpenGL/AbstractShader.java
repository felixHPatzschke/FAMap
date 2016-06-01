package OpenGL;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Felix Patzschke on 01.06.2016.
 */
public abstract class AbstractShader {

    protected int fragShader, vertShader, shaderProgram;


    public void bind()
    {
        glUseProgram(shaderProgram);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    protected static final String fileToString(String path) {
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
        glDeleteShader(vertShader);
        glDeleteShader(fragShader);
        glDeleteProgram(shaderProgram);
    }

}
