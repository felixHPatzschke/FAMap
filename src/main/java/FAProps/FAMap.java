/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import UI.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import static UI.Logger.logOut;

/**
 *
 * @author Basti_Temp
 */
public class FAMap {

    private MapDetails mapDetails;

    private WaveTexture[] waveTexture = new WaveTexture[4];
    private ArrayDeque<WaveGenerator> waveGenerators = new ArrayDeque();
    private ArrayDeque<Decal> decals = new ArrayDeque();
    private ArrayDeque<DecalGroup> decalGroups = new ArrayDeque();
    private Layers layers;
    private Normalmap[] normalmap;
    private Texturemap texturemap;
    private TerrainShader terrainShader;
    private WaterShader waterShader;
    private WaterDetails waterDetails;
    private char[] terrainTypeData;
    private ArrayDeque<Prop> props = new ArrayDeque();

    private boolean loaded=false;

    public void loadFAMap(InputStream is,String name) {
        int i, count;
        try(
                MapReader reader = new MapReader(is)
        ){
            if (reader.getChar() == 'M' && reader.getChar() == 'a' && reader.getChar() == 'p') {
                reader.skip(1); // skip 1 byte of MAP_MAGIC
                logOut("2               : " + reader.getInt32());
                logOut("EDFE EFBE       : " + Boolean.toString(reader.getInt32() == -1091567891));
                logOut("2 (usually)     : " + reader.getInt32());
                reader.skip(8); //It's a secret
                logOut("0               : " + reader.getInt32());
                logOut("0               : " + reader.getInt16());

                mapDetails = new MapDetails(name,reader);
                terrainShader = new TerrainShader(reader);
                waterShader = new WaterShader(reader);
                i = 0;
                while (i < 4) {
                    waveTexture[i] = new WaveTexture(reader);
                    i++;
                }
                logOut("Loaded WaveTextures");
                count = reader.getInt32();
                logOut(count,false);
                i = 0;
                while (i < count) {
                    waveGenerators.add(new WaveGenerator(reader));
                    i++;
                }
                i = 0;

                layers = new Layers(reader,mapDetails);

                if (mapDetails.getVersion() == 60) {
                    reader.skip(8 + 12 + 8); //It's getting obvious...
                } else {
                    reader.skip(8);// It's not better to choose how many bytes to skip
                }
                count = reader.getInt32();
                logOut("Loading " + count + " Decals");
                i = 0;
                while (i < count) {
                    decals.add(new Decal(reader));
                    i++;
                }

                count = reader.getInt32();
                logOut("Loading " + count + " DecalGroups");
                i = 0;
                while (i < count) {
                    decalGroups.add(new DecalGroup(reader));
                    i++;
                }

                logOut("Dimensions OK:    : " + (reader.getInt32() == mapDetails.getWidth() && reader.getInt32() == mapDetails.getHeight()));
                count = reader.getInt32();
                logOut("Loading " + count + " Normalmap(s)");
                normalmap = new Normalmap[count];
                i = 0;
                while (i < count) {
                    normalmap[i] = new Normalmap(reader);
                    i++;
                }
                if (mapDetails.getVersion() < 56) {
                    reader.skip(4);
                }
                logOut("Loading Texturemap");
                texturemap = new Texturemap(reader, mapDetails.getVersion());

                logOut("1               : " + reader.getInt32());

                logOut("Loading Watermap");
                waterDetails = new WaterDetails(reader, mapDetails);

                logOut("Loading Terrain Type Data");

                count = mapDetails.getHeight() * mapDetails.getHeight();
                terrainTypeData = new char[count];
                i = 0;
                while (i < count) {
                    terrainTypeData[i] = reader.getChar();
                    i++;
                }

                if (mapDetails.getVersion() <= 53) { //Might fix it! (Or cause even more trouble)
                    reader.skip(2);
                }

                count = reader.getInt32();
                logOut("Loading " + count + " Props");
                i = 0;
                while (i < count) {
                    props.add(new Prop(reader));
                    i++;
                }
                System.gc();
                logOut("Finished loading FAMap!");
                loaded=true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texturemap getTexturemap() {
        return texturemap;
    }

    public MapDetails getMapDetails() {
        return mapDetails;
    }

    public WaterShader getWaterShader() {
        return waterShader;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
