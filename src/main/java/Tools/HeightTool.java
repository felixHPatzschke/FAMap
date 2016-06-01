package Tools;

import javafx.scene.image.Image;
import org.joml.Vector2f;

import java.awt.*;

/**
 * Created by Basti on 10.01.2016.
 */
public class HeightTool extends Tool{

    public HeightTool() {
        super("Height Tool", new Image(HeightTool.class.getResourceAsStream("/tools/brush.png")));
    }

    @Override
    public Rectangle applyTool(int keycode, Vector2f pos) {
        switch (keycode){
            case MOUSE_BUTTON_LEFT:

                break;
            case MOUSE_BUTTON_RIGHT:

                break;
        }
        return null;
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
