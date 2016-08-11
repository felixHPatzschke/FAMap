package FAProps;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Basti on 19.7.16.
 */
public class MapWriter implements AutoCloseable{
    private BufferedOutputStream map;
    private int version = 0;

    public MapWriter(OutputStream os){
        map = new BufferedOutputStream(os);
    }

    public void writeInt16(int in) throws IOException {
        map.write(new byte[]{(byte) in, (byte) (in>>8)});
    }

    public void writeInt32(int in) throws IOException {
        map.write(new byte[]{(byte) in, (byte) (in>>8), (byte) (in>>16), (byte) (in>>24)});
    }

    public void writeFloat(float in) throws IOException{
        writeInt32(Float.floatToIntBits(in));
    }

    public void writeChar(char in) throws IOException{
        map.write(in);
    }

    public void writeString(String in,boolean usesLength) throws IOException {
        if(usesLength){
            int i=0;
            writeInt32(in.length());
            while(i<in.length()) {
                map.write(in.charAt(i));
                i++;
            }
        }else {
            int i=0;
            while(i<in.length()) {
                map.write(new byte[]{(byte) in.charAt(i)});
                i++;
            }
            map.write(new byte[]{0});
        }
    }

    public void writeVector3f(Vector3f in) throws IOException {
        writeFloat(in.x);
        writeFloat(in.y);
        writeFloat(in.z);
    }

    public void writeVector4f(Vector4f in) throws IOException{
        writeFloat(in.x);
        writeFloat(in.y);
        writeFloat(in.z);
        writeFloat(in.w);
    }

    public void writeRaw(byte[] in) throws IOException {
        map.write(in);
    }

    @Override
    public void close() throws IOException {
        map.close();
    }
}
