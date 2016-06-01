package Tools;

import javafx.scene.image.Image;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * Created by Basti on 10.01.2016.
 */
public abstract class Tool {
    private Image preview, brush;
    private String name;
    private int size;
    protected static final int
            MOUSE_BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT,
            MOUSE_BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
            MOUSE_BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT
    ;

    Tool(String name,Image preview){
        this.name=name;
        this.preview=preview;
    }

    public abstract Rectangle applyTool(int keycode,Vector2f pos);

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
}