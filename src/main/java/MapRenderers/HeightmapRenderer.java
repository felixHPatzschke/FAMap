package MapRenderers;

import FAProps.FAMap;
import OpenGL.MapColor;

/**
 * Created by Basti on 03.01.2016.
 */
public class HeightmapRenderer extends MapRenderer {
    @Override
    public MapColor getColorAt(FAMap map, int x, int y) {
        float h = (int) (map.getMapDetails().getHeightmap()[x][y]*map.getMapDetails().getHeightmapScale());
        if(h<=map.getWaterShader().getWaterElevation()) {
            return new MapColor((byte) (h/2),(byte) (h/2),(byte) h);
        }else{
            return new MapColor((byte) h,(byte) h,(byte) h);
        }
    }
}