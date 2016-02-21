package Tools;

import javafx.scene.image.Image;

/**
 * Created by Basti on 10.01.2016.
 */
public abstract class Tool {
    private Image preview, brush;
    private int size;

    //public abstract void applyTool();

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
}