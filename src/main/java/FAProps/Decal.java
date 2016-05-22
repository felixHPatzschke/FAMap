/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import org.joml.Vector3f;

import java.io.IOException;

/**
 *
 * @author Basti_Temp
 */
public class Decal {

    private int type, ownerArmy;
    private String[] texPathes;
    private Vector3f scale, position, rotation;
    private float cutOffLOD, nearCutOffLOD;

    public Decal(MapReader map) throws IOException {
        map.skip(4); //Not again!
        type = map.getInt32();
        int length, textureCount = map.getInt32();

        int i = 0;
        texPathes = new String[textureCount];
        while (i < textureCount) {
            length = map.getInt32();
            texPathes[i] = map.getString(length);
            i++;
        }

        scale = map.getVector3();
        position = map.getVector3();
        rotation = map.getVector3();
        cutOffLOD = map.getFloat();
        nearCutOffLOD = map.getFloat();
        ownerArmy = map.getInt32();
    }
}
