package Tools;

import FAProps.FAMap;
import com.sun.istack.internal.Nullable;
import javafx.scene.image.Image;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.awt.Rectangle;


/**
 * Created by Basti on 10.01.2016.
 */
public abstract class Tool {
    private Image preview, brush;
    private String name;
    private int size = 10;

    Tool(String name,Image preview){
        this.name=name;
        this.preview=preview;
    }

    public abstract MapChangeHandler applyTool(@Nullable MapChangeHandler handler,boolean left,boolean middle,boolean right, Vector2f pos);

    public Image getPreview() {
        return preview;
    }

    public void setPreview(Image preview) {
        this.preview = preview;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public abstract boolean hasSize();

    public Image getBrush() {
        return brush;
    }

    public void setBrush(Image brush) {
        this.brush = brush;
    }

    public abstract boolean hasBrush();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Rectangle checkBounds(Rectangle area, MapChangeHandler handler){
        if(area.x<0){
            area.width=area.width-(0-area.x);
            area.x=0;
        }
        if(area.y<0){
            area.height=area.height-(0-area.y);
            area.y=0;
        }
        if(area.x>handler.getWidth()){
            area.x=handler.getWidth();
            area.width=0;
        }
        if(area.y>handler.getHeight()){
            area.y=handler.getHeight();
            area.height=0;
        }
        if(area.x+area.width>handler.getWidth()){
            area.width=(area.x+area.width)-handler.getWidth();
        }
        if(area.y+area.height>handler.getHeight()){
            area.height=(area.y+area.height)-handler.getHeight();
        }
        return area;
    }

    public abstract class PointIterator{
        abstract void iterate(int x,int y,MapChangeHandler map);
    }

    public static MapChangeHandler iterateRectangle(Rectangle rectangle,MapChangeHandler handler,PointIterator iterator){
        rectangle=checkBounds(rectangle,handler);
        int x,y=rectangle.y;
        while (y<rectangle.y+rectangle.height){
            x=rectangle.x;
            while (x<rectangle.x+rectangle.width){
                iterator.iterate(x,y,handler);
                x++;
            }
            y++;
        }
        return handler;
    }
}