package crawling.historicalsite;

import java.io.PrintStream;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.HistoricalSite;


public class CrawlHistoricalSiteFromSecondWikiLink extends CrawlHistoricalSite
{
    public List<String> extractOverviewAndRelatedChar(Elements aTags) throws IOException
    {
        List<String> result = new ArrayList<String>();
        result.add("Không rõ");
        result.add("Không rõ");
        String overview = "";

        for (Element aTag : aTags)
        {
            if (aTag.attr("title").contains("trang không tồn tại"))
            {
                continue;
            }

            if (aTag.hasClass("external text"))
            {
                continue;
            }

            String url = "https://vi.wikipedia.org" + aTag.attr("href");
            Document doc = Jsoup.connect(url).get();
            doc.select("sup").remove();

            Element first_pTag = doc.selectFirst("p");
            if (first_pTag == null)
            {
                continue;
            }

            result.set(0, "Rõ");
            overview += first_pTag.text() + "\n";
            for (String name: this.extractPersonalNameEntityFrom(doc.select("#mw-content-text > div.mw-parser-output > p").text()))
            {
                result.add(name);
            }
        }

        if (result.get(0).equals("Rõ"))
        {
            result.set(1, overview.replaceAll("\\n$", ""));
        }
        return result;
    }

    public void crawlHistoricalSiteFrom(String url) throws IOException
    {
        List<HistoricalSite> historicalsiteList = new ArrayList<HistoricalSite>();

        PrintStream outPutStreamHistorycalSite = new PrintStream("outputHistoricalSiteWiki2.txt");

        Document doc = Jsoup.connect(url).get();

        String historicalSiteName = "Không rõ";
        Set<String> aliases = new HashSet<String>();
        String location = "Không rõ";
        String establishment = "Không rõ";
        String category  = "Không rõ";
        String overview = "Không rõ";
        Set<String> relatedCharacters = new HashSet<String>();

        Elements wikitables = doc.select("#mw-content-text > div.mw-parser-output > table:gt(13) > tbody");
        wikitables.select("sup").remove();
        for (Element wikitable : wikitables)
        {
            for (int i = 1; i < wikitable.children().size(); i++)
            {
                Element ith_Row = wikitable.child(i);
                Elements aTags;
                List<String> result = new ArrayList<String>();
                if (wikitable.child(0).childrenSize() == 5)
                {
                    if (!ith_Row.child(0).text().isEmpty())
                    {
                        historicalSiteName = ith_Row.child(0).text();
                    }

                    if (!ith_Row.child(2).text().isEmpty())
                    {
                        location = ith_Row.child(2).text();
                    }

                    if (!ith_Row.child(3).text().isEmpty())
                    {
                        category = ith_Row.child(3).text();
                    }

                    aTags = ith_Row.child(0).select("a");
                    if (aTags != null)
                    {
                        result = extractOverviewAndRelatedChar(aTags);
                        if (!result.get(0).equals("Không rõ"))
                        {
                            overview = result.get(1);

                            for (int j = 2; j < result.size(); j++)
                            {
                                relatedCharacters.add(result.get(j));
                            }
                        }
                    }
                }
                else if (wikitable.child(0).childrenSize() == 3)
                {
                    if (!ith_Row.child(0).text().isEmpty())
                    {
                        historicalSiteName = ith_Row.child(0).text();
                    }

                    if (!ith_Row.child(1).text().isEmpty())
                    {
                        location = ith_Row.child(1).text();
                    }

                    if (!ith_Row.child(2).text().isEmpty())
                    {
                        category = ith_Row.child(2).text();
                    }

                    aTags = ith_Row.child(0).select("a");
                    if (aTags != null)
                    {
                        result = extractOverviewAndRelatedChar(aTags);
                        if (!result.get(0).equals("Không rõ"))
                        {
                            overview = result.get(1);

                            for (int j = 2; j < result.size(); j++)
                            {
                                relatedCharacters.add(result.get(j));
                            }
                        }
                    }
                }

                outPutStreamHistorycalSite.println("historicalSiteName: " + historicalSiteName);
                outPutStreamHistorycalSite.println("Location: " + location);
                outPutStreamHistorycalSite.println("establishment: " + establishment);
                outPutStreamHistorycalSite.println("Category: " + category);
                outPutStreamHistorycalSite.println("aliase: " + aliases);
                outPutStreamHistorycalSite.println("RelatedCharacter: " + relatedCharacters);
                outPutStreamHistorycalSite.println("overview: " + overview);
                outPutStreamHistorycalSite.println("================================================================================");

                historicalsiteList.add(new HistoricalSite(historicalSiteName,
                        location,
                        establishment,
                        category,
                        overview,
                        aliases,
                        relatedCharacters));

                historicalSiteName = "Không rõ";
                location = "Không rõ";
                establishment = "Không rõ";
                category = "Không rõ";
                overview = "Không rõ";
                aliases.clear();
                relatedCharacters.clear();
            }
        }
        //this.historicalSiteCollection.setData(historicalsiteList);
        this.setHistoricalSiteCollection(historicalsiteList);
        // outPutStream.close();
        outPutStreamHistorycalSite.close();
    }

    public void crawlHistoricalSite() throws IOException
    {
        this.crawlHistoricalSiteFrom("https://vi.wikipedia.org/wiki/Di_t%C3%ADch_qu%E1%BB%91c_gia_%C4%91%E1%BA%B7c_bi%E1%BB%87t_(Vi%E1%BB%87t_Nam)");
    }

    public static void main(String[] args) throws IOException
    {
        CrawlHistoricalSiteFromSecondWikiLink crawler = new CrawlHistoricalSiteFromSecondWikiLink();
        crawler.crawlHistoricalSite();
        System.out.println(crawler.getHistoricalSiteCollection().getData().get(0).getEntityName());
    }
}
