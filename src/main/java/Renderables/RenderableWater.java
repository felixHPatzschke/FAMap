package Renderables;

import FAProps.FAMap;
import FAProps.WaterShader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Basti on 28.05.2016.
 */
public class RenderableWater extends Renderable {

    private int waterIbo, waterVao, waterVbo, waterCbo;
    private boolean loaded=false;

    public RenderableWater(){
        waterIbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, waterIbo);
        ShortBuffer indices = BufferUtils.createShortBuffer(18);
        indices.put(new short[]{
                8, 9, 10,
                9, 10, 11,
                4, 5, 6,
                5, 6, 7,
                0, 1, 2,
                1, 2, 3,
        });
        indices.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        IntBuffer vertices = BufferUtils.createIntBuffer(12 * 3);
        vertices.put(new int[]{
                1, 1, 0,
                1, -1, 0,
                -1, 1, 0,
                -1, -1, 0,

                1, 0, 1,
                -1, 0, 1,
                1, 0, -1,
                -1, 0, -1,

                0, 1, 1,
                0, -1, 1,
                0, 1, -1,
                0, -1, -1
        });
        vertices.flip();

        ByteBuffer colors = BufferUtils.createByteBuffer(12 * 3);
        colors.put(new byte[]{
                (byte) 0,(byte) 191,(byte) 255,
                (byte) 0,(byte) 191,(byte) 255,
                (byte) 0,(byte) 191,(byte) 255,
                (byte) 0,(byte) 191,(byte) 255,

                (byte) 0,(byte) 110,(byte) 255,
                (byte) 0,(byte) 110,(byte) 255,
                (byte) 0,(byte) 110,(byte) 255,
                (byte) 0,(byte) 110,(byte) 255,

                (byte) 0,(byte) 0,(byte) 255,
                (byte) 0,(byte) 0,(byte) 255,
                (byte) 0,(byte) 0,(byte) 255,
                (byte) 0,(byte) 0,(byte) 255,
        });
        colors.flip();

        waterVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(waterVao);

        waterVbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, waterVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_INT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        waterCbo = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, waterCbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_UNSIGNED_BYTE, true, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);
    }

    public void setWaterShader(FAMap map) {
        IntBuffer vertices = BufferUtils.createIntBuffer(12 * 3);
        WaterShader shader = map.getWaterShader();

        int width = map.getMapDetails().getWidth(), height = map.getMapDetails().getHeight();
        int
                water = (int) (shader.getWaterElevation()), //* map.getMapDetails().getHeightmapScale()),
                deep = (int) (shader.getWaterElevationDeep()), //* map.getMapDetails().getHeightmapScale()),
                abyss = (int) (shader.getWaterElevationAbyss()); //* map.getMapDetails().getHeightmapScale());

        System.out.println(shader.getWaterElevation());

        vertices.put(new int[]{
                width, height, water,
                width, 0, water,
                0, height, water,
                0, 0, water,

                width, height, deep,
                width, 0, deep,
                0, height, deep,
                0, 0, deep,

                width, height, abyss,
                width, 0, abyss,
                0, height, abyss,
                0, 0, abyss,
        });
        vertices.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, waterVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_INT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        loaded=true;
    }

    @Override
    public float getTransparency(){
        return 0.2f;
    }

    @Override
    public void render(Camera4 camera) {
        GL30.glBindVertexArray(waterVao);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, waterIbo);
        GL11.glDrawElements(GL11.GL_TRIANGLES, 12 * 3, GL11.GL_UNSIGNED_SHORT, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }

    @Override
    public boolean isRenderable(){
        return loaded;
    }

    @Override
    public void remove() {

    }
}
