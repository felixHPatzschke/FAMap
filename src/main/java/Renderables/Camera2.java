package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by felix on 23.02.2016.
 *
 * Complete rewrite of a Camera class
 *
 * And yes, I don't care, if you've done it before.
 */
public class Camera2 extends Moveable2{

    /**
     * field of view
     */
    float fov;
    /**
     * framebuffer aspect ratio
     */
    float aspectRatio;


    public Camera2(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz, float fov, int width, int height)
    {
        position = new float[3];
        forward = new float[3];
        right = new float[3];
        up = new float[3];
        set(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
        setFoV(fov);
        setViewport(width, height);
        matrix_update();
    }

    public Camera2()
    {
        position = new float[3];
        forward = new float[3];
        right = new float[3];
        up = new float[3];
        set(0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        setFoV(60.0f);
        setViewport(1, 1);
        matrix_update();
    }


    /**
     * Takes the typical gluLookAt arguments
     * @param eyex
     * @param eyey
     * @param eyez
     * @param centerx
     * @param centery
     * @param centerz
     * @param upx
     * @param upy
     * @param upz
     */
    public final void set(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz)
    {
        position[0] = centerx;
        position[1] = centery;
        position[2] = centerz;

        forward[0] = centerx - eyex;
        forward[1] = centery - eyey;
        forward[2] = centerz - eyez;

        up[0] = upx;
        up[1] = upy;
        up[2] = upz;

        Util.normalize(forward);

        Util.cross(forward, up, right);
        Util.normalize(right);

        Util.cross(right, forward, up);

    }

    public final void setFoV(float fov)
    {
        this.fov = fov;
    }

    public final void setViewport(int width, int height)
    {
        aspectRatio = (float)width/(float)height;
    }

    @Override
    public void matrix_compute() {
        matrix = matrix_load_perspective_projection(matrix);
        matrix = matrix_load_translation(matrix);
        matrix = matrix_load_rotation(matrix);
    }

    protected Matrix matrix_load_perspective_projection(Matrix in)
    {
        float f = (float) Math.atan(fov/2);

        float[][] matrix = {
                {f/aspectRatio, 0.0f, 0.0f, 0.0f},
                {0.0f, f, 0.0f, 0.0f},
                {0.0f, 0.0f, (5000.01f)/(-4999.99f), (2*50.0f)/(4999.99f)},
                {0.0f, 0.0f, -1.0f, 0.0f},
        };
        return new Matrix(matrix);
    }

}
