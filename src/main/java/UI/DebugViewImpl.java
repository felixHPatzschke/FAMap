package UI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by yenon on 02/08/16.
 */
public class DebugViewImpl implements DebugView{

    @FXML
    private Label debugLabel;

    private GLContextThread contextThread;
    private Stage primaryStage;
    private long time;
    private double avgTime=16;

    @Override
    public void setContext(GLContextThread thread) {
        this.contextThread=thread;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage = new Stage();
                try {
                    primaryStage.setX(10);
                    primaryStage.setY(10);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/debug.fxml"));
                    loader.setController(DebugViewImpl.this);
                    Parent p = loader.load();
                    primaryStage.setTitle("Debug");
                    primaryStage.setScene(new Scene(p));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void beforeRender() {
        time=System.currentTimeMillis();
    }

    @Override
    public void afterRender() {
        avgTime=((avgTime*59d)+(System.currentTimeMillis()-time))/60d;
    }

    @Override
    public void render() {
        Platform.runLater(() -> debugLabel.setText(
                "avg: " + avgTime + "ms\n"));
    }

    @Override
    public void toggleDisplay(){
        Platform.runLater(() -> {
            if(primaryStage.isShowing()){
                primaryStage.hide();
            }else {
                primaryStage.show();
            }
        });
    }
}
