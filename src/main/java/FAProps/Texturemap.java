/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import OpenGL.MapColor;

import java.io.IOException;

/**
 *
 * @author bhofmann
 */
public class Texturemap {

    private MapColor[][] texturemap1, texturemap2;

    public Texturemap(MapReader map, int version) throws IOException {
        int length = map.getInt32()-128;
        System.out.println("Texturemap: "+length);
        int sidelen= (int) Math.sqrt(length);

        texturemap1 = new MapColor[sidelen][sidelen];
        map.skip(128);

        for (int y = 0; y < sidelen; y++) {
            for (int x = 0; x < sidelen; x++) {
                texturemap1[x][y]=new MapColor(map.getChar());
            }
        }

        if (version >= 56) {
            length = map.getInt32()-128;
            sidelen= (int) Math.sqrt(length);
            texturemap2 = new MapColor[sidelen][sidelen];
            for (int y = 0; y < sidelen; y++) {
                for (int x = 0; x < sidelen; x++) {
                    texturemap2[x][y]=new MapColor(map.getChar());
                }
            }
            map.skip(128);
        }

        /*
        texturemap1 = new char[length];
        while (i < length) {
            map.getChar();
            texturemap1[i] = map.getChar();
            i++;
        }

        if (version >= 56) {
            length = map.getInt32();
            i = 0;
            texturemap2 = new char[length];
            while (i < length) {
                texturemap2[i] = map.getChar();
                i++;
            }
        }
        */
    }
}