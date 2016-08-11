package Renderables;

import UI.GLContextThread;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by felix on 23.02.2016.
 */
public class CoordSystem extends Renderable {

    int vbo, cbo, vao, ibo;

    public CoordSystem() throws Exception {

        ibo = GL15.glGenBuffers();
        GLContextThread.checkError();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GLContextThread.checkError();
        ShortBuffer indices = BufferUtils.createShortBuffer(6);
        indices.put(new short[]{
                0, 1,
                2, 3,
                4, 5
        });
        indices.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLContextThread.checkError();

        IntBuffer vertices = BufferUtils.createIntBuffer(18);
        vertices.put(new int[]{
                -1, 0, 0,
                1, 0, 0,
                0, -1, 0,
                0, 1, 0,
                0, 0, -1,
                0, 0, 1
        });
        vertices.flip();

        ByteBuffer colors = BufferUtils.createByteBuffer(18);
        colors.put(new byte[]{
                (byte)0, 0, 0,
                (byte)255, 0, 0,
                0, (byte)0, 0,
                0, (byte)255, 0,
                0, 0, (byte)0,
                0, 0, (byte)255
        });
        colors.flip();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        GLContextThread.checkError();

        vbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_INT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GLContextThread.checkError();

        cbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_UNSIGNED_BYTE, true, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        GLContextThread.checkError();
    }

    @Override
    public void render(Camera camera) {
        GL30.glBindVertexArray(vao);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL11.glDrawElements(GL11.GL_LINES, 6, GL11.GL_UNSIGNED_SHORT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }

    @Override
    public void remove() {

    }
}
