package Renderables;

import OpenGL.Shader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import static UI.Logger.logOut;

/**
 * Created by Basti on 31.01.2016.
 */
public abstract class Moveable {

    float eyeX = 0, eyeY = 0, eyeZ = 0, translX = 0, translY = 0, translZ = 0, scaleX = 1, scaleY = 1, scaleZ = 1;
    boolean refresh=true;
    Matrix4f matrix = new Matrix4f();

    protected Matrix4f getMatrix() {
        if(refresh) {
            matrix = new Matrix4f();

            matrix.scale(scaleX,scaleY,scaleZ);
            //matrix.translate(translX,translY,translZ);
            //matrix.rotate(eyeX,1,0,0);
            //matrix.rotate(eyeY,0,1,0);
            //matrix.rotate(eyeZ,0,0,1);

            logOut("O:",matrix.toString());
            refresh=false;
        }
        return matrix;
    }

    public void applyMatrix(Shader s){
        GL20.glUniformMatrix4fv(s.getObjectMatrixLocation(),false,getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }

    //<editor-fold desc="Translation">
    public void setTranslation(float translX, float translY, float translZ) {
        this.translX = translX;
        this.translY = translY;
        this.translZ = translZ;
        refresh=true;
    }

    public void addTranslation(float translX, float translY, float translZ) {
        this.translX += translX;
        this.translY += translY;
        this.translZ += translZ;
        refresh=true;
    }

    public void setTranslationX(float translX) {
        this.translX = translX;
        refresh=true;
    }

    public void setTranslationY(float translY) {
        this.translY = translY;
        refresh=true;
    }

    public void setTranslationZ(float translZ) {
        this.translZ = translZ;
        refresh=true;
    }

    public float getTranslationX() {
        return translX;
    }

    public float getTranslationY() {
        return translY;
    }

    public float getTranslationZ() {
        return translZ;
    }
    //</editor-fold>

    //<editor-fold desc="Rotation">
    public void setRotationDeg(float x, float y, float z) {
        eyeX = (float) Math.toRadians(x);
        eyeY = (float) Math.toRadians(y);
        eyeZ = (float) Math.toRadians(z);
        refresh=true;
    }

    public void addRotationDeg(float x, float y, float z) {
        eyeX += Math.toRadians(x);
        eyeY += Math.toRadians(y);
        eyeZ += Math.toRadians(z);
        refresh=true;
    }

    public void setEyeXDeg(float eyeX) {
        this.eyeX = (float) Math.toRadians(eyeX);
        refresh=true;
    }

    public void setEyeYDeg(float eyeY) {
        this.eyeY = (float) Math.toRadians(eyeY);
        refresh=true;
    }

    public void setEyeZDeg(float eyeZ) {
        this.eyeZ = (float) Math.toRadians(eyeZ);
        refresh=true;
    }

    public float getEyeX() {
        return eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public float getEyeZ() {
        return eyeZ;
    }
    //</editor-fold>

    //<editor-fold desc="Scale">
    public void setScale(float scaleFactor) {
        scaleX = scaleFactor;
        scaleY = scaleFactor;
        scaleZ = scaleFactor;
        refresh=true;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        refresh=true;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        refresh=true;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        refresh=true;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
        refresh=true;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }
    //</editor-fold>
}
