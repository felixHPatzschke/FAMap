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
public class WaveTexture implements Storeable{

    private float scaleX, scaleY;
    private String texturePath;

    public WaveTexture(MapReader map) throws IOException {
        scaleX = map.getFloat();
        scaleY = map.getFloat();
        texturePath = map.getString();
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeFloat(scaleX);
        writer.writeFloat(scaleY);
        writer.writeString(texturePath,false);
    }
}
