package Renderables;

import OpenGL.Matrix;

/**
 * Created by felix on 23.02.2016.
 */
public abstract class Scalable extends Moveable2 {

    float[] scale = new float[3];


    public final Matrix matrix_load_scale(Matrix in)
    {
        in.multiply(new Matrix(new float[][]{
                {scale[0], 0.0f, 0.0f, 0.0f},
                {0.0f, scale[1], 0.0f, 0.0f},
                {0.0f, 0.0f, scale[2], 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f},
        }));
        return in;
    }


    public final void scale(float r)
    {
        scale[0] *= r;
        scale[1] *= r;
        scale[2] *= r;
    }

    public final void scale_normalize()
    {
        scale[0] = 1.0f;
        scale[1] = 1.0f;
        scale[2] = 1.0f;
    }

    public final void scale(float x, float y, float z)
    {
        scale[0] *= x;
        scale[1] *= y;
        scale[2] *= z;
    }

}
