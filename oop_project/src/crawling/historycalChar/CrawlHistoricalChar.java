package crawling.historycalChar;

import crawling.Crawler;
import entity.HistoricalCharacter;

import java.io.IOException;
import collection.EntityCollection;
import collection.HistoricalCharCollection;

public abstract class CrawlHistoricalChar extends Crawler
{
    protected EntityCollection<HistoricalCharacter> historicalCharCollection = new HistoricalCharCollection();

    public HistoricalCharCollection getHistoricalCharCollection() 
    {
        if (historicalCharCollection instanceof HistoricalCharCollection)
        {
            return (HistoricalCharCollection) historicalCharCollection;
        }
        return null;
    }

    public abstract void crawlHistoricalChar() throws IOException;
}
