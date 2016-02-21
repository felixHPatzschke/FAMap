package MapRenderers;

import FAProps.FAMap;
import OpenGL.MapColor;

/**
 * Created by Basti on 03.01.2016.
 */
public class TexturemapRenderer extends MapRenderer {
    @Override
    public MapColor getColorAt(FAMap map, int x, int y) {
        return new MapColor((byte)0,(byte)0,(byte)0);
    }
}