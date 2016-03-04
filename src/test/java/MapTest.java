import UI.Launcher;
import UI.Lwjgl;
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
        Lwjgl lw = new Lwjgl();
        lw.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                throw new RuntimeException(e);
            }
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
