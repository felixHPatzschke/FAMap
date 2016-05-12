package Renderables;

/**
 * Created by felix on 23.02.2016.
 *
 * Like Batman's Utility Belt for linear algebra
 */
public abstract class Util {

    /**
     * identity matrix
     */
    public static final float[] IDENTITY_MATRIX = {1.0f, 0.0f, 0.0f, 0.0f,
                                                   0.0f, 1.0f, 0.0f, 0.0f,
                                                   0.0f, 0.0f, 1.0f, 0.0f,
                                                   0.0f, 0.0f, 0.0f, 1.0f};


    /**
     * normalizes vectors
     * (who would have thought?)
     *
     * @param v vector to be normalized
     * @return normalized vector
     */
    public static final float[] normalizef(float[] v)
    {
        float r = (float) Math.sqrt( v[0]*v[0] + v[1]*v[1] + v[2]*v[2] );

        if(r==0.0f)
        {
            return v;
        }

        r = 1.0f/r;

        v[0] *= r;
        v[1] *= r;
        v[2] *= r;

        return v;
    }

    /**
     * calculates vector product
     *
     * @param a first operand
     * @param b second operand
     * @param res matrix
     */
    public static final void crossf(float[] a, float[] b, float[] res)
    {
        res[0] = a[1]*b[2] - a[2]*b[1];
        res[1] = a[2]*b[0] - a[0]*b[2];
        res[2] = a[0]*b[1] - a[1]*b[0];
    }

    /**
     * rotates a vector around a given axis.
     * @param vec vector to be rotated
     * @param axis normalized axis vector
     * @param angle angle
     */
    public static final void rotate(float[] vec, float[] axis, float angle)
    {
        float sin = (float)Math.sin((double) angle);
        float cos = (float)Math.cos((double) angle);

        float[][] matrix = new float[3][3];

        matrix[0][0] = (axis[0]*axis[0]*(1-cos)) + cos;
        matrix[0][1] = (axis[0]*axis[1]*(1-cos)) + axis[2]*sin;
        matrix[0][2] = (axis[0]*axis[2]*(1-cos)) + axis[1]*sin;

        matrix[1][0] = (axis[1]*axis[0]*(1-cos)) + axis[2]*sin;
        matrix[1][1] = (axis[1]*axis[1]*(1-cos)) + cos;
        matrix[1][2] = (axis[1]*axis[2]*(1-cos)) + axis[0]*sin;

        matrix[2][0] = (axis[2]*axis[0]*(1-cos)) + axis[1]*sin;
        matrix[2][1] = (axis[2]*axis[1]*(1-cos)) + axis[0]*sin;
        matrix[2][2] = (axis[2]*axis[2]*(1-cos)) + cos;

        float newVec[] = new float[3];
        newVec[0] = vec[0]*matrix[0][0] + vec[1]*matrix[0][1] + vec[2]*matrix[0][2];
        newVec[1] = vec[0]*matrix[1][0] + vec[1]*matrix[1][1] + vec[2]*matrix[1][2];
        newVec[2] = vec[0]*matrix[2][0] + vec[1]*matrix[2][1] + vec[2]*matrix[2][2];

        vec[0] = newVec[0];
        vec[1] = newVec[1];
        vec[2] = newVec[2];
    }

}
