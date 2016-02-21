/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import java.io.IOException;

/**
 *
 * @author Basti_Temp
 */
public class WaveGenerator {

    private float rotation, lifetimeFirst, lifetimeSecond, periodFirst,
            periodSecond, scaleFirst, scaleSecond, frameCount, frameRateFirst,
            frameRateSecond, stripCount;
    private String textureName, rampName;
    private Vector3 position, velocity;

    public WaveGenerator(MapReader map) throws IOException {
        textureName = map.getString();
        rampName = map.getString();

        position = map.getVector3();
        rotation = map.getFloat();
        velocity = map.getVector3();

        lifetimeFirst = map.getFloat();
        lifetimeSecond = map.getFloat();
        periodFirst = map.getFloat();
        periodSecond = map.getFloat();
        scaleFirst = map.getFloat();
        scaleSecond = map.getFloat();

        frameCount = map.getFloat();
        frameRateFirst = map.getFloat();
        frameRateSecond = map.getFloat();
        stripCount = map.getFloat();
    }
}
