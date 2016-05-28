package MapRenderers;

import FAProps.FAMap;
import OpenGL.MapColor;

/**
 * Created by Basti on 03.01.2016.
 */
public class TexturemapRenderer extends MapRenderer {
    @Override
    public MapColor getColorAt(FAMap map, int x, int y) {
        float hscale=((float) map.getTexturemap().getSidelen())/map.getMapDetails().getHeight();
        float wscale=((float) map.getTexturemap().getSidelen())/map.getMapDetails().getWidth();

        if(map.getTexturemap().hasTexturemap2()){
            return map.getTexturemap().getTexturemap2()[(int) (x * wscale)][(int) (y * hscale)];
        }else {
            return map.getTexturemap().getTexturemap1()[(int) (x * wscale)][(int) (y * hscale)];
        }
    }
}