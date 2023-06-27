package application;

import history.collection.Eras;
import history.collection.Events;
import history.collection.Festivals;
import history.collection.HistoricalFigures;
import history.collection.HistoricSites;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;

public class App extends Application {

    public static final String TOPSCREEN_PATH = "/application/view/MainScreen.fxml";

    /**
     * Chuyển path sang dạng URL để cho vào FXMLLoader
     * >> không phải cấu hình resource cho project
     *
     * @param path từ /src/main/java/...
     *             ("/application/fxml/MainScreen.fxml")
     * @return URL để cho vào FXMLLoader
     */
    public static URL convertToURL(String path) {
        try {
            String passedInPath = "./src/main/java" + path;
            URL url = FileSystems.getDefault().getPath(passedInPath)
                    .toUri().toURL();
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(convertToURL(TOPSCREEN_PATH));
        Scene scene = new Scene(root);
        stage.setTitle("History APP");
        stage.setScene(scene);
        stage.show();

        /* Loading all resources */
        Eras.loadJSON();
        Events.loadJSON();
        Festivals.loadJSON();
        HistoricalFigures.loadJSON();
        HistoricSites.loadJSON();
    }

    /*  WARNING */
    /* Không chạy main của class này */
    public static void main(String[] args) {
        launch(args);
    }
}