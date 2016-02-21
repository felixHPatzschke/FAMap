package MapRenderers;

import FAProps.FAMap;
import OpenGL.MapColor;

import java.util.Random;

/**
 * Created by Basti on 04.01.2016.
 */
public class RandomRenderer extends MapRenderer {

    Random r = new Random();

    @Override
    public MapColor getColorAt(FAMap map, int x, int y) {
        byte[] color = new byte[3];
        r.nextBytes(color);
        return new MapColor(color[0],color[1],color[2]);
    }
}
