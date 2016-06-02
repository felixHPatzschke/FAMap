package OpenGL;

import FAProps.FAMap;
import MapRenderers.MapRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.*;

/**
 * Created by Basti on 03.01.2016.
 */
public class Tile {

    private static final short REAL_SIZE = 256;
    public static final short SIZE = REAL_SIZE - 1;
    public static final int INDEX_BUFFER_SIZE = (2 * (REAL_SIZE) * (REAL_SIZE)) - 4;
    private int minHeight, maxHeight;
    private int vbo, vao;

    public static int generateIndexBuffer() {
        ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(INDEX_BUFFER_SIZE);

        int i, j = 0;
        for (i = 0; i < REAL_SIZE - 1; i++) {
            if (i != 0 && j > 0) {
                indicesBuffer.put((short) ((i - 1) * REAL_SIZE + j));
            }
            for (j = 0; j < REAL_SIZE; j++) {
                indicesBuffer.put((short) (i * REAL_SIZE + j));
                indicesBuffer.put((short) ((i + 1) * REAL_SIZE + j));
            }
            if (i != REAL_SIZE - 2) {
                indicesBuffer.put((short) ((i + 1) * REAL_SIZE + j - 1));
            }
        }

        indicesBuffer.flip();

        int iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return iboId;
    }

    public Tile(FAMap map, int x, int y) {
        maxHeight = Integer.MIN_VALUE;
        minHeight = Integer.MAX_VALUE;
        ShortBuffer vertexBuffer = BufferUtils.createShortBuffer(REAL_SIZE * REAL_SIZE * 3);
        int[][] heightmap = map.getMapDetails().getHeightmap();
        int height;
        for (int offsetY = SIZE * y; offsetY < (y * SIZE) + REAL_SIZE; offsetY++) {
            for (int offsetX = SIZE * x; offsetX < (x * SIZE) + REAL_SIZE; offsetX++) {
                vertexBuffer.put((short) offsetX);
                vertexBuffer.put((short) offsetY);
                try {
                    height = heightmap[offsetX][offsetY];
                    vertexBuffer.put((short) height);
                    if (height < minHeight) {
                        minHeight = height;
                    }
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    vertexBuffer.put((short) 0);
                }
            }
        }

        vertexBuffer.flip();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_UNSIGNED_SHORT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);
    }

    public void bind() {
        GL30.glBindVertexArray(vao);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void delete() {
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}