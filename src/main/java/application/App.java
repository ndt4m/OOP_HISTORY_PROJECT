package application;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;

import collection.EraCollection;
import collection.EventCollection;
import collection.FestivalCollection;
import collection.HistoricalCharCollection;
import collection.HistoricalSiteCollection;

public class App{
    public static URL convertToURL(String path) {
        try {
            String passedInPath = "src/main/resources" + path;
            URL url = FileSystems.getDefault().getPath(passedInPath)
                    .toUri().toURL();
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}