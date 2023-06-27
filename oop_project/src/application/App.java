package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;

import collection.EraCollection;
import collection.EventCollection;
import collection.FestivalCollection;
import collection.HistoricalCharCollection;
import collection.HistoricalSiteCollection;

public class App extends Application {
    public static EraCollection eraCollection = new EraCollection();
    public static HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
    public static HistoricalSiteCollection hissHistoricalSiteCollection = new HistoricalSiteCollection();
    public static EventCollection eventCollection = new EventCollection();
    public static FestivalCollection festivalCollection = new FestivalCollection();

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
            String passedInPath = "./src" + path;
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
        // eraCollection.loadJsonFiles();
        // eventCollection.loadJsonFiles();
        // festivalCollection.loadJsonFiles();
        // historicalCharCollection.loadJsonFiles();
        // hissHistoricalSiteCollection.loadJsonFiles();
    }

    /*  WARNING */
    /* Không chạy main của class này */
    public static void main(String[] args) {
        launch(args);
    }
}