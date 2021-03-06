package Renderables;

import OpenGL.Camera4SupportingShader;
import OpenGL.MapShader;
import OpenGL.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static UI.Logger.logOut;

/**
 * Created by Basti on 22.05.2016.
 */
public class Camera {
    /**
     * Relative Position Vector
     */
    private Vector3f pos;
    /**
     * Field of view in y-direction
     */
    private float fov;
    /**
     * Framebuffer aspect ratio
     */
    private float aspect;

    private Matrix4f matrix;
    private Vector3f up, posToCamera;
    private boolean angleChanged = true;
    private float direction, angle, cameraHeight = 5;

    public Camera() {
        pos = new Vector3f(0, 0, 0);
        fov = 45.0f;
        aspect = 1.0f;
        matrix = new Matrix4f();
    }

    public Matrix4f getMatrix() {
        //*
        if (angleChanged) {
            posToCamera = new Vector3f(
                    (float) ((cameraHeight * Math.sin(angle)) * (Math.cos(direction))),
                    (float) ((cameraHeight * Math.sin(angle)) * (Math.sin(direction))),
                    (float) (cameraHeight * Math.cos(angle))
            );
            up = new Vector3f(
                    (float) ((cameraHeight * Math.cos(angle)) * (Math.cos(direction + Math.PI))),
                    (float) ((cameraHeight * Math.cos(angle)) * (Math.sin(direction + Math.PI))),
                    (float) (cameraHeight * Math.sin(angle))
            );
            angleChanged = false;
        }

        matrix = new Matrix4f();
        matrix.perspective(fov, aspect, 1f, 500000);

        matrix.lookAlong(posToCamera, up);
        matrix.translate(pos.x + posToCamera.x, pos.y + posToCamera.y, pos.z + posToCamera.z);
        //matrix.translate(-posToCamera.x,-posToCamera.y,-posToCamera.z);
        //*/

        //matrix = new Matrix4f();
        //matrix.lookAlong(new Vector3f(0,0,10),new Vector3f(0,10,0));
        //matrix.translate(0,0,0);

        return matrix;
    }

    public void applyMatrix(Camera4SupportingShader s) {
        GL20.glUniformMatrix4fv(s.getCameraMatrixLocation(), false, getMatrix().get(BufferUtils.createFloatBuffer(16)));
    }

    /**
     * Converts clipspace coordinates to worldspace coordinates
     * @param clipx the x-value in clipspace (between -1 and 1)
     * @param clipy the y-value in clipspace (between -1 and 1)
     * @param depth the depthbuffer value at the screenspace coordinates corresponding to clipx and clipy (between 0 and 1)
     * @return worldspace coordinates
     */
    public Vector4f clipspaceToWorldspace(float clipx, float clipy, float depth)
    {
        Matrix4f m = getMatrix();
        m.invert();

        Vector4f v = new Vector4f(clipx, clipy, depth, 1.0f);
        v.mul(m);
        v.x /= v.w;
        v.y /= v.w;
        v.z /= v.w;
        v.w /= v.w;

        return v;
    }

    public void rotateCenter(double x, double y) {
        angle = (float) (angle + Math.toRadians(y));
        if (angle < 0) {
            angle = 0;
        }
        if (angle > Math.PI) {
            angle = (float) (Math.PI);
        }
        direction = (float) ((direction - Math.toRadians(x)) % (Math.PI * 2));

        angleChanged = true;
    }

    public void setViewport(int width, int height) {
        aspect = ((float) width) / ((float) height);
        System.out.println("Aspect: " + aspect);
    }

    public void zoom(double q) {
        cameraHeight = (float) (cameraHeight + q);
        if (cameraHeight < 1) {
            cameraHeight = 1;
        }
        angleChanged = true;
    }

    public void moveBy(Vector2f xandy) {
        float sin = (float) Math.sin(direction), cos = (float) Math.cos(direction);
        pos.x+=xandy.x*cos-xandy.y*sin;
        pos.y+=xandy.y*cos+xandy.x*sin;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getPos() {
        return pos;
    }
}
