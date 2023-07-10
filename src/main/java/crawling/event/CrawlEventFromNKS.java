package crawling.event;

import java.io.IOException;
import java.io.FileWriter;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.Event;

public class CrawlEventFromNKS extends CrawlEvent
{
    public List<String> crawlAllUrlsFrom(String url) throws IOException
    {
        List<String> urls = new ArrayList<String>();

        Document doc = Jsoup.connect(url).get();


        for (int i = 0; i < 15; i++)
        {
            doc = Jsoup.connect("https://nguoikesu.com/tu-lieu/quan-su?filter_tag[0]=&start=" + i*5).get();

            Elements aTags = doc.select("#content > div.com-content-category-blog.blog > div.com-content-category-blog__items.blog-items.items-leading > div > div > div > h2 > a");

            for (Element a: aTags)
            {
                urls.add("https://nguoikesu.com" + a.attr("href"));
            }
        }
        return urls;
    }

    public void crawlAllEventsFrom(List<String> urls) throws IOException
    {
        List<Event> eventList = new ArrayList<>();
        for (String url : urls)
        {
            eventList.add(crawlEventFrom(url));
        }
        //this.eventCollection.setData(eventList);
        this.setEventCollection(eventList);
    }

    public Event crawlEventFrom(String url) throws IOException
    {

        FileWriter fw = new FileWriter("outputEventNKS.txt",true);

        String eventName = "Không rõ";
        String location = "Không rõ";
        String time = "Không rõ";
        String result = "Không rõ";
        String overview = "Không rõ";
        Set<String> relatedCharacters = new HashSet<String>();
        Set<String> aliases = new HashSet<>();

        Document doc = Jsoup.connect(url).get();
        Element headLine = doc.selectFirst("h1[itemprop=headline]");
        System.out.println(headLine.text());//bí ẩn??????
        eventName = headLine.text().replaceAll("(?:năm|,) [\\d -]+|\\([\\d -]+\\)|\\d{4}", "").trim();

        for (Element a : doc.selectFirst("div[class=com-content-article item-page]").select("a"))
        {
            if (a.attr("href").contains("nhan-vat") && !a.attr("href").contains("nha-"))
            {
                relatedCharacters.add(a.text());
            }
        }


        Element contentBody = doc.selectFirst("div[class=com-content-article__body]");
        contentBody.select("sup").remove();

        for (int i = 0; i < contentBody.children().size(); i++)
        {
            Element currentElement = contentBody.child(i);
            if (currentElement.tagName().equals("p") && !currentElement.text().isEmpty() && currentElement.text().length() > 10)
            {
                overview = currentElement.text();
                break;
            }
        }

        Element infoTable = doc.selectFirst("table[cellpadding=0]");
        if (infoTable != null)
        {
            infoTable.select("sup").remove();
            for (Element row: infoTable.select("tr"))
            {
                if (row.child(0).text().equals("Thời gian"))
                {
                    time = row.child(1).text();
                }
                else if (row.child(0).text().equals("Địa điểm"))
                {
                    location = row.child(1).text();
                }
                else if (row.child(0).text().equals("Kết quả"))
                {
                    result = row.child(1).text();
                }
            }
        }

        fw.write("eventName: " + eventName + "\n");
        fw.write("location: " + location + "\n");
        fw.write("time: " + time + "\n");
        fw.write("result: " + result + "\n");
        fw.write("overview: " + overview + "\n");
        fw.write("relatedCharacters: " + relatedCharacters + "\n");
        fw.write("aliases: " + aliases + "\n");
        fw.write("=======================================================" + "\n");
        fw.close();

        return new Event(eventName,
                location,
                time,
                result,
                overview,
                aliases,
                relatedCharacters);
        // eventName = "Không rõ";
        // location = "Không rõ";
        // time = "Không rõ";
        // result = "Không rõ";
        // overview = "Không rõ";
        // relatedCharacters.clear();
        // aliases.clear();
    }

    public void crawlEvent() throws IOException
    {
        List<String> urls = this.crawlAllUrlsFrom("https://nguoikesu.com/tu-lieu/quan-su?filter_tag[0]=&start=0");
        this.crawlAllEventsFrom(urls);
    }

    public static void main(String[] args) throws IOException
    {
        CrawlEventFromNKS crawlerNKS = new CrawlEventFromNKS();
        crawlerNKS.crawlEvent();
        System.out.println(crawlerNKS.getEventCollection().getData().get(0).getEntityName());
    }
}
