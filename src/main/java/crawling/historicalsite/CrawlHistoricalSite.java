package crawling.historicalsite;

import collection.EntityCollection;
import entity.HistoricalSite;
import collection.HistoricalSiteCollection;
import crawling.Crawler;
import java.io.IOException;

public abstract class CrawlHistoricalSite extends Crawler
{
    protected EntityCollection<HistoricalSite> historicalSiteCollection = new HistoricalSiteCollection();

    public HistoricalSiteCollection getHistoricalSiteCollection() 
    {
        if (historicalSiteCollection instanceof HistoricalSiteCollection)
        {
            return (HistoricalSiteCollection) historicalSiteCollection;
        }
        return null;
    }

    public abstract void crawlHistoricalSite() throws IOException;
}
