package MapRenderers;

import FAProps.FAMap;
import OpenGL.MapColor;

/**
 * Created by Basti on 03.01.2016.
 */
public abstract class MapRenderer {
    public abstract MapColor getColorAt(FAMap map,int x,int y);
}
