package crawling.event;

import collection.EventCollection;
import crawling.Crawler;
import entity.Event;
import collection.EntityCollection;
import java.io.IOException;

public abstract class CrawlEvent extends Crawler
{
    private EntityCollection<Event> eventCollection = new EventCollection();

    public EventCollection getEventCollection()
    {
        if (eventCollection instanceof EventCollection)
        {
            return (EventCollection) eventCollection;
        }
        return null;
    }

    public void setEventCollection(List<Event> events)
    {
        this.eventCollection.setData(events);
    }
    
    public abstract void crawlEvent() throws IOException;
}
