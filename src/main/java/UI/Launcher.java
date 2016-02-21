package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Game on 01.01.2016.
 */
public class Launcher extends Application{
    public static void main(String[] args){
        setNatives();
        launch();
    }

    public static void setNatives(){
        Path p = new File("natives").toPath();
        if(Files.isDirectory(p)) {
            System.out.println("Using local natives!");
            System.setProperty("org.lwjgl.librarypath", "natives");
        }else{
            System.setProperty("org.lwjgl.librarypath", "E:\\Documents\\Projects\\_LIBS\\LWJGL3.0.0b\\natives");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent p = loader.load();
        MainUIController controller = loader.getController();
        primaryStage.setTitle("FAHeightmap");
        primaryStage.setScene(new Scene(p));
        controller.setStage(primaryStage);
        primaryStage.show();
    }
}