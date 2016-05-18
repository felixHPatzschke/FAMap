package Renderables;


import OpenGL.Shader;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import static UI.Logger.*;

/**
 * Created by Felix Patzschke on 12.05.2016.
 */
public class Camera3 {

    /** Relative Position Vector */
    private Vector3d pos;
    /** Worldspace translation */
    private Vector3d transl;
    /** Camera space y-Axis */
    private Vector3d up;
    /** Camera space z-Axis */
    private Vector3d fwd;
    /** Field of view in y-direction */
    private float fov;
    /** Framebuffer aspect ratio */
    private float aspect;

    private Matrix4f p_matrix, v_matrix, t_matrix, matrix;
    private boolean p_changed, v_changed, t_changed;

    public Camera3()
    {
        pos = new Vector3d(0, 2, 0);
        transl = new Vector3d(0.5, 0, 0);
        up = new Vector3d(0, 0, 1);
        fwd = new Vector3d(0, -1, 0);
        fov = 45.0f;
        aspect = 1.0f;
        matrix = new Matrix4f();

        p_changed = true;
        v_changed = true;
        t_changed = true;
    }

    // <editor-fold desc="Matrix">
    public Matrix4f getMatrix()
    {
        if(p_changed || v_changed || t_changed)
        {
            if(p_changed)
                makeProjectionMatrix();
            if(v_changed)
                makeViewMatrix();
            if(t_changed)
                makeTranslationMatrix();
            matrix = new Matrix4f();
            //matrix.mul(p_matrix);
            matrix.mul(v_matrix);
            logOut("View Matrix:", v_matrix);
            matrix.mul(t_matrix);
            logOut("Translation Matrix:", t_matrix);
            logOut("Camera Matrix:", matrix);
        }
        return matrix;
    }

    protected void makeProjectionMatrix()
    {
        p_matrix = new Matrix4f();
        p_matrix.perspective((float)(fov*Math.PI/180.0), aspect, 0.1f, 100.0f);
        p_changed = false;
    }

    protected void makeViewMatrix()
    {
        v_matrix = new Matrix4f();
        //        1.0f, 0.0f, 0.0f, (float)(-pos.x),
        //        0.0f, 1.0f, 0.0f, (float)(-pos.y),
        //        0.0f, 0.0f, 1.0f, (float)(-pos.z),
        //        0.0f, 0.0f, 0.0f, 1.0f
        //);
        v_matrix.lookAlong((float)fwd.x, (float)fwd.y, (float)fwd.z, 0.0f, 0.0f, 1.0f);
        //v_matrix.lookAt((float)pos.x, (float)pos.x, (float)pos.x, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        //v_matrix.translate((float)(-pos.x), (float)(-pos.x), (float)(-pos.x));
        v_changed = false;
    }

    protected void makeTranslationMatrix()
    {
        t_matrix = new Matrix4f(
                1.0f, 0.0f, 0.0f, (float)(transl.x),
                0.0f, 1.0f, 0.0f, (float)(transl.y),
                0.0f, 0.0f, 1.0f, (float)(transl.z),
                0.0f, 0.0f, 0.0f, 1.0f
        );
        //t_matrix = new Matrix4f(
        //        1.0f, 0.0f, 0.0f, (float)(pos.x),
        //        0.0f, 1.0f, 0.0f, (float)(pos.y),
        //        0.0f, 0.0f, 1.0f, (float)(pos.z),
        //        0.0f, 0.0f, 0.0f, 1.0f
        //);
        //t_matrix.translate((float)(-pos.x), (float)(-pos.y), (float)(-pos.z));
        t_matrix.transpose();
        t_changed = false;
    }

    public void applyMatrix(Shader s)
    {
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(), false, getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }
    // </editor-fold>
    // <editor-fold desc="Rotation">
    @Deprecated
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

    @Deprecated
    public void yaw(double angle)
    {
        Matrix3d mat = new Matrix3d();
        mat.rotation(angle, up);
        fwd = fwd.mul(mat);
        v_changed = true;
    }

    @Deprecated
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
        right.normalize();
        Matrix3d xrot = new Matrix3d(
                Math.cos(x), Math.sin(x), 0.0,
                -1.0*Math.sin(x), Math.cos(x), 0.0,
                0.0, 0.0, 1.0);
        //logOut("xRot Matrix: ", xrot);
        Matrix3d yrot = new Matrix3d();// = Util.rotationMatrix(right, y);
        yrot.rotation(y, right);
        //logOut("old pos:", pos);
        pos.mul(xrot);
        pos.mul(yrot);
        //pos.normalize();
        //logOut("new pos:", pos);
        //logOut("old fwd:", fwd);
        //fwd.mul(xrot);
        //fwd.mul(yrot);
        pos.mul(-1.0, fwd);
        fwd.normalize();
        //logOut("new fwd:", fwd);
        v_changed = true;
    }
    // </editor-fold>
    // <editor-fold desc="Translation">
    public void translateForward(double q)
    {
        transl.add(fwd.x*q, fwd.y*q, 0.0);
        t_changed = true;
    }

    public void translateRight(double q)
    {
        Vector3d right = new Vector3d();
        fwd.cross(up, right);
        right.normalize();
        transl.add(right.x*q, right.y*q, 0.0);
        t_changed = true;
    }

    public void translateUp(double q)
    {
        transl.add(0.0, 0.0, q);
        t_changed = true;
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

    public void zoom(double q)
    {
        pos.mul(1.0+q);
        v_changed = true;
    }
    // </editor-fold>

}