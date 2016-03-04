import OpenGL.Matrix;
import org.junit.Test;

import java.nio.FloatBuffer;

import static org.junit.Assert.*;

/**
 * Created by Game on 02.01.2016.
 */
public class ViewMatrixTest {
    @Test
    public void testMultiply(){
        Matrix vm1 = new Matrix();
        Matrix vm2 = new Matrix();
        assertArrayEquals(new Matrix().getAsArray(),vm1.multiply(vm2).getAsArray());
    }

    @Test
    public void testMultiplyAdv(){
        Matrix vm1 = new Matrix(new float[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}});
        Matrix vm2 = new Matrix(new float[][]{{3,2,1,0},{2,1,0,3},{1,0,3,2},{0,3,2,1}});
        assertArrayEquals(new float[][]{{10,16,18,16},{34,40,42,40},{58,64,66,64},{82,88,90,88}},vm1.multiply(vm2).getAsArray());
    }

    @Test
    public void testAsBuffer(){
        Matrix matrix = new Matrix();
        FloatBuffer fb = matrix.getAsBuffer();
        float[] test={
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        };
        for (int i = 0; i < 16 ; i++) {
            if(fb.get(i)!=test[i]){
                fail("Failed at location "+i+", "+fb.get(i)+"!="+test[i]);
            }
        }
    }

    @Test
    public void testEquals(){
        assertTrue(new Matrix().equals(new Matrix()));
    }
}