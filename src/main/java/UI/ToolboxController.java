package UI;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Basti on 03.01.2016.
 */
public class ToolboxController {

    private static GLContextThread GLContextThread;

    private Stage stage;

    public void setStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    public void initialize(){
        GLContextThread = new GLContextThread();
        GLContextThread.start();
    }

    @FXML
    public void loadMap(){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(Settings.path));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SupCom-Map","*.scmap"));
        File f = fc.showOpenDialog(stage);
        if(f!=null) {
            Settings.path = f.getParent();
            try {
                GLContextThread.loadMap(new FileInputStream(f),f.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exit(int exitValue)
    {
        Settings.exportCONFIG();
        System.exit(exitValue);
    }

    public static void exit()
    {
        GLContextThread.stopThread();
    }
}
