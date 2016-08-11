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
public class Prop implements Storeable{

    private Vector3f position, rotationX, rotationY, rotationZ, scale;
    private String blueprintPath;

    public Prop(MapReader map) throws IOException {
        blueprintPath = map.getString();
        position = map.getVector3();
        rotationX = map.getVector3();
        rotationY = map.getVector3();
        rotationZ = map.getVector3();
        scale = map.getVector3();
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeString(blueprintPath,false);
        writer.writeVector3f(position);
        writer.writeVector3f(rotationX);
        writer.writeVector3f(rotationY);
        writer.writeVector3f(rotationZ);
        writer.writeVector3f(scale);
    }
}
