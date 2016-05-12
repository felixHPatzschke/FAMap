import UI.Logger;
import org.joml.Matrix4f;
import org.junit.Test;

/**
 * Created by Basti on 12.05.2016.
 */
public class CameraTest {
    @Test
    public void testCamera(){
        Matrix4f m = new Matrix4f();
        System.out.println(m.toString());
        m.translate(0,0,-10);
        m.rotateZ(30);
        m.rotateY(10);
        m.rotateX(-20);
        System.out.println(m.toString());

        m = new Matrix4f();
        m.rotateZ(30);
        m.rotateY(10);
        m.rotateX(-20);
        m.translate(0,0,-10);
        System.out.println(m.toString());
    }
}