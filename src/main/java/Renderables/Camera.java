package Renderables;

import OpenGL.Shader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static UI.Logger.logOut;

/**
 * Created by Dev on 17.02.2016.
 */
public class Camera extends Moveable {

    private int width=800,height=600;

    @Override
    public void applyMatrix(Shader s){
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(),false,getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }

    public void setViewport(int width,int height){
        GL11.glViewport(0,0,width, height);
        this.width=width;
        this.height=height;
    }

    @Override
    protected Matrix4f getMatrix() {
        if(refresh) {
            matrix = new Matrix4f();

            matrix.rotate(-eyeZ,0,0,1);
            matrix.rotate(-eyeY,0,1,0);
            matrix.rotate(-eyeX,1,0,0);
            matrix.translate(-translX,-translY,-translZ);
            matrix.perspective(60,width/height,0.1f,5000f);

            logOut("C:",matrix.toString());
            refresh=false;
        }
        return matrix;
    }
}