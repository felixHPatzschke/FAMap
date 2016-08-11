import UI.Launcher;
import UI.GLContextThread;
import UI.Settings;
import org.junit.Test;

/**
 * Created by Basti on 21.02.2016.
 */
public class MapTest {
    @Test
    public void openMap(){
        Settings.init();
        Launcher.setNatives();
        GLContextThread lw = new GLContextThread();
        lw.setUncaughtExceptionHandler((t, e) -> {
            throw new RuntimeException(e);
        });
        lw.start();
        lw.loadMap(this.getClass().getResourceAsStream("/barrier_islands.scmap"),"barrier_islands");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
