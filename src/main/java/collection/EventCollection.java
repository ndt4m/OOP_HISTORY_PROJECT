package collection;

import entity.Event;
import entity.Entity;
import utils.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;


public class EventCollection extends EntityCollection<Event>
{
    public final static String DIR_NAME = "\\Event";

    public void toJsonFiles()
    {
        for (Entity event: this.getData())
        {
            String fileName = EventCollection.DIR_NAME + "\\" + event.getId() + ".json";
            JsonUtils.toJsonFile(fileName, event);
        }
    }

    public void loadJsonFiles() throws IOException
    {
        List<Event> eventList = new ArrayList<Event>();
        Stream<Path> paths = Files.list(Paths.get(JsonUtils.PREFIX_URL + EventCollection.DIR_NAME));
        paths.forEach(path -> {
            eventList.add(JsonUtils.fromJsonFile(path.toString(), Event.class));
        });
        paths.close();
        setData(eventList);
    }
}