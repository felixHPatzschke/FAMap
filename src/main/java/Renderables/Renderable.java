package Renderables;

/**
 * Created by Dev on 17.02.2016.
 */
public abstract class Renderable extends Moveable {
    public abstract void render(Camera4 camera);

    public abstract void remove();

    public boolean isRenderable(){
        return true;
    }

    public float getTransparency(){
        return 1;
    }
}
