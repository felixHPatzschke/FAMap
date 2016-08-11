package Renderables;

import static OpenGL.MapShader.FRAG_DEFAULT;
import static OpenGL.MapShader.FRAG_TOOL_BLOB;

/**
 * Created by Dev on 17.02.2016.
 */
public abstract class Renderable extends Moveable {
    public abstract void render(Camera camera);

    public int getFragEnum(){
        return FRAG_DEFAULT | FRAG_TOOL_BLOB;
    }

    public abstract void remove();

    public boolean isRenderable(){
        return true;
    }

    public float getTransparency(){
        return 1;
    }
}
