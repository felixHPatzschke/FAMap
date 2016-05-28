package UI;

import Tools.Tool;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Created by Basti on 27.05.2016.
 */
public class ToolCell extends ListCell<Tool> {

    @FXML
    private ImageView toolIcon;

    @FXML
    private Label toolName;

    public ToolCell(){
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tool.fxml"));
        loader.setController(this);
        try {
            setGraphic(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(Tool item, boolean empty) {
        super.updateItem(item, empty);
        if(item!=null) {
            toolIcon.setImage(item.getPreview());
            toolName.setText(item.getName());
        }
    }
}
