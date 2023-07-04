package application.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class figureScreen extends Application{
     @Override
    public void start(Stage stage) {
        System.setProperty("file.encoding", "UTF-8");
        /*
        try {
            App.eraCollection.loadJsonFiles();
            App.eventCollection.loadJsonFiles();
            App.festivalCollection.loadJsonFiles();
            App.historicalCharCollection.loadJsonFiles();
            App.hissHistoricalSiteCollection.loadJsonFiles();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/HistoricalFiguresScreen.fxml"));
        System.out.println(loader.toString());
        Parent root;
        try {

            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);

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
