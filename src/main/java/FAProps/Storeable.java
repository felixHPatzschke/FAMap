package FAProps;

import java.io.IOException;

/**
 * Created by Basti on 19.7.16.
 */
public interface Storeable {
    //Assume map version 56 for EVERYTHING
    void store(MapWriter writer)throws IOException;
}
