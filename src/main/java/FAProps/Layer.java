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
public class Layer{

    private String pathTexture, pathNormalmap;
    private int scaleTexture;
    private float scaleNormalmap;

    public Layer(MapReader map, boolean onlyTexture) throws IOException {
        pathTexture = map.getString();
        if (!onlyTexture) {
            pathNormalmap = map.getString();
        }
        scaleTexture = map.getInt32();
        if (!onlyTexture) {
            scaleNormalmap = map.getFloat();
        }
    }

    public Layer() {
        pathTexture = "";
        pathNormalmap = "";
        scaleTexture = 1;
        scaleNormalmap = 1;
    }

    public void loadNormalmap(MapReader map) throws IOException {
        pathNormalmap = map.getString();
        scaleNormalmap = map.getFloat();
    }

    void writeTexture(MapWriter writer) throws IOException {
        writer.writeString(pathTexture,false);
        writer.writeInt32(scaleTexture);
    }

    void writeNormalmap(MapWriter writer) throws IOException {
        writer.writeString(pathNormalmap,false);
        writer.writeFloat(scaleNormalmap);
    }
}
