package Renderables;


import OpenGL.Shader;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

/**
 * Created by Felix Patzschke on 12.05.2016.
 */
public class Camera3 {

    /** Position Vector */
    Vector3f pos;
    /** Camera space y-Axis */
    Vector3f up;
    /** Camera space z-Axis */
    Vector3f fwd;
    /** Field of view in y-direction */
    float fov;
    /** Framebuffer aspect ratio */
    float aspect;

    Matrix4f p_matrix, v_matrix, matrix;
    boolean p_changed, v_changed;

    public Camera3()
    {
        pos = new Vector3f(0, 0, 1);
        up = new Vector3f(0, 1, 0);
        fwd = new Vector3f(0, 0, -1);
        fov = 45;
        aspect = 1;
        matrix = new Matrix4f();

        p_changed = true;
        v_changed = true;
    }

    // <editor-fold desc="Matrix">
    public Matrix4f getMatrix()
    {
        if(p_changed || v_changed)
        {
            if(p_changed)
                makeProjectionMatrix();
            if(v_changed)
                makeViewMatrix();
            matrix = p_matrix.mul(v_matrix);
        }
        return matrix;
    }

    protected void makeProjectionMatrix()
    {
        p_matrix = new Matrix4f();
        // TODO projection matrix
        p_changed = false;
    }

    protected void makeViewMatrix()
    {
        v_matrix = new Matrix4f();
        v_matrix.translate(pos.negate());
        v_matrix.lookAlong(fwd, up);
        v_changed = false;
    }

    public void applyMatrix(Shader s)
    {
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(), false, getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }
    // </editor-fold>
    // <editor-fold desc="Rotation">
    public void pitch(float angle)
    {
        Matrix3f mat = new Matrix3f();
        Vector3f right = new Vector3f();
        fwd.cross(up, right);
        mat.rotation(angle, right);
        fwd = fwd.mul(mat);
        up = up.mul(mat);
        v_changed = true;
    }

    public void yaw(float angle)
    {
        Matrix3f mat = new Matrix3f();
        mat.rotation(angle, up);
        fwd = fwd.mul(mat);
        v_changed = true;
    }

    public void roll(float angle)
    {
        Matrix3f mat = new Matrix3f();
        mat.rotation(angle, fwd);
        up = up.mul(mat);
        v_changed = true;
    }

    public void rotateCenter(float x, float y)
    {
        Vector3f right = new Vector3f();
        fwd.cross(up, right);
        Matrix3f xrot = new Matrix3f();
        xrot.rotation(x, up);
        Matrix3f yrot = new Matrix3f();
        yrot.rotation(y, right);
        pos = pos.mul(xrot);
        pos = pos.mul(yrot);
        fwd = fwd.mul(xrot);
        fwd = fwd.mul(yrot);
        up = up.mul(yrot);
        v_changed = true;
    }
    // </editor-fold>
    // <editor-fold desc="Translation">
    public void translateForward(float q)
    {
        pos.add(fwd.x*q, fwd.y*q, fwd.z*q);
        v_changed = true;
    }

    public void translateRight(float q)
    {
        Vector3f right = new Vector3f();
        fwd.cross(up, right);
        pos.add(right.x*q, right.y*q, right.z*q);
        v_changed = true;
    }

    public void translateUp(float q)
    {
        pos.add(up.x*q, up.y*q, up.z*q);
        v_changed = true;
    }
    // </editor-fold>
    // <editor-fold desc="Projection">
    public void setFoV(float fov)
    {
        this.fov = fov;
        p_changed = true;
    }

    public void setViewport(int width, int height)
    {
        aspect = (float)width/(float)height;
        p_changed = true;
    }
    // </editor-fold>

}
