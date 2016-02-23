package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

/**
 * Created by felix on 23.02.2016.
 *
 * A Moveable2 contains a new coodinate system
 */
public abstract class Moveable2 {

    /**
     * The new coordinate system's base point
     */
    protected float[] position = new float[3];
    /**
     * a vector pointing "to the right"
     * the new x-axis
     */
    protected float[] right = new float[3];
    /**
     * a vector pointing "up"
     * the new y-axis
     */
    protected float[] up = new float[3];
    /**
     * a vector pointing "forwards"
     * the new z-axis
     */
    protected float[] forward = new float[3];
    /**
     *
     */
    protected Matrix matrix = new Matrix();
    /**
     * Indicates whether something has changed and the matrix has therefore to be recalculated
     */
    protected boolean changed = false;


    // <editor-fold desc="Matrix Production">
    public final void matrix_load_identity()
    {
        matrix = new Matrix();
    }

    public final Matrix matrix_load_translation(Matrix in)
    {
        in.multiply(new Matrix(new float[][]{
                {1.0f, 0.0f, 0.0f, position[0]},
                {0.0f, 1.0f, 0.0f, position[1]},
                {0.0f, 0.0f, 1.0f, position[2]},
                {0.0f, 0.0f, 0.0f, 1.0f},
        }));
        return in;
    }

    public final Matrix matrix_load_rotation(Matrix in)
    {
        in.multiply(new Matrix(new float[][]{
                {right[0], up[0], -forward[0], 0.0f},
                {right[1], up[1], -forward[1], 0.0f},
                {right[2], up[2], -forward[2], 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f}
        }));
        return in;
    }

    public abstract void matrix_compute();
    // </editor-fold>

    // <editor-fold desc="Translation">
    public final void translate(float[] v)
    {
        position[0] += v[0];
        position[1] += v[1];
        position[2] += v[2];
        changed = true;
    }

    public final void translate_reset()
    {
        position[0] = 0.0f;
        position[1] = 0.0f;
        position[2] = 0.0f;
        changed = true;
    }

    public final void translate(float x, float y, float z)
    {
        position[0] += x;
        position[1] += y;
        position[2] += z;
        changed = true;
    }

    public void translate_right(float r)
    {
        position[0] += r*right[0];
        position[1] += r*right[1];
        position[2] += r*right[2];
        changed = true;
    }

    public void translate_up(float r)
    {
        position[0] += r*up[0];
        position[1] += r*up[1];
        position[2] += r*up[2];
        changed = true;
    }

    public void translate_forward(float r)
    {
        position[0] += r*forward[0];
        position[1] += r*forward[1];
        position[2] += r*forward[2];
        changed = true;
    }

    public void translate_x(float r)
    {
        position[0] += r;
        changed = true;
    }

    public void translate_y(float r)
    {
        position[1] += r;
        changed = true;
    }

    public void translate_z(float r)
    {
        position[2] += r;
        changed = true;
    }
    // </editor-fold>

    // <editor-fold desc="Rotation">

    /**
     * Rotates around a given axis.
     * WARNING! AXIS WILL BE NORMALIZED!
     * @param angle
     * @param axis
     */
    public final void rotate(float angle, float[] axis)
    {
        Util.normalize(axis);
        Util.rotate(up, axis, angle);
        Util.rotate(right, axis, angle);
        Util.rotate(forward, axis, angle);
        changed = true;
    }

    public final void rotate_reset()
    {
        right = new float[]{1.0f, 0.0f, 0.0f};
        up = new float[]{0.0f, 1.0f, 0.0f};
        forward = new float[]{0.0f, 0.0f, 1.0f};
        changed = true;
    }

    /**
     * Rotates around a given axis
     * @param angle
     * @param axisx
     * @param axisy
     * @param axisz
     */
    public final void rotate(float angle, float axisx, float axisy, float axisz)
    {
        float[] axis = new float[]{axisx, axisy, axisz};
        Util.normalize(axis);
        Util.rotate(up, axis, angle);
        Util.rotate(right, axis, angle);
        Util.rotate(forward, axis, angle);
        changed = true;
    }

    /**
     * Rotates around "up"-vector
     * (within the x-z-plane)
     * @param angle
     */
    public void rotate_left_right(float angle)
    {
        Util.rotate(right, up, angle);
        Util.rotate(forward, up, angle);
        changed = true;
    }

    /**
     * Rotates around side-pointing-vector
     * (within the y-z-plane)
     * @param angle
     */
    public void rotate_up_down(float angle)
    {
        Util.rotate(up, right, angle);
        Util.rotate(forward, right, angle);
        changed = true;
    }

    /**
     * Rotates around forward pointing vector
     * (within the x-y-plane)
     * @param angle
     */
    public void rotate_the_other_way(float angle)
    {
        Util.rotate(right, forward, angle);
        Util.rotate(up, forward, angle);
        changed = true;
    }


    // </editor-fold>

    // <editor-fold desc="Matrix Getter functions">
    public final void matrix_update()
    {
        if(changed)
        {
            matrix_compute();
            changed = false;
        }
    }

    public final float[][] get_matrix_as_array()
    {
        return matrix.getAsArray();
    }

    public final FloatBuffer get_matrix_as_buffer()
    {
        return matrix.getAsBuffer();
    }

    public final Matrix get_matrix()
    {
        return matrix;
    }
    // </editor-fold>

    /**
     * As argument use Shader::get******MatrixLocation()
     * @param location
     */
    public final void matrix_apply(int location)
    {
        GL20.glUniformMatrix4fv(location, false, get_matrix_as_buffer());
    }

}
