package collection;

import entity.Entity;
import entity.Era;
import utils.JsonUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;


public class EraCollection extends EntityCollection<Era> implements EraJsonFilesHandling
{
    public void toJsonFiles()
    {
        for (Entity era: this.getData())
        {
            String fileName = EraCollection.DIR_NAME + "\\" + era.getId() + ".json";
            JsonUtils.toJsonFile(fileName, era);
        }
    }

    public void loadJsonFiles() throws IOException
    {
        List<Era> eraList = new ArrayList<Era>();
        Stream<Path> paths = Files.list(Paths.get(JsonUtils.PREFIX_URL + DIR_NAME));
        paths.forEach(path -> {
            eraList.add(JsonUtils.fromJsonFile(path.toString(), Era.class));
        });
        paths.close();
        setData(eraList);
    }

    public static void main(String[] args) throws IOException
    {
        EraCollection eraCollection = new EraCollection();
        eraCollection.loadJsonFiles();
    }
}