import FAProps.MapReader;
import FAProps.MapWriter;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * Created by Basti on 19.7.16.
 */
public class ReaderWriterTest {
    @Test
    public void testRW() {
        Vector3f test3f = new Vector3f(0,256,256*256);
        Vector4f test4f = new Vector4f(0f,256,256*256,256*256*256);
        String test = "Help me!/_";

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            new Thread(
                    () -> {
                        MapWriter writer = new MapWriter(out);
                        try {
                            writer.writeInt16(0);
                            writer.writeInt16(256);

                            writer.writeInt32(0);
                            writer.writeInt32(256);
                            writer.writeInt32(256*256);
                            writer.writeInt32(256*256*256);

                            writer.writeFloat(0);
                            writer.writeFloat(Float.MAX_VALUE);

                            writer.writeChar((char) 0);
                            writer.writeChar((char) 255);

                            writer.writeVector3f(test3f);
                            writer.writeVector4f(test4f);

                            writer.writeString(test,false);
                            writer.writeString(test,true);

                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
            MapReader reader = new MapReader(in);

            Assert.assertEquals(0,reader.getInt16());
            Assert.assertEquals(256,reader.getInt16());

            Assert.assertEquals(0,reader.getInt32());
            Assert.assertEquals(256,reader.getInt32());
            Assert.assertEquals(256*256,reader.getInt32());
            Assert.assertEquals(256*256*256,reader.getInt32());

            Assert.assertEquals(0f,reader.getFloat(),0);
            Assert.assertEquals(Float.MAX_VALUE,reader.getFloat(),0);

            Assert.assertEquals((char) 0,reader.getChar());
            Assert.assertEquals((char) 255,reader.getChar());

            Assert.assertEquals(test3f.toString(),reader.getVector3().toString());
            Assert.assertEquals(test4f.toString(),reader.getVector4().toString());

            Assert.assertEquals(test,reader.getString());
            int length = reader.getInt32();
            Assert.assertEquals(test,reader.getString(length));

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
