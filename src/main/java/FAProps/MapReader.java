/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;

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

    public void skip(long amount) throws IOException {
        long skipped=0;
        while(skipped!=amount){
            skipped=skipped+map.skip(amount-skipped);
        }
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

    @Override
    public void close() throws IOException {
        map.close();
    }
}
