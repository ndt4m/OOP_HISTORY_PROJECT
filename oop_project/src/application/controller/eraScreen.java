package application.controller;

import java.io.IOException;

import gui.controller.eraScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class eraScreen extends Application
{
    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/EraScreen.fxml"));
        System.out.println(loader.toString());
        Parent root;
        try {

            root = loader.load();
            System.out.println("2jljafkljalk");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
