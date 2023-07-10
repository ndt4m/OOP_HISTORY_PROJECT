package crawling.historycalChar;

import crawling.Crawler;
import entity.HistoricalCharacter;

import java.io.IOException;
import collection.EntityCollection;
import collection.HistoricalCharCollection;

public abstract class CrawlHistoricalChar extends Crawler
{
    private EntityCollection<HistoricalCharacter> historicalCharCollection = new HistoricalCharCollection();

    public HistoricalCharCollection getHistoricalCharCollection()
    {
        if (historicalCharCollection instanceof HistoricalCharCollection)
        {
            return (HistoricalCharCollection) historicalCharCollection;
        }
        return null;
    }

    public void setHistoricalCharCollection(List<HistoricalCharacter> historicalCharacters)
    {
        this.historicalCharCollection.setData(historicalCharacters);
    }

    public abstract void crawlHistoricalChar() throws IOException;
}
