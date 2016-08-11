/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

import static UI.Logger.logOut;

/**
 *
 * @author bhofmann
 */
public class TerrainShader implements Storeable{

    private String terrainShader, texPathBackground, textPathSkyCubemap;
    private String[][] texPathEnvCubemap;
    private float lightingMultiplier, bloom, fogMin, fogMax;
    private Vector3f sunDirection, sunColor, sunAmbience, shadowFillColor, fogColor;
    private Vector4f specularColor;

    public TerrainShader(MapReader map) throws IOException {
        terrainShader = map.getString();
        logOut("Terrain OpenGL.Shader  : " + terrainShader);
        texPathBackground = map.getString();
        logOut("Path Background : " + texPathBackground);
        textPathSkyCubemap = map.getString();
        logOut("Path Sky Cubemap: " + textPathSkyCubemap);

        int envCubemapCount = map.getInt32();
        logOut("Env Cubemap Cnt.: " + envCubemapCount);

        texPathEnvCubemap = new String[envCubemapCount][2];
        int i = 0;
        while (i < envCubemapCount) {
            texPathEnvCubemap[i][0] = map.getString();
            texPathEnvCubemap[i][1] = map.getString();
            logOut(texPathEnvCubemap[i][0] + " is using " + texPathEnvCubemap[i][1]);
            i++;
        }
        lightingMultiplier = map.getFloat();
        logOut("Lighting Mult.  : " + lightingMultiplier);
        sunDirection = map.getVector3();
        sunAmbience = map.getVector3();
        sunColor = map.getVector3();
        shadowFillColor = map.getVector3();
        specularColor = map.getVector4();
        bloom = map.getFloat();
        logOut("Bloom           : " + bloom);
        fogColor = map.getVector3();
        fogMin = map.getFloat();
        fogMax = map.getFloat();
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        writer.writeString(terrainShader,false);
        writer.writeString(texPathBackground,false);
        writer.writeString(textPathSkyCubemap,false);
        writer.writeInt32(texPathEnvCubemap.length);
        int i = 0;
        while (i < texPathEnvCubemap.length) {
            writer.writeString(texPathEnvCubemap[i][0],false);
            writer.writeString(texPathEnvCubemap[i][1],false);
            i++;
        }
        writer.writeFloat(lightingMultiplier);
        writer.writeVector3f(sunDirection);
        writer.writeVector3f(sunAmbience);
        writer.writeVector3f(sunColor);
        writer.writeVector3f(shadowFillColor);
        writer.writeVector4f(specularColor);
        writer.writeFloat(bloom);
        writer.writeVector3f(fogColor);
        writer.writeFloat(fogMin);
        writer.writeFloat(fogMax);
    }
}
