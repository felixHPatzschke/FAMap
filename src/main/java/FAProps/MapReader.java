/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import UI.Logger;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.util.Arrays;

/**
 *
 * @author Basti_Temp
 */
public class MapReader implements AutoCloseable{

    private BufferedInputStream map;
    private int version = 0;

    public MapReader(InputStream is){
        map = new BufferedInputStream(is);
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    @Deprecated
    public void skip(int amount) throws IOException {
        int[] read = new int[amount];
        int skipped=0;
        while (skipped<amount){
            read[skipped]=map.read();
            skipped++;
        }
        Logger.logOut(Arrays.toString(read));
    }

    public int getInt16() throws IOException {
        return ((byte)(map.read()) & 0xFF)
                | (((byte)(map.read()) & 0xFF) << 8);
    }

    public int getInt32() throws IOException {
        return ((byte)(map.read()) & 0xFF)
                | (((byte)(map.read()) & 0xFF) << 8)
                | (((byte)(map.read()) & 0xFF) << 16)
                | (((byte)(map.read()) & 0xFF) << 24);
    }

    public float getFloat() throws IOException {
        return Float.intBitsToFloat(getInt32());
    }

    public char getChar() throws IOException {
        return (char) map.read();
    }

    public String getString() throws IOException {
        StringBuilder sb = new StringBuilder();
        char add;
        while ((add = this.getChar()) != 0) {
            sb.append(add);
        }
        return sb.toString();
    }

    public String getString(int length) throws IOException {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < length) {
            sb.append(getChar());
            i++;
        }
        return sb.toString();
    }

    public Vector3f getVector3() throws IOException {
        return new Vector3f(this.getFloat(), this.getFloat(), this.getFloat());
    }

    public Vector4f getVector4() throws IOException {
        return new Vector4f(this.getFloat(), this.getFloat(), this.getFloat(), this.getFloat());
    }

    public byte[] readRaw(int count) throws IOException {
        byte[] out = new byte[count];
        int i=0;
        while (i<count) {
            out[i]=(byte)map.read();
            i++;
        }
        return out;
    }

    @Override
    public void close() throws IOException {
        map.close();
    }
}
