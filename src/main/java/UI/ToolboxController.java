package UI;

import Tools.HeightTool;
import Tools.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
        toolList.setCellFactory(param -> new ToolCell());
        toolList.getItems().add(new HeightTool());
        GLContextThread = new GLContextThread();
        GLContextThread.start();
    }

    @FXML
    public void loadMap(){
        File f;
        // #JavafxIsBrokenOnLinux
        if(OS.WINDOWS) {
            FileChooser fc = new FileChooser();
            if (Files.isDirectory(Paths.get(Settings.path))) {
                fc.setInitialDirectory(new File(Settings.path));
            }
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SupCom-Map", "*.scmap"));
            f = fc.showOpenDialog(stage);
        }else{
            JFileChooser fc = new JFileChooser(new File(Settings.path));
            fc.setFileFilter(new FileNameExtensionFilter("SupCom-Map","scmap"));
            if(fc.showOpenDialog(null)!=JFileChooser.APPROVE_OPTION){
                return;
            }
            f = fc.getSelectedFile();
        }
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
