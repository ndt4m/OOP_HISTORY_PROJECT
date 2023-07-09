package crawling.era;

import java.io.IOException;

import collection.EntityCollection;
import collection.EraCollection;
import crawling.Crawler;
import entity.Era;


public abstract class CrawlEra extends Crawler
{
    protected EntityCollection<Era> eraCollection = new EraCollection();

    public EraCollection getEraCollection()
    {
        if (eraCollection instanceof EraCollection)
        {
            return (EraCollection) eraCollection;
        }
        return null;
    }

    public abstract void crawlEra() throws IOException;
}