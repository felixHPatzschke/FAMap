package OpenGL;

import java.awt.*;

/**
 * Created by Basti on 03.01.2016.
 */
public class MapColor {

    byte r,g,b,a=(byte)255;

    public MapColor(byte r,byte g,byte b,byte a){
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }

    public MapColor(char s){
        r = (byte) (((s)&0x1F)*8.226);
        g = (byte) (((s>>6)&0x3F)*4.048);
        b = (byte) (((s>>11)&0x1F)*8.226);
    }

    public MapColor(byte r,byte g,byte b){
        this.r=r;
        this.g=g;
        this.b=b;
    }

    public byte[] toRGBArray() {
        return new byte[]{r,g,b};
    }
}
