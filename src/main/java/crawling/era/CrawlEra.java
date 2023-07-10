package crawling.era;

import java.io.IOException;

import collection.EntityCollection;
import collection.EraCollection;
import crawling.Crawler;
import entity.Era;


public abstract class CrawlEra extends Crawler
{
    private EntityCollection<Era> eraCollection = new EraCollection();

    public EraCollection getEraCollection() 
    {
        if (eraCollection instanceof EraCollection)
        {
            return (EraCollection) eraCollection;
        }
        return null;
    }

    public void setEraCollection(List<Era> eras)
    {
        this.eraCollection.setData(eras);
    }

    public abstract void crawlEra() throws IOException;
}
