package newApp.App;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{

    private static Scene scene;

    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/MainScreen.fxml"));
        Parent rootParent;
        try {
            rootParent = loader.load();
            scene = new Scene(rootParent);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene() {
        return scene;
    }

}