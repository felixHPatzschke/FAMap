/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author bhofmann
 */
public class MapDetails {

    private int previewSize, width, height, version;
    private float heightmapScale;
    private int[][] heightmap;
    private String name;

    public MapDetails(File f, MapReader map) throws IOException {
        name=f.getName();
        previewSize = map.getInt32();
        System.out.println("Preview Size    : " + previewSize);
        map.skip(previewSize);
        version = map.getInt32();
        map.setVersion(version);
        System.out.println("Map Version     : " + version);
        width = map.getInt32();
        height = map.getInt32();
        System.out.println("Width           : " + width);
        System.out.println("Height          : " + height);
        heightmapScale = map.getFloat();
        heightmap = new int[width + 1][height + 1];
        int i = 0, j;
        System.out.println("---------");
        System.out.println("Heightmap");
        while (i < width + 1) {
            j = 0;
            while (j < height + 1) {
                heightmap[i][j] = map.getInt16();
                j++;
            }
            i++;
        }
        System.out.println("---------");
        if (map.getVersion() > 53) {
            map.skip(1);
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

    public String getName(){
        return name;
    }
}
