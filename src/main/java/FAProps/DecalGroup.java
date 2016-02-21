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
public class DecalGroup {

    private int[] data;
    private String name;

    public DecalGroup(MapReader map) throws IOException {
        map.skip(4);
        name = map.getString();
        int i = 0, count = map.getInt32();
        data = new int[count];
        while (i < count) {
            data[i] = map.getInt32();
            i++;
        }
    }

}
