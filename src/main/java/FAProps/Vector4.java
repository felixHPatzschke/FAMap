/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

/**
 *
 * @author bhofmann
 */
public class Vector4 {

    public float a, b, c, d;

    public Vector4(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public String toString() {
        return "a: " + a + " | b: " + b + " | c: " + c + " | d: " + d;
    }
}
