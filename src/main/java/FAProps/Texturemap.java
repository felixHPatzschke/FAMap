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
public class Texturemap {

    private MapColor[][] texturemap1, texturemap2;
    private int sidelen;

    public Texturemap(MapReader map, int version) throws IOException {
        int length = map.getInt32() - 128;
        logOut("Texturemap: " + length);
        sidelen = (int) Math.sqrt(length);

        texturemap1 = new MapColor[sidelen][sidelen];
        map.skip(128);

        for (int y = 0; y < sidelen; y++) {
            for (int x = 0; x < sidelen; x++) {
                texturemap1[x][y] = new MapColor(map.getChar());
            }
        }

        if (version >= 56) {
            length = map.getInt32() - 128;
            sidelen = (int) Math.sqrt(length);
            texturemap2 = new MapColor[sidelen][sidelen];
            for (int y = 0; y < sidelen; y++) {
                for (int x = 0; x < sidelen; x++) {
                    texturemap2[x][y] = new MapColor(map.getChar());
                }
            }
            map.skip(128);
        }
    }

    public MapColor[][] getTexturemap1() {
        return texturemap1;
    }

    public boolean hasTexturemap2(){
        return texturemap2!=null;
    }

    public MapColor[][] getTexturemap2() {
        return texturemap2;
    }

    public int getSidelen() {
        return sidelen;
    }
}