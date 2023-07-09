package crawling.festival;

import crawling.Crawler;

import collection.EntityCollection;
import entity.Festival;
import collection.FestivalCollection;
import java.io.IOException;

public abstract class CrawlFestival extends Crawler
{
    protected EntityCollection<Festival> festivalCollection = new FestivalCollection();

    public FestivalCollection getFestivalCollection() 
    {
        if (festivalCollection instanceof FestivalCollection)
        {
            return (FestivalCollection) festivalCollection;
        }
        return null;
    }

    public abstract void crawlFestival() throws IOException;
}
