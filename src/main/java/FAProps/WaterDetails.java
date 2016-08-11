/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.IOException;

/**
 *
 * @author Basti_Temp
 */
public class WaterDetails implements Storeable{

    public byte[] watermapData, waterFoamMask, waterFlatnessMask, waterDepthBiasMask;

    public WaterDetails(MapReader map, MapDetails mapDetails) throws IOException {
        int length = map.getInt32();

        watermapData = map.readRaw(length);

        length = (mapDetails.getHeight() / 2) * (mapDetails.getWidth() / 2);

        waterFoamMask = map.readRaw(length);
        waterFlatnessMask = map.readRaw(length);
        waterDepthBiasMask = map.readRaw(length);
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeInt32(watermapData.length);

        writer.writeRaw(watermapData);
        writer.writeRaw(waterFoamMask);
        writer.writeRaw(waterFlatnessMask);
        writer.writeRaw(waterDepthBiasMask);
    }
}
