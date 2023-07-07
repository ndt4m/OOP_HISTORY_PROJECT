package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class eraScreenTest extends Application
{
    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("eraScreen.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}