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
public class DecalGroup implements Storeable{

    private int[] data;
    private String name;
    private byte[] unknownHeader;

    public DecalGroup(MapReader map) throws IOException {
        unknownHeader=map.readRaw(4);
        name = map.getString();
        int i = 0, count = map.getInt32();
        data = new int[count];
        while (i < count) {
            data[i] = map.getInt32();
            i++;
        }
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeRaw(unknownHeader);      //TODO: Check actual value
        writer.writeString(name,false);
        writer.writeInt32(data.length);
        int i = 0;
        while (i < data.length) {
            writer.writeInt32(data[i]);
            i++;
        }
    }
}
