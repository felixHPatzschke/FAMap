package UI;

/**
 * Created by yenon on 11/08/16.
 */
public class OS {
    public static final boolean WINDOWS;
    static {
        String OS = System.getProperty("os.name").toLowerCase();
        WINDOWS = OS.contains("win");
    }
}
