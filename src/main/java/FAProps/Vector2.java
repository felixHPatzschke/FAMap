/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FAProps;

/**
 *
 * @author Dev
 */
public class Vector2 {

    public static final Vector2 o = new Vector2(0,0);
    public static final Vector2 e_x = new Vector2(1,0);
    public static final Vector2 e_y = new Vector2(0,1);

    public double x,y;

    public Vector2(){
        this.x=0;
        this.y=0;
    }

    public Vector2(double x, double y){
        this.x=x;
        this.y=y;
    }

    public void add(Vector2 v){
        this.x += v.x;
        this.y += v.y;
    }

    public void subtract(Vector2 v){
        this.x -= v.x;
        this.y -= v.y;
    }

    public Vector2 plus(Vector2 v){
        return new Vector2(x+v.x, x+v.y);
    }

    public Vector2 minus(Vector2 v){
        return new Vector2(x-v.x, x-v.y);
    }

    public double absSqr(){
        return (x*x+y*y);
    }

    public double abs(){
        return Math.sqrt(absSqr());
    }

    public double point(Vector2 v){
        return (x*v.x+y*v.y);
    }

    public double cross(Vector2 v){
        return (x*v.y-y*v.x);
    }

    /**
     * Rotates mathematically positive (counter-clockwise).
     * @param angle Angle in radians
     */
    public void rotate(double angle){
        double xFnord, yFnord;
        angle = Math.toRadians(angle);
        double sin=Math.sin(angle), cos=Math.cos(angle);
        xFnord = cos*x-sin*y;
        yFnord = sin*x+cos*y;
        x = xFnord;
        y = yFnord;
    }

}
