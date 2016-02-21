package UI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static UI.Logger.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 * Created by Game on 01.01.2016.
 */
public class Launcher extends Application{

    public static void main(String[] args){
        Settings.init();
        try {
            logInit(Settings.logger_console_output, Settings.logger_file_output);
        } catch (IOException e) {
            return;
        }
        setNatives();
        launch();
    }

    public static void setNatives(){
        Path p = new File("natives").toPath();
        if(Files.isDirectory(p)) {
            logOut("Using local natives");
            System.setProperty("org.lwjgl.librarypath", "natives");
        }else{
            logOut("Using external natives");
            //System.setProperty("org.lwjgl.librarypath", "C:\\LWJGL\\lwjgl-3.0.0\\native");
            System.setProperty("org.lwjgl.librarypath", "E:\\Documents\\Projects\\_LIBS\\LWJGL3.0.0b\\natives");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // TODO terminate glfw
                exit();
            }
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent p = loader.load();
        MainUIController controller = loader.getController();
        primaryStage.setTitle("FAHeightmap");
        primaryStage.setScene(new Scene(p));
        controller.setStage(primaryStage);
        primaryStage.show();
    }

    public static void exit(int exitValue)
    {
        Settings.exportCONFIG();


        System.exit(exitValue);
    }

    public static void exit()
    {
        exit(0);
    }

}