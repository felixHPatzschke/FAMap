package Renderables;

import FAProps.FAMap;
import MapRenderers.MapRenderer;
import OpenGL.Tile;
import org.lwjgl.opengl.*;

/**
 * Created by Basti on 31.01.2016.
 */
public class RenderableMap extends Renderable {

    private FAMap map;
    private MapRenderer mapRenderer;
    private float visionRadius=4;
    private Tile[][] tiles;
    private int iboId=-1;

    public RenderableMap(){
    }

    public void setMapRenderer(MapRenderer mapRenderer){
        this.mapRenderer=mapRenderer;
        tiles=null;
    }

    public void setMap(FAMap map){
        if(iboId==-1){
            iboId=Tile.generateIndexBuffer();
        }
        setScale(map.getMapDetails().getWidth()/10000f,map.getMapDetails().getHeight()/10000f,map.getMapDetails().getHeightmapScale()/20f);
        this.map=map;
    }

    @Override
    public boolean isRenderable(){
        return map!=null;
    }

    @Override
    public void render(Camera camera) {
        if(tiles==null){
            tiles=new Tile[map.getMapDetails().getWidth()/Tile.SIZE][map.getMapDetails().getHeight()/Tile.SIZE];
        }
        int centralTileX = (int)(getTranslationX()/Tile.SIZE);
        int centralTileY = (int)(getTranslationY()/Tile.SIZE);
        for (int x = 0;x<map.getMapDetails().getWidth()/Tile.SIZE;x++){
            for (int y = 0;y<map.getMapDetails().getHeight()/Tile.SIZE;y++){
                if(Math.pow(centralTileX-x,2)<=visionRadius&&Math.pow(centralTileY-y,2)<=visionRadius){
                    if (tiles[x][y] == null) {
                        tiles[x][y] = new Tile(map, mapRenderer, x, y);
                    }
                    tiles[x][y].bind();

                    GL20.glEnableVertexAttribArray(0);
                    GL20.glEnableVertexAttribArray(1);

                    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);

                    GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, Tile.INDEX_BUFFER_SIZE, GL11.GL_UNSIGNED_SHORT, 0);

                    GL20.glDisableVertexAttribArray(0);
                    GL20.glDisableVertexAttribArray(1);

                    GL30.glBindVertexArray(0);
                }
            }
        }
    }

    @Override
    public void remove() {
        if(tiles!=null) {
            int mapHeight = map.getMapDetails().getHeight() / Tile.SIZE;
            int mapWidth = map.getMapDetails().getWidth() / Tile.SIZE;
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    if(tiles[x][y]!=null) {
                        tiles[x][y].delete();
                    }
                }
            }
        }
    }
}
