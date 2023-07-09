package collection;

import entity.Festival;
import entity.Entity;
import utils.JsonUtils;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.nio.file.Path;

public class FestivalCollection extends EntityCollection<Festival>
{
    public final static String DIR_NAME = "\\Festival";
    
    public void toJsonFiles()
    {
        for (Entity festival: this.getData())
        {
            String fileName = FestivalCollection.DIR_NAME + "\\" + festival.getId() + ".json";
            JsonUtils.toJsonFile(fileName, festival);
        }
    }

    public void loadJsonFiles() throws IOException
    {
        List<Festival> festivalList = new ArrayList<Festival>();
        Stream<Path> paths = Files.list(Paths.get(JsonUtils.PREFIX_URL + FestivalCollection.DIR_NAME));
        paths.forEach(path -> {
            festivalList.add(JsonUtils.fromJsonFile(path.toString(), Festival.class));
            });
        paths.close();
        setData(festivalList);
    }
}
