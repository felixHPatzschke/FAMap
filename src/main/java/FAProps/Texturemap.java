/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import OpenGL.MapColor;

import java.io.IOException;

import static UI.Logger.logOut;

/**
 * @author bhofmann
 */
public class Texturemap implements Storeable {

    byte[] texturemap1, texturemap2;

    public Texturemap(MapReader map, int version) throws IOException {
        texturemap1 = map.readRaw(map.getInt32());
        if (version >= 56) {
            texturemap2 = map.readRaw(map.getInt32());
        }
    }

    public byte[] getTexturemap1() {
        return texturemap1;
    }

    public boolean hasTexturemap2() {
        return texturemap2 != null;
    }

    public byte[] getTexturemap2() {
        return texturemap2;
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeInt32(texturemap1.length);
        writer.writeRaw(texturemap1);

        writer.writeInt32(texturemap2.length);
        writer.writeRaw(texturemap2);
    }
}