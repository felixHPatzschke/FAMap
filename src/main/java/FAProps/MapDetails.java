/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.File;
import java.io.IOException;

import static UI.Logger.logOut;

/**
 * @author bhofmann
 */
public class MapDetails implements Storeable{

    private int previewSize, width, height, version;
    private float heightmapScale;
    private int[][] heightmap;
    private String name;
    private byte[] preview;
    private byte unknownEnd;

    public MapDetails(String name, MapReader map) throws IOException {
        this.name = name;
        previewSize = map.getInt32();
        logOut("Preview Size    : " + previewSize);
        preview=map.readRaw(previewSize);
        version = map.getInt32();
        map.setVersion(version);
        logOut("Map Version     : " + version);
        width = map.getInt32();
        height = map.getInt32();
        logOut("Width           : " + width);
        logOut("Height          : " + height);
        heightmapScale = map.getFloat();
        logOut("Heightmap Scale : " + heightmapScale);
        heightmap = new int[width + 1][height + 1];
        int i = 0, j;
        logOut("---------");
        logOut("Heightmap");
        while (i < width + 1) {
            j = 0;
            while (j < height + 1) {
                heightmap[i][j] = map.getInt16();
                j++;
            }
            i++;
        }
        logOut("---------");
        if (map.getVersion() > 53) {
            unknownEnd=map.readRaw(1)[0];
        }
    }

    public int getVersion() {
        return version;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getHeightmap() {
        return heightmap;
    }

    public void setHeightmap(int[][] heightmap) {
        this.heightmap = heightmap;
    }

    public float getHeightmapScale() {
        return heightmapScale;
    }

    public String getName() {
        return name;
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeInt32(previewSize);             //TODO: Generate good preview
        writer.writeRaw(preview);

        writer.writeInt32(56);                      //Enforces Version 56
        writer.writeInt32(width);
        writer.writeInt32(height);
        writer.writeFloat(heightmapScale);
        int j,i = 0;
        while (i < width + 1) {
            j = 0;
            while (j < height + 1) {
                writer.writeInt16(heightmap[i][j]); //TODO: Convert Heightmap to short
                j++;
            }
            i++;
        }
        writer.writeRaw(new byte[]{unknownEnd});                 //TODO: Find actual value
    }
}
