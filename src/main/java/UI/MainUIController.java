package UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Basti on 03.01.2016.
 */
public class MainUIController {

    private Lwjgl lwjgl;

    private Stage stage;

    public void setStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    public void initialize(){
        lwjgl = new Lwjgl();
        lwjgl.start();
    }

    @FXML
    public void loadMap(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SupCom-Map","*.scmap"));
        File f = fc.showOpenDialog(stage);
        if(f!=null) {
            lwjgl.loadMap(f);
        }
    }
}
