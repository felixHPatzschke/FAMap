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
public class WaterDetails {

    public char[] watermapData, waterFoamMask, waterFlatnessMask, waterDepthBiasMask;

    public WaterDetails(MapReader map, MapDetails mapDetails) throws IOException {
        int i = 0, length = map.getInt32();
        watermapData = new char[length];
        while (i < length) {
            watermapData[i] = map.getChar();
            i++;
        }
        length = (mapDetails.getHeight() / 2) * (mapDetails.getWidth() / 2);

        waterFoamMask = new char[length];
        waterFlatnessMask = new char[length];
        waterDepthBiasMask = new char[length];

        i = 0;
        while (i < length) {
            waterFoamMask[i] = map.getChar();
            i++;
        }
        i = 0;
        while (i < length) {
            waterFlatnessMask[i] = map.getChar();
            i++;
        }
        i = 0;
        while (i < length) {
            waterDepthBiasMask[i] = map.getChar();
            i++;
        }

    }
}
