package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewStarter extends Application{
    @Override
    public void start(Stage stage) {
        System.setProperty("file.encoding", "UTF-8");
    
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/MainScreen.fxml"));
        System.out.println(loader.toString());
        Parent root;
        try {

            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
