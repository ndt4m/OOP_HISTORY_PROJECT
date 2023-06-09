package crawling.event;

import collection.EventCollection;
import crawling.Crawler;
import entity.Event;
import collection.EntityCollection;
import java.io.IOException;

public abstract class CrawlEvent extends Crawler 
{
    protected EntityCollection<Event> eventCollection = new EventCollection();

    public EventCollection getEventCollection() 
    {
        if (eventCollection instanceof EventCollection)
        {
            return (EventCollection) eventCollection;
        }
        return null;
    }

    public abstract void crawlEvent() throws IOException;
}
