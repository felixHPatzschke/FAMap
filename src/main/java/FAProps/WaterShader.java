/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.IOException;

/**
 * import FAHeightmap.Map;
 *
 *
 * @author bhofmann
 */
public class WaterShader {

    private boolean hasWater;
    private float waterElevation, waterElevationDeep, waterElevationAbyss;
    private float[] unknown1 = new float[20];
    private float[] unknown2 = new float[4];
    private String texPathWaterCubemap, texPathWaterRamp;

    public WaterShader(MapReader map) throws IOException {
        int i;

        hasWater = map.getChar() == 1;
        waterElevation = map.getFloat();
        waterElevationDeep = map.getFloat();
        waterElevationAbyss = map.getFloat();

        i = 0;
        while (i < 20) {
            unknown1[i] = map.getFloat();
            i++;
        }

        texPathWaterCubemap = map.getString();
        texPathWaterRamp = map.getString();

        i = 0;
        while (i < 4) {
            unknown2[i] = map.getFloat();
            i++;
        }
    }

    public float getWaterElevation() {
        return waterElevation;
    }

    public float getWaterElevationDeep() {
        return waterElevationDeep;
    }

    public float getWaterElevationAbyss() {
        return waterElevationAbyss;
    }

    public void setWaterElevation(int waterElevation) {
        this.waterElevation = waterElevation;
    }
}
