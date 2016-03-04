package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.Arrays;

import static UI.Logger.logOut;

/**
 * Created by Dev on 17.02.2016.
 */
public class Camera extends Moveable {

    private int width=800,height=600;

    @Override
    public void applyMatrix(Shader s){
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(),false,getMatrix().getAsBuffer());
    }

    public void setViewport(int width,int height){
        GL11.glViewport(0, 0, width, height);
        this.width=width;
        this.height=height;
    }

    protected Matrix getMatrix() {
        if(refresh) {
            result = Matrix.getPerspectiveMatrix(60,width/height,0.01f,5000);
            Matrix model = Matrix.scale(new Matrix(), scaleX, scaleY, scaleZ);
            model = Matrix.rotate(model, eyeX, eyeY, eyeZ);
            model = Matrix.translate(model,-translX,-translY,-translZ);
            result=result.multiply(model);
            logOut(Arrays.deepToString(result.getAsArray()));
            refresh=false;
        }
        return result;
    }
}
