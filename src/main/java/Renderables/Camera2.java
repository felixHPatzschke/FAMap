package Renderables;

import OpenGL.Shader;
import UI.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.joml.*;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by felix on 23.02.2016.
 *
 * Complete rewrite of a Camera class
 *
 * And yes, I don't care, if you've done it before.
 */
public class Camera2 {

    /**
     * coordinates of the camera in space
     */
    float[] eye;
    /**
     * normalized "forward" vector
     */
    float[] forward;
    /**
     * normalized vector pointing to the right side of the camera
     */
    float[] right;
    /**
     * normalized "up" vector
     */
    float[] up;
    /**
     * field of view
     */
    float fov;
    /**
     * framebuffer aspect ratio
     */
    float aspectRatio;
    /**
     * lookat matrix
     */
    private Matrix4f v_matrix, p_matrix, matrix;

    boolean p_change, v_change;


    public Camera2(float eyex, float eyey, float eyez, float centerx, float centery, float centerz, float upx, float upy, float upz, float fov, int width, int height)
    {
        eye = new float[3];
        forward = new float[3];
        right = new float[3];
        up = new float[3];
        set(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
        setFoV(fov);
        setViewport(width, height);
        makeMatrix();
    }

    public Camera2()
    {
        eye = new float[3];
        forward = new float[3];
        right = new float[3];
        up = new float[3];
        set(0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        setFoV(60.0f);
        setViewport(1, 1);
        makeMatrix();
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
        forward[0] = centerx - eyex;
        forward[1] = centery - eyey;
        forward[2] = centerz - eyez;

        up[0] = upx;
        up[1] = upy;
        up[2] = upz;

        Util.normalizef(forward);

        Util.crossf(forward, up, right);
        Util.normalizef(right);

        Util.crossf(right, forward, up);
        v_change = true;
    }

    public final void setFoV(float fov)
    {
        Logger.logOutI("P changed");
        this.fov = fov;
        p_change = true;
    }

    public final float getFoV()
    {
        Logger.logOutI("P changed");
        return this.fov;
    }

    public final void setViewport(int width, int height)
    {
        Logger.logOutI("P changed");
        aspectRatio = (float)width/(float)height;
        p_change = true;
    }

    public void translate(float x, float y, float z)
    {
        eye[0] += x;
        eye[1] += y;
        eye[2] += z;
    }

    /**
     * use negative factors for backwards translation
     * @param factor
     */
    public void translateForward(float factor)
    {
        Logger.logOutI("V changed");
        eye[0] += factor*forward[0];
        eye[1] += factor*forward[1];
        eye[2] += factor*forward[2];
        v_change = true;
    }

    /**
     * use negative factors for downwards translation
     * @param factor
     */
    public void translateUp(float factor)
    {
        Logger.logOutI("V changed");
        eye[0] += factor*up[0];
        eye[1] += factor*up[1];
        eye[2] += factor*up[2];
        v_change = true;
    }

    /**
     * use negative factors for translation to the left
     * @param factor
     */
    public void translateRight(float factor)
    {
        Logger.logOutI("V changed");
        eye[0] += factor*right[0];
        eye[1] += factor*right[1];
        eye[2] += factor*right[2];
        v_change = true;
    }


    public void rotate(float axisx, float axisy, float axisz, float angle)
    {
        float[] axis = new float[3];
        axis[0] = axisx;
        axis[1] = axisy;
        axis[2] = axisz;

        Util.normalizef(axis);

        Util.rotate(up, axis, angle);
        Util.rotate(forward, axis, angle);
        Util.rotate(right, axis, angle);
    }

    /**
     * WARNING: AXIS VECTOR WILL BE NORMALIZED
     * @param axis
     * @param angle
     */
    public void rotate(float[] axis, float angle)
    {
        Util.normalizef(axis);

        Util.rotate(up, axis, angle);
        Util.rotate(forward, axis, angle);
        Util.rotate(right, axis, angle);
    }

    /**
     * Rotates Camera around its "up" vector
     * @param angle
     */
    public void rotateLeftRight(float angle)
    {
        Logger.logOutI("V changed");
        Util.rotate(forward, up, angle);
        Util.rotate(right, up, angle);
        v_change = true;
    }

    /**
     * Rotates Camera around its side-pointing-vector
     * @param angle
     */
    public void rotateUpDown(float angle)
    {
        Logger.logOutI("V changed");
        Util.rotate(forward, right, angle);
        Util.rotate(up, right, angle);
        v_change = true;
    }

    /**
     * Rotates Camera around its forward pointing vector
     * @param angle
     */
    public void rotateTheOtherWay(float angle)
    {
        Util.rotate(up, forward, angle);
        Util.rotate(right, forward, angle);
        v_change = true;
    }

    protected void makePerspectiveMatrix()
    {
        p_matrix = new Matrix4f();
        //p_matrix.perspective(fov, aspectRatio, 0.1f, 5000.0f);
        Logger.logOut("Perspective Matrix:", p_matrix);
        p_change = false;
    }

    protected void makeViewMatrix()
    {
        v_matrix = new Matrix4f();
        v_matrix.lookAt(eye[0], eye[1], eye[2], eye[0]+forward[0], eye[1]+forward[1], eye[2]+forward[2], up[0], up[1], up[2]);
        Logger.logOut("View Matrix:", v_matrix);
        v_change = false;
    }

    protected void makeMatrix()
    {
        if(p_change || v_change)
        {
            matrix = new Matrix4f();
            if(p_change)
                makePerspectiveMatrix();
            if(v_change)
                makeViewMatrix();
            //matrix = p_matrix.mul(v_matrix);
            matrix = v_matrix.mul(p_matrix);  // or the other way around, i'm not sure...
            Logger.logOut("Camera Matrix: ", matrix);
        }
    }

    public Matrix4f getMatrix()
    {
        makeMatrix();
        return matrix;
    }

    public void applyMatrix(Shader s)
    {
        makeMatrix();
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(), false, getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }

}
