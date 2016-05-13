package Renderables;


import OpenGL.Shader;
import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import static UI.Logger.*;

/**
 * Created by Felix Patzschke on 12.05.2016.
 */
public class Camera3 {

    /** Position Vector */
    Vector3d pos;
    /** Camera space y-Axis */
    Vector3d up;
    /** Camera space z-Axis */
    Vector3d fwd;
    /** Field of view in y-direction */
    float fov;
    /** Framebuffer aspect ratio */
    float aspect;

    Matrix4f p_matrix, v_matrix, matrix;
    boolean p_changed, v_changed;

    public Camera3()
    {
        pos = new Vector3d(0, 0, 2);
        up = new Vector3d(0, 1, 0);
        fwd = new Vector3d(0, 0, -1);
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
            logOut("Camera Matrix:", matrix);
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
        v_matrix = new Matrix4f(
                1.0f, 0.0f, 0.0f, (float)(-pos.x),
                0.0f, 1.0f, 0.0f, (float)(-pos.y),
                0.0f, 0.0f, 1.0f, (float)(-pos.z),
                0.0f, 0.0f, 0.0f, 1.0f
                );
        //v_matrix.translate((float)(-pos.x), (float)(-pos.x), (float)(-pos.x));
        v_matrix.lookAlong((float)fwd.x, (float)fwd.y, (float)fwd.z, (float)up.x, (float)up.y, (float)up.z);
        v_changed = false;
    }

    public void applyMatrix(Shader s)
    {
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(), false, getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }
    // </editor-fold>
    // <editor-fold desc="Rotation">
    public void pitch(double angle)
    {
        Matrix3d mat = new Matrix3d();
        Vector3d right = new Vector3d();
        fwd.cross(up, right);
        mat.rotation(angle, right);
        fwd = fwd.mul(mat);
        up = up.mul(mat);
        v_changed = true;
    }

    public void yaw(double angle)
    {
        Matrix3d mat = new Matrix3d();
        mat.rotation(angle, up);
        fwd = fwd.mul(mat);
        v_changed = true;
    }

    public void roll(double angle)
    {
        Matrix3d mat = new Matrix3d();
        mat.rotation(angle, fwd);
        up = up.mul(mat);
        v_changed = true;
    }

    public void rotateCenter(double x, double y)
    {
        Vector3d right = new Vector3d();
        fwd.cross(up, right);
        Matrix3d xrot = new Matrix3d(
                Math.cos(x), 0.0f, Math.sin(x),
                0.0f, 1.0f, 0.0f,
                -1.0*Math.sin(x), 0.0f, Math.cos(x));
        //xrot.rotation(x, up);
        logOut("xRot Matrix: ", xrot);
        Matrix3d yrot = new Matrix3d();
        yrot.rotation(y, right);
        logOut("old pos:", pos);
        pos.mul(xrot);
        logOut("new pos:", pos);
        pos = pos.mul(yrot);
        logOut("old fwd:", fwd);
        fwd.mul(xrot);
        logOut("new fwd:", fwd);
        fwd = fwd.mul(yrot);
        up = up.mul(yrot);
        v_changed = true;
    }
    // </editor-fold>
    // <editor-fold desc="Translation">
    public void translateForward(double q)
    {
        pos.add(fwd.x*q, fwd.y*q, fwd.z*q);
        v_changed = true;
    }

    public void translateRight(double q)
    {
        Vector3d right = new Vector3d();
        fwd.cross(up, right);
        pos.add(right.x*q, right.y*q, right.z*q);
        v_changed = true;
    }

    public void translateUp(double q)
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
