package collection;

import entity.HistoricalSite;
import entity.Entity;
import utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.nio.file.Path;

public class HistoricalSiteCollection extends EntityCollection<HistoricalSite>
{
    public final static String DIR_NAME = "\\HistoricalSite";

    public void toJsonFiles()
    {
        for (Entity historicalsite: this.getData())
        {
            String fileName = HistoricalSiteCollection.DIR_NAME + "\\" + historicalsite.getId() + ".json";
            JsonUtils.toJsonFile(fileName, historicalsite);
        }
    }

    public void loadJsonFiles() throws IOException
    {
        List<HistoricalSite> historicalsiteList = new ArrayList<HistoricalSite>();
        Stream<Path> paths = Files.list(Paths.get(JsonUtils.PREFIX_URL + HistoricalSiteCollection.DIR_NAME));
        paths.forEach(path -> {
            historicalsiteList.add(JsonUtils.fromJsonFile(path.toString(), HistoricalSite.class));
        });
        paths.close();
        setData(historicalsiteList);
    }
}