package crawling.historicalsite;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import entity.HistoricalSite;

public class CrawlHistoricalSiteFromNKS extends CrawlHistoricalSite
{
    public List<String> extractAliasesFrom(String s)
    {
        List<String> result = new ArrayList<String>();

        String regex = "(?:hay còn gọi là |còn được gọi là |còn gọi là |lúc đầu được gọi là |gọi tắt là |tên gọi trước đây là |\bhay\b |hoặc |còn có các tên gọi là )(.+?)[(,\\.)]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find() && matcher.group(1) != null)
        {
            if (matcher.group(1).isEmpty())
            {
                continue;
            }

            result.add(matcher.group(1).split(" là")[0]);
        }
        return result;
    }

    public String extractEstablishmentFrom(String s)
    {
        String regex = "(?:xây dựng từ thời|dưới thời|Quyết định xếp hạng là Khu di tích|năm \\d+).+?\\.";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find() || matcher.group(0).isEmpty())
        {
            return "Không rõ";
        }
        return matcher.group(0);
    }

    public String extractLocationFrom(String s)
    {
        String regex = "xã .+?\\.|nằm ở .+?\\.|(?:nằm )*trên địa (?:bàn|phận) .+?\\.|phần đất phía.+?\\.";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find() || matcher.group(0).isEmpty())
        {
            return "Không rõ";
        }

        return matcher.group(0);
    }


    public boolean isRelatedCharacter(String s)
    {
        return s.equals("Kỹ sư công chính") ||
               s.equals("Người sáng lập")   ||
               s.equals("Giám đốc")         ||
               s.equals("Thầy")             ||
               s.equals("Kiến trúc sư");
    }

    public boolean isAliases(String s)
    {
        return s.equals("Tên cũ");
    }

    public boolean isCategory(String s)
    {
        return s.equals("Phân loại") ||
               s.equals("Kiểu");
    }


    public boolean isLocation(String s)
    {
        return s.equals("Vị trí")    ||
               s.equals("Địa chỉ")   ||
               s.equals("Địa điểm")  ||
               s.equals("Khu vực");
    }

    public boolean isEstablishment(String s)
    {
        return s.equals("Thành lập") ||
               s.equals("Khởi lập")  ||
               s.equals("Hoàn thành")||
               s.equals("Công nhận") ||
               s.equals("Xây dựng");
    } 

    public List<String> crawlAllUrlsFrom(String url) throws IOException
    {
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < 3; i++)
        {
            Document doc = Jsoup.connect(url + i * 10).get();
            for (Element a : doc.select("#content > div.com-tags-tag.tag-category > div.com-tags__items > ul > li > a"))
            {
                urls.add("https://nguoikesu.com" + a.attr("href"));
                //System.out.println("https://nguoikesu.com" + a.attr("href"));
                //break;
            }
        }
        return urls;
    }


    public HistoricalSite crawlHistoricalSiteFrom(String url) throws IOException
    {   
        FileWriter fw = new FileWriter("outputHistoricalSiteNKS.txt",true);

        Document doc = Jsoup.connect(url).get();

        String historicalSiteName = "Không rõ";
        Set<String> aliases = new HashSet<String>(); 
        String location = "Không rõ";
        String establishment = "Không rõ";
        String category  = "Không rõ";
        String overview = "Không rõ";
        Set<String> relatedCharacters = new HashSet<String>();
        
        Element headline = doc.selectFirst("h2[itemprop=headline]");
        if (headline != null)
        {
            historicalSiteName = headline.text();
        }

        for (Element a : doc.selectFirst("#content > div.com-content-article.item-page.page-list-items > div.com-content-article__body").select("a"))
        {
            if (a.attr("href").contains("nhan-vat") && !a.attr("href").contains("nha-"))
            {
                relatedCharacters.add(a.text());
            }
        }
        
        
        Element infobox = doc.selectFirst("div[class=infobox]");
        if (infobox != null)
        {   
            Element tbody = infobox.selectFirst("tbody");
            tbody.select("sup").remove();
            for (Element e : tbody.children())
            {   
                Element thTag = e.selectFirst("th");
                if (thTag == null)
                {
                    continue;
                }

                if (isEstablishment(thTag.text()) && e.selectFirst("td") != null)
                {
                    establishment = e.selectFirst("td").text();
                }
                else if (isLocation(thTag.text()) && e.selectFirst("td") != null)
                {
                    location = e.selectFirst("td").text();
                }
                else if (isCategory(thTag.text()))
                {
                    category = e.selectFirst("td").text();
                }
                else if (isAliases(thTag.text()))
                {
                    aliases.add(e.selectFirst("td").text());
                }
                else if (isRelatedCharacter(thTag.text()))
                {
                    relatedCharacters.add(e.selectFirst("td").text());
                }
                else if (thTag.text().equals("Thờ phụng"))
                {
                    if (e.nextElementSibling().text().isEmpty())
                    {
                        relatedCharacters.add(e.nextElementSibling().nextElementSibling().text());
                    }
                    else
                    {
                        relatedCharacters.add(e.nextElementSibling().text());
                    }
                }
            }
        }


        Element articleBody = doc.selectFirst("div[class=com-content-article__body]");
        
        String firstTwo_pTagContent = "";
        int i = 0;
        for (Element e : articleBody.children())
        {
            if (i == 2) {break;}
            if (e.tagName().equals("p") && !e.text().isEmpty())
            {   
                e.select("sup").remove();
                firstTwo_pTagContent += e.text();
                i++;
            }
        }


        overview = firstTwo_pTagContent;

        if (location.equals("Không rõ"))
        {
            location = extractLocationFrom(firstTwo_pTagContent);
        }

        if (establishment.equals("Không rõ"))
        {
            establishment = extractEstablishmentFrom(firstTwo_pTagContent);
        }

        for (String name : extractAliasesFrom(firstTwo_pTagContent))
        {
            aliases.add(name);
        }

        fw.write("historicalSiteName: " + historicalSiteName + "\n");
        fw.write("aliases: " + aliases + "\n");
        fw.write("location: " + location + "\n");
        fw.write("establishment: " + establishment + "\n");
        fw.write("category: " + category + "\n");
        fw.write("relatedCharacters: " + relatedCharacters + "\n");
        fw.write("overview: " + overview + "\n");
        fw.write("=========================================================================================" + "\n");
        fw.close();

        return new HistoricalSite(historicalSiteName, 
                                  location, 
                                  establishment, 
                                  category, 
                                  overview, 
                                  aliases, 
                                  relatedCharacters);
    }


    public void crawlAllHistoricalSite(List<String> urls) throws IOException
    {
        List<HistoricalSite> historicalSiteList = new ArrayList<HistoricalSite>();
        for (String url: urls)
        {
            historicalSiteList.add(crawlHistoricalSiteFrom(url));
            //break;
        }
        this.historicalSiteCollection.setData(historicalSiteList);
    }

    public void crawlHistoricalSite() throws IOException 
    {
        List<String> urls = this.crawlAllUrlsFrom("https://nguoikesu.com/di-tich-lich-su?types[0]=1&start=");
        this.crawlAllHistoricalSite(urls);
    }

    public static void main(String[] args)  throws IOException
    {
        CrawlHistoricalSiteFromNKS crawler = new CrawlHistoricalSiteFromNKS();
        crawler.crawlHistoricalSite();
        System.out.println(crawler.getHistoricalSiteCollection().getData().get(0).getEntityName());
    }   
}
