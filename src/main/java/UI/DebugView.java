package UI;

/**
 * Created by yenon on 02/08/16.
 */
public interface DebugView {
    void setContext(GLContextThread thread);
    void beforeRender();
    void afterRender();
    void render();
    void toggleDisplay();
}
