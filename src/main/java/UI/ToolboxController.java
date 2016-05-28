package UI;

import Tools.HeightTool;
import Tools.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Basti on 03.01.2016.
 */
public class ToolboxController {

    private static GLContextThread GLContextThread;

    private Stage stage;

    @FXML
    private ListView<Tool> toolList;

    public void setStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    public void initialize(){
        toolList.setCellFactory(new Callback<ListView<Tool>, ListCell<Tool>>() {
            @Override
            public ListCell<Tool> call(ListView<Tool> param) {
                return new ToolCell();
            }
        });
        toolList.getItems().add(new HeightTool());
        GLContextThread = new GLContextThread();
        GLContextThread.start();
    }

    @FXML
    public void loadMap(){
        FileChooser fc = new FileChooser();
        if(Files.isDirectory(Paths.get(Settings.path))) {
            fc.setInitialDirectory(new File(Settings.path));
        }
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
