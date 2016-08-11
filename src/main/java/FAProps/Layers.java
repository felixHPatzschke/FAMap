package FAProps;

import UI.Logger;

import java.io.IOException;

import static UI.Logger.logOut;

/**
 * Created by Basti on 19.7.16.
 */
public class Layers implements Storeable{

    private Layer[] layers = new Layer[11];
    private byte unknownLayerData[][] = new byte[5][20];
    private byte unknown56Header[];
    private byte unknown56OnlyHeader[];

    public Layers(MapReader reader, MapDetails mapDetails) throws IOException{
        int i=0;
        if (mapDetails.getVersion() < 56) {
            logOut("Tileset: " + reader.getString());
            while (i <= 4) {
                layers[i] = new Layer(reader, false);
                i++;
            }
            while (i <= 8) {
                layers[i] = new Layer();
                i++;
            }
            layers[i] = new Layer(reader, false);
        } else {

            int count = reader.getInt32();
            unknown56Header=reader.readRaw(count);

            if (mapDetails.getVersion() == 56) {
                unknown56OnlyHeader=reader.readRaw(24); //No one will ever know!
                i = 0;
                while (i <= 9) {
                    layers[i] = new Layer(reader, true);
                    i++;
                }
                i = 0;
                while (i <= 8) {
                    layers[i].loadNormalmap(reader);
                    i++;
                }
            } else {
                i = 0;
                while (i < 11) {
                    if (i == 5) {
                        unknownLayerData[i]=reader.readRaw(20);
                    }
                    if (i < 6) {
                        layers[i] = new Layer(reader, true);
                    } else {
                        layers[i - 6].loadNormalmap(reader);
                    }
                    i++;
                }
            }
        }
    }

    @Override
    public void store(MapWriter writer) throws IOException {
        int i;
        writer.writeInt32(unknown56Header.length);
        writer.writeRaw(unknown56Header);
        writer.writeRaw(unknown56OnlyHeader);
        i = 0;
        while (i <= 9) {
            layers[i].writeTexture(writer);
            i++;
        }
        i = 0;
        while (i <= 8) {
            layers[i].writeNormalmap(writer);
            i++;
        }
    }
}
