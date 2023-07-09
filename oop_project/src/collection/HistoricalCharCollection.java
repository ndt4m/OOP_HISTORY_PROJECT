package collection;

import entity.HistoricalCharacter;
import entity.Entity;
import utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.nio.file.Path;

public class HistoricalCharCollection extends EntityCollection<HistoricalCharacter>
{
    public final static String DIR_NAME = "\\HistoricalCharacter";
    
    public void toJsonFiles()
    {
        for (Entity historicalchar: this.getData())
        {
            String fileName = HistoricalCharCollection.DIR_NAME + "\\" + historicalchar.getId() + ".json";
            JsonUtils.toJsonFile(fileName, historicalchar);
        }
    }

    public void loadJsonFiles() throws IOException
    {
        List<HistoricalCharacter> historicalcharList = new ArrayList<HistoricalCharacter>();
        Stream<Path> paths = Files.list(Paths.get(JsonUtils.PREFIX_URL + HistoricalCharCollection.DIR_NAME));
        paths.forEach(path -> {
            historicalcharList.add(JsonUtils.fromJsonFile(path.toString(), HistoricalCharacter.class));
            });
        paths.close();
        setData(historicalcharList);
    }
}
