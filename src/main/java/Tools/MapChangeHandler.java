package Tools;

import FAProps.FAMap;
import OpenGL.Tile;
import Renderables.RenderableMap;

/**
 * Created by Basti on 19.7.16.
 */
public class MapChangeHandler {
    private boolean[][] mapChanges;
    private FAMap map;

    public MapChangeHandler(FAMap map) {
        this.map = map;
        mapChanges = new boolean[map.getMapDetails().getWidth() / Tile.SIZE][map.getMapDetails().getHeight() / Tile.SIZE];
    }

    public void setHeightAt(int x, int y, int value) {
        mapChanges[x / Tile.SIZE][y / Tile.SIZE] = true;
        map.getMapDetails().getHeightmap()[x][y] = value;
    }

    public int getHeightAt(int x, int y) {
        return map.getMapDetails().getHeightmap()[x][y];
    }

    public boolean[][] getMapChanges() {
        return mapChanges;
    }

    public void add(MapChangeHandler otherHandler) {
        int x, y = 0;
        boolean[][] otherChanges = otherHandler.getMapChanges();
        while (y < otherChanges.length) {
            x = 0;
            while (x < otherChanges[0].length) {
                if (!mapChanges[x][y]) {
                    mapChanges[x][y] = otherChanges[x][y];
                }
                x++;
            }
            y++;
        }
    }

    public void applyChanges(RenderableMap map) {
        int x, y = 0;
        while (y < mapChanges.length) {
            x = 0;
            while (x < mapChanges[0].length) {
                if (mapChanges[x][y]) {
                    map.clearTile(x, y);
                }
                x++;
            }
            y++;
        }
    }

    public int getHeight() {
        return map.getMapDetails().getHeight();
    }

    public int getWidth() {
        return map.getMapDetails().getWidth();
    }
}
