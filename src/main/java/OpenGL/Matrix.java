package OpenGL;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 * Created by Game on 02.01.2016.
 */
public class Matrix {
    float[][] matrix = new float[4][4];

    public static Matrix getPerspectiveMatrix(float fovy, float aspect, float near, float far) {
        float f = (float) Math.atan(fovy / 2);
        float[][] matrix = new float[][]{
                {f / aspect, 0, 0, 0},
                {0, f, 0, 0},
                {0, 0, ((far + near)) / (near - far), (2 * far * near) / (near - far)},
                {0, 0, -1, 0}
        };
        return new Matrix(matrix);
    }

    public static Matrix getOrthographicMatrix(float width, float height, float zNear, float zFar) {
        float[][] matrix = new float[][]{
                {1 / width, 0, 0, 0},
                {0, 1 / height, 0, 0},
                {0, 0, -(2 / (zFar - zNear)), (zFar + zNear) / (zFar - zNear)},
                {0, 0, 0, 1}
        };
        return new Matrix(matrix);
    }

    public Matrix() {
        matrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }
    
    public Matrix(float[][] matrix) {
        this.matrix = matrix;
    }

    public static Matrix translate(Matrix in, float translX, float translY, float translZ) {
        in.matrix[0][3] += translX;
        in.matrix[1][3] += translY;
        in.matrix[2][3] += translZ;
        return in;
    }

    public static Matrix scale(Matrix in, float x, float y, float z) {
        Matrix m = new Matrix(new float[][]{
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        });
        return in.multiply(m);
    }

    public static Matrix rotate(Matrix result, float eyeX, float eyeY, float eyeZ) {
        Matrix normal = new Matrix();

        result = result.multiply(normal.rotateX(eyeX));
        result = result.multiply(normal.rotateY(eyeY));
        result = result.multiply(normal.rotateZ(eyeZ));

        return result;
    }

    public Matrix rotateX(float angle) {
        double rangle = Math.toRadians(angle);

        float sin = (float) Math.sin(rangle);
        float cos = (float) Math.cos(rangle);

        Matrix m = new Matrix(new float[][]{
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        });

        return this.multiply(m);
    }

    public Matrix rotateY(float angle) {
        double rangle = Math.toRadians(angle);

        float sin = (float) Math.sin(rangle);
        float cos = (float) Math.cos(rangle);

        Matrix m = new Matrix(new float[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
        return this.multiply(m);
    }

    public Matrix rotateZ(float angle) {
        double rangle = Math.toRadians(angle);

        float sin = (float) Math.sin(rangle);
        float cos = (float) Math.cos(rangle);

        Matrix m = new Matrix(new float[][]{
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return this.multiply(m);
    }

    public FloatBuffer getAsBuffer() {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        for (int i = 0; i < 4; i++) {
            fb.put(matrix[i]);
        }
        fb.flip();
        return fb;
    }

    public float[][] getAsArray() {
        return matrix;
    }

    public Matrix multiply(Matrix v2) {
        float[][] result = new float[4][4];

        float[][] matrix1 = this.getAsArray(), matrix2 = v2.getAsArray();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }

        return new Matrix(result);
    }
}
