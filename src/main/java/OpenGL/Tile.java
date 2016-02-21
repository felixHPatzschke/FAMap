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

    private static final int REAL_SIZE = 256;
    public static final int SIZE=REAL_SIZE-1;
    public static final int INDEX_BUFFER_SIZE=(2*(REAL_SIZE)*(REAL_SIZE))-4;
    private int vbo,cbo,vao;

    public static int generateIndexBuffer(){
        ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(INDEX_BUFFER_SIZE);

        int i,j=0;
        for (i = 0; i < REAL_SIZE-1; i++) {
            if(i!=0&&j>0){
                indicesBuffer.put((short) ((i-1)* REAL_SIZE +j));
            }
            for (j = 0; j < REAL_SIZE; j++) {
                indicesBuffer.put((short) (i* REAL_SIZE +j));
                indicesBuffer.put((short) ((i+1)* REAL_SIZE +j));
            }
            if(i!= REAL_SIZE -2){
                indicesBuffer.put((short) ((i+1)* REAL_SIZE +j-1));
            }
        }

        indicesBuffer.flip();

        int iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        return iboId;
    }

    public Tile(FAMap map, MapRenderer renderer,int x,int y){

        IntBuffer vertexBuffer = BufferUtils.createIntBuffer(REAL_SIZE * REAL_SIZE *3);
        ByteBuffer colorBuffer = BufferUtils.createByteBuffer(REAL_SIZE * REAL_SIZE *3);
        int[][] heightmap = map.getMapDetails().getHeightmap();

        for (int offsetY = SIZE *y; offsetY < (y * SIZE)+REAL_SIZE; offsetY++) {
            for (int offsetX = SIZE *x; offsetX < (x * SIZE)+REAL_SIZE; offsetX++) {
                vertexBuffer.put(offsetX);
                vertexBuffer.put(offsetY);
                try {
                    vertexBuffer.put(heightmap[offsetX][offsetY]);
                    //vertexBuffer.put(0);
                    MapColor color = renderer.getColorAt(map, offsetX, offsetY);
                    colorBuffer.put(color.toRGBArray());
                }catch (ArrayIndexOutOfBoundsException ex){
                    vertexBuffer.put(0);
                    colorBuffer.put((byte)0).put((byte)0).put((byte)0);
                }
            }
        }

        vertexBuffer.flip();
        colorBuffer.flip();

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_INT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        cbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_UNSIGNED_BYTE, true, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);
    }

    public void bind(){
        GL30.glBindVertexArray(vao);
    }

    public void unbind(){
        GL30.glBindVertexArray(0);
    }

    public void delete() {
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(cbo);
        GL30.glDeleteVertexArrays(vao);
    }
}