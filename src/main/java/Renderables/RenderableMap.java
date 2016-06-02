package Renderables;

import FAProps.FAMap;
import FAProps.WaterShader;
import MapRenderers.MapRenderer;
import OpenGL.Tile;
import UI.GLContextThread;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Basti on 31.01.2016.
 */
public class RenderableMap extends Renderable {

    private FAMap map;
    private MapRenderer mapRenderer;
    private float visionRadius = 4;
    private Tile[][] tiles;
    private int iboId = -1;

    private int minHeight, maxHeight;

    public RenderableMap() {
        //eyeX=-90;
    }

    public void rescanHeight() {
        minHeight = Integer.MAX_VALUE;
        maxHeight = Integer.MIN_VALUE;
        for (int x = 0; x < map.getMapDetails().getWidth() / Tile.SIZE; x++) {
            for (int y = 0; y < map.getMapDetails().getHeight() / Tile.SIZE; y++) {
                if (tiles[x][y].getMinHeight() < minHeight) {
                    minHeight = tiles[x][y].getMinHeight();
                }
                if (tiles[x][y].getMaxHeight() < maxHeight) {
                    maxHeight = tiles[x][y].getMaxHeight();
                }
            }
        }
    }

    public void setMapRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
        tiles = null;
    }

    public void setMap(FAMap map) {
        if (iboId == -1) {
            iboId = Tile.generateIndexBuffer();
        }
        setScale(1, 1, map.getMapDetails().getHeightmapScale());
        this.map = map;
        tiles = new Tile[map.getMapDetails().getWidth() / Tile.SIZE][map.getMapDetails().getHeight() / Tile.SIZE];

        for (int x = 0; x < map.getMapDetails().getWidth() / Tile.SIZE; x++) {
            for (int y = 0; y < map.getMapDetails().getHeight() / Tile.SIZE; y++) {
                tiles[x][y] = new Tile(map, x, y);
            }
        }
        rescanHeight();
    }

    @Override
    public boolean isRenderable() {
        return map != null;
    }

    @Override
    public void render(Camera4 camera) {
        if (tiles == null) {
            tiles = new Tile[map.getMapDetails().getWidth() / Tile.SIZE][map.getMapDetails().getHeight() / Tile.SIZE];
        }
        Vector3f cameraPos = camera.getPos();
        int centralTileX = (int) (-cameraPos.x / Tile.SIZE);
        int centralTileY = (int) (-cameraPos.y / Tile.SIZE);
        for (int x = 0; x < map.getMapDetails().getWidth() / Tile.SIZE; x++) {
            for (int y = 0; y < map.getMapDetails().getHeight() / Tile.SIZE; y++) {
                if (Math.pow(centralTileX - x, 2) <= visionRadius && Math.pow(centralTileY - y, 2) <= visionRadius) {
                    if (tiles[x][y] == null) {
                        tiles[x][y] = new Tile(map, x, y);
                    }

                    tiles[x][y].bind();

                    GL20.glEnableVertexAttribArray(0);
                    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
                    GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, Tile.INDEX_BUFFER_SIZE, GL11.GL_UNSIGNED_SHORT, 0);
                    GL20.glDisableVertexAttribArray(0);
                    GL30.glBindVertexArray(0);
                }
            }
        }
    }

    @Override
    public void remove() {
        if (tiles != null) {
            int mapHeight = map.getMapDetails().getHeight() / Tile.SIZE;
            int mapWidth = map.getMapDetails().getWidth() / Tile.SIZE;
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    if (tiles[x][y] != null) {
                        tiles[x][y].delete();
                    }
                }
            }
        }
    }
}
