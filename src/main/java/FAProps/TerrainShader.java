/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.IOException;

/**
 *
 * @author bhofmann
 */
public class TerrainShader {

    private String terrainShader, texPathBackground, textPathSkyCubemap;
    private String[][] texPathEnvCubemap;
    private float lightingMultiplier, bloom, fogMin, fogMax;
    private Vector3 sunDirection, sunColor, sunAmbience, shadowFillColor, fogColor;
    private Vector4 specularColor;

    public TerrainShader(MapReader map) throws IOException {
        terrainShader = map.getString();
        System.out.println("Terrain OpenGL.Shader  : " + terrainShader);
        texPathBackground = map.getString();
        System.out.println("Path Background : " + texPathBackground);
        textPathSkyCubemap = map.getString();
        System.out.println("Path Sky Cubemap: " + textPathSkyCubemap);

        int envCubemapCount = map.getInt32();
        System.out.println("Env Cubemap Cnt.: " + envCubemapCount);

        texPathEnvCubemap = new String[envCubemapCount][2];
        int i = 0;
        while (i < envCubemapCount) {
            texPathEnvCubemap[i][0] = map.getString();
            texPathEnvCubemap[i][1] = map.getString();
            System.out.println(texPathEnvCubemap[i][0] + " is using " + texPathEnvCubemap[i][1]);
            i++;
        }
        lightingMultiplier = map.getFloat();
        System.out.println("Lighting Mult.  : " + lightingMultiplier);
        sunDirection = map.getVector3();
        sunAmbience = map.getVector3();
        sunColor = map.getVector3();
        shadowFillColor = map.getVector3();
        specularColor = map.getVector4();
        bloom = map.getFloat();
        System.out.println("Bloom           : " + bloom);
        fogColor = map.getVector3();
        fogMin = map.getFloat();
        fogMax = map.getFloat();
    }
}
