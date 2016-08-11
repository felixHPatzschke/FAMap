/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import org.joml.Vector3f;

import java.io.IOException;

/**
 * @author Basti_Temp
 */
public class Decal implements Storeable {

    private int type, ownerArmy, unknownHeader;
    private String[] texPaths;
    private Vector3f scale, position, rotation;
    private float cutOffLOD, nearCutOffLOD;

    public Decal(MapReader map) throws IOException {
        unknownHeader=map.getInt32();
        type = map.getInt32();

        int length, textureCount = map.getInt32();
        int i = 0;

        texPaths = new String[textureCount];
        while (i < textureCount) {
            length = map.getInt32();
            texPaths[i] = map.getString(length);
            i++;
        }

        scale = map.getVector3();
        position = map.getVector3();
        rotation = map.getVector3();
        cutOffLOD = map.getFloat();
        nearCutOffLOD = map.getFloat();
        ownerArmy = map.getInt32();
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeInt32(unknownHeader);
        writer.writeInt32(type);
        writer.writeInt32(texPaths.length);
        int i = 0;
        while (i < texPaths.length) {
            writer.writeString(texPaths[i],true);
            i++;
        }
        writer.writeVector3f(scale);
        writer.writeVector3f(position);
        writer.writeVector3f(rotation);
        writer.writeFloat(cutOffLOD);
        writer.writeFloat(nearCutOffLOD);
        writer.writeInt32(ownerArmy);
    }
}
