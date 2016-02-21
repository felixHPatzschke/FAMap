package Renderables;

import OpenGL.Matrix;
import OpenGL.Shader;
import org.lwjgl.opengl.GL20;

import java.util.Arrays;

/**
 * Created by Basti on 31.01.2016.
 */
public abstract class Moveable {

    float eyeX = 0, eyeY = 0, eyeZ = 0, translX = 0, translY = 0, translZ = 0, scaleX = 1, scaleY = 1, scaleZ = 1;
    boolean refresh=true;
    Matrix result = new Matrix();

    protected Matrix getMatrix() {
        if(refresh) {
            result = new Matrix();
            result = Matrix.rotate(result, eyeX, eyeY, eyeZ);
            result = Matrix.scale(result, scaleX, scaleY, scaleZ);
            result = Matrix.translate(result,translX,translY,translZ);
            refresh=false;
        }
        return result;
    }

    public void applyMatrix(Shader s){
        GL20.glUniformMatrix4fv(s.getObjectMatrixLocation(),false,getMatrix().getAsBuffer());
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
    public void setRotation(float x, float y, float z) {
        eyeX = x;
        eyeY = y;
        eyeZ = z;
        refresh=true;
    }

    public void addRotation(float x, float y, float z) {
        eyeX += x;
        eyeY += y;
        eyeZ += z;
        refresh=true;
    }

    public void setEyeX(float eyeX) {
        this.eyeX = eyeX;
        refresh=true;
    }

    public void setEyeY(float eyeY) {
        this.eyeY = eyeY;
        refresh=true;
    }

    public void setEyeZ(float eyeZ) {
        this.eyeZ = eyeZ;
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
