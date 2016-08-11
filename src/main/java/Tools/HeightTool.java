package Tools;

import javafx.scene.image.Image;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;

/**
 * Created by Basti on 10.01.2016.
 */
public class HeightTool extends Tool{

    public HeightTool() {
        super("Height Tool", new Image(HeightTool.class.getResourceAsStream("/tools/brush.png")));
    }

    @Override
    public MapChangeHandler applyTool(MapChangeHandler handler, boolean left,boolean middle,boolean right, Vector2f pos) {
        Vector2i posInt = new Vector2i(Math.round(pos.x),Math.round(pos.y));
        if(left) {
            Rectangle area = new Rectangle(posInt.x - getSize(), posInt.y - getSize(), getSize() * 2, getSize() * 2);
            handler=Tool.iterateRectangle(area, handler, new PointIterator() {
                @Override
                void iterate(int x, int y, MapChangeHandler map) {
                    if(Math.pow((posInt.x-x),2)+Math.pow((posInt.y-y),2)<=getSize()*getSize()){
                        map.setHeightAt(x, y, map.getHeightAt(x, y)+10);
                    }
                }
            });
        }
        if(right) {
            Rectangle area = new Rectangle(posInt.x - getSize(), posInt.y - getSize(), getSize() * 2, getSize() * 2);
            handler=Tool.iterateRectangle(area, handler, new PointIterator() {
                @Override
                void iterate(int x, int y, MapChangeHandler map) {
                    if(Math.pow((posInt.x-x),2)+Math.pow((posInt.y-y),2)<=getSize()*getSize()){
                        map.setHeightAt(x, y, map.getHeightAt(x, y)-10);
                    }
                }
            });
        }
        return handler;
    }

    @Override
    public boolean hasSize() {
        return true;
    }

    @Override
    public boolean hasBrush() {
        return false;
    }
}
