package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.ByteOrder;
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
    private FloatBuffer matrix;


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

    }

    public final void setFoV(float fov)
    {
        this.fov = fov;
    }

    public final void setViewport(int width, int height)
    {
        aspectRatio = (float)width/(float)height;
    }

    public void translate(float x, float y, float z)
    {
        eye[0] += x;
        eye[1] += x;
        eye[2] += x;
    }

    /**
     * use negative factors for backwards translation
     * @param factor
     */
    public void translateForward(float factor)
    {
        eye[0] += factor*forward[0];
        eye[1] += factor*forward[1];
        eye[2] += factor*forward[2];
    }

    /**
     * use negative factors for downwards translation
     * @param factor
     */
    public void translateUp(float factor)
    {
        eye[0] += factor*up[0];
        eye[1] += factor*up[1];
        eye[2] += factor*up[2];
    }

    /**
     * use negative factors for translation to the left
     * @param factor
     */
    public void translateRight(float factor)
    {
        eye[0] += factor*right[0];
        eye[1] += factor*right[1];
        eye[2] += factor*right[2];
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
        Util.rotate(forward, up, angle);
        Util.rotate(right, up, angle);
    }

    /**
     * Rotates Camera around its side-pointing-vector
     * @param angle
     */
    public void rotateUpDown(float angle)
    {
        Util.rotate(forward, right, angle);
        Util.rotate(up, right, angle);
    }

    /**
     * Rotates Camera around its forward pointing vector
     * @param angle
     */
    public void rotateTheOtherWay(float angle)
    {
        Util.rotate(up, forward, angle);
        Util.rotate(right, forward, angle);
    }

    /**
     * prepares part of the gluLookAt-Matrix
     */
    public void makeGLUMatrix()
    {
        matrix.put(0, right[0]);
        matrix.put(4, right[1]);
        matrix.put(8, right[2]);

        matrix.put(1, up[0]);
        matrix.put(5, up[1]);
        matrix.put(9, up[2]);

        matrix.put(2, -forward[0]);
        matrix.put(6, -forward[1]);
        matrix.put(10, -forward[2]);
    }

    protected void makePerspectiveMatrix(float fovy, float aspectRatio, float zNear, float zFar)
    {
        float f = (float) Math.atan(fovy/2);
        matrix.put(0, f/aspectRatio);
        matrix.put(5, f);
        matrix.put(10, (zFar-zNear)/(zNear-zFar));
        matrix.put(11, (2*zFar*zNear)/(zNear-zFar));
        matrix.put(14, -1.0f);
    }

    protected void makeTranslationMatrix(float[] v)
    {
        matrix.put(3, matrix.get(3)+v[0]);
        matrix.put(7, matrix.get(7)+v[1]);
        matrix.put(10, matrix.get(10)+v[2]);
    }

    protected void makeRotationMatrix(float x, float y, float z)
    {

    }

    protected void makeMatrix()
    {
        makeIdentityMatrix();
        makePerspectiveMatrix(fov, aspectRatio, 0.01f, 5000.0f);
        makeGLUMatrix();
        makeTranslationMatrix(eye);

    }

    protected FloatBuffer getMatrix()
    {
        return this.matrix;
    }

    /**
     * resets the gluLookAt-Matrix to the identity matrix
     */
    protected final void makeIdentityMatrix()
    {
        matrix = BufferUtils.createFloatBuffer(16);
        int oldpos = matrix.position();
        matrix.put(Util.IDENTITY_MATRIX);
        matrix.position(oldpos);
    }

    ///**
    // * call Camera2::makeGLUMatrix() before!!!
    // * or at least, whenever something changes
    // */
    public void gluLookAt()
    {
        makeIdentityMatrix();
        makeGLUMatrix();
        glMultMatrixf(matrix);
        glTranslatef(-eye[0], -eye[1], -eye[2]);
    }

    public void applyMatrix(Shader s)
    {
        makeMatrix();
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(),false,getMatrix());
    }

}
