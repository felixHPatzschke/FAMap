package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

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

    public Matrix getMatrix() {
        if(refresh) {
            result = Matrix.getPerspectiveMatrix(60,width/height,0.01f,5000);
            result = Matrix.translate(result,translX,translY,translZ);
            result = Matrix.rotate(result, eyeX, eyeY, eyeZ);
            //result = Matrix.scale(result, scaleX, scaleY, scaleZ);

            refresh=false;
        }
        return result;
    }
}
