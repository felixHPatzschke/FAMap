import UI.Launcher;
import UI.Lwjgl;
import org.junit.Test;

/**
 * Created by Basti on 21.02.2016.
 */
public class MapTest {
    @Test
    public void openMap(){
        Launcher.setNatives();
        Lwjgl lw = new Lwjgl();
        lw.start();
        lw.loadMap(this.getClass().getResourceAsStream("/barrier_islands.scmap"),"barrier_islands");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
