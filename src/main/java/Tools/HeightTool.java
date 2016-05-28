package Tools;

import javafx.scene.image.Image;

/**
 * Created by Basti on 10.01.2016.
 */
public class HeightTool extends Tool{

    public HeightTool() {
        super("Height Tool", new Image(HeightTool.class.getResourceAsStream("/tools/brush.png")));
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
