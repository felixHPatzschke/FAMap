package OpenGL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Felix Patzschke on 01.06.2016.
 */
public class MapShader extends AbstractShader implements Camera4SupportingShader{

    /** Vertex shader uniforms */
    private int objectMatrix, cameraMatrix;
    /** Fragment shader uniforms */
    private int fragEnum, toolx, tooly, toolrsq, hmin, hmax, transparency, campos, lightpos, fog_dist;
    /** Fragment shader macros */
    public static final int FRAG_NONE = 0;
    public static final int FRAG_DEFAULT = 1;
    public static final int FRAG_HEIGHTMAP = 2;
    public static final int FRAG_WATER = 4;
    public static final int FRAG_HEIGHT_LEVEL_LINES = 8;
    public static final int FRAG_TOOL_BLOB = 16;
    public static final int FRAG_FOG = 128;
    public static final int FRAG_DYNAMICLIGHTING = 256;

    public MapShader()
    {
        int status;

        vertShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertShader, fileToString("/map.vert"));
        glCompileShader(vertShader);

        status = glGetShaderi(vertShader, GL_COMPILE_STATUS);
        if(status != GL_TRUE)
            throw new RuntimeException(glGetShaderInfoLog(vertShader));

        fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, fileToString("/map.frag"));
        glCompileShader(fragShader);

        status = glGetShaderi(fragShader, GL_COMPILE_STATUS);
        if(status != GL_TRUE)
            throw new RuntimeException(glGetShaderInfoLog(fragShader));

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertShader);
        glAttachShader(shaderProgram, fragShader);
        glBindAttribLocation(shaderProgram, 0, "position");
        glBindAttribLocation(shaderProgram, 1, "color");
        glLinkProgram(shaderProgram);
        glUseProgram(shaderProgram);
        objectMatrix = glGetUniformLocation(shaderProgram, "objectMatrix");
        cameraMatrix = glGetUniformLocation(shaderProgram, "cameraMatrix");
        fragEnum = glGetUniformLocation(shaderProgram, "frag_enum");
        toolx = glGetUniformLocation(shaderProgram, "tool_x");
        tooly = glGetUniformLocation(shaderProgram, "tool_y");
        toolrsq = glGetUniformLocation(shaderProgram, "tool_r_sqr");
        hmin = glGetUniformLocation(shaderProgram, "h_min");
        hmax = glGetUniformLocation(shaderProgram, "h_max");
        transparency = glGetUniformLocation(shaderProgram, "transp");
        campos = glGetUniformLocation(shaderProgram, "u_campos");
        lightpos = glGetUniformLocation(shaderProgram, "lightpos");
        fog_dist = glGetUniformLocation(shaderProgram, "fog_distance");
        glUseProgram(0);
    }


    public int getObjectMatrixLocation() {
        return objectMatrix;
    }

    @Override
    public int getCameraMatrixLocation() {
        return cameraMatrix;
    }

    public int getFragEnumLocation() {
        return fragEnum;
    }

    public int getToolXLocation() {
        return toolx;
    }

    public int getToolYLocation() {
        return tooly;
    }

    public int getToolRSQRLocation() {
        return toolrsq;
    }

    public int getHminLocation() {
        return hmin;
    }

    public int getHmaxLocation() {
        return hmax;
    }

    public int getTransparencyLocation() {
        return transparency;
    }

    public int getCameraPositionLocation() {
        return campos;
    }

    public int getLightPositionLocation() {
        return lightpos;
    }

    public int getFogDistanceLocation() {
        return fog_dist;
    }

}
