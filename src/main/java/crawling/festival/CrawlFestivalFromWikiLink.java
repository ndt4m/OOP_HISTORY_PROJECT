package crawling.festival;

import java.io.PrintStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.Festival;


public class CrawlFestivalFromWikiLink extends CrawlFestival
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

    public List<String> extractRelatedCharacters(String s)
    {
        List<String> result = new ArrayList<String>();
        String regex = "[\\p{L} ]+?(?=,|và|\\)|\\.|$)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find())
        {
            if (matcher.group(0).isEmpty())
            {
                continue;
            }

            result.add(matcher.group(0).replace("và", "").trim());
        }
        
        return result;
    }

    public Map<String, String> extractFestivalNameAndTime(String s)
    {
        Map<String, String> result = new HashMap<String, String>();
        String regex = "((?:Dolta.+?|lễ.+?|hội.+?|kỷ niệm.+?|ngày giỗ.+?|liên hoan.+?|đền.+?|rạch.+?|ngày.+?|giỗ.+?|tết.+?))((?:(?:[\\d \\-]+)?(?:từ |(?:hậu |cuối |giữa )?tháng |tháng \\d+ và(?: (?:\\d+ )?tháng )?|\\d+ đến \\d+ tháng|[\\d\\/]+ và|ngày \\d+ – \\d+ tháng [\\p{L}\\d]+ và ngày \\d+ – \\d+ tháng [\\p{L}\\d]+|[\\d \\/]+ âm lịch|cuối tháng[\\d ]+ đầu tháng)*[\\d+\\,-\\/ ]+)(?:âm lịch(?: trở đi| chính hội)?|dương lịch|al|khoảng 7 ngày))|(?:lễ|tết|hội).+?(?=,|$)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find())
        {
            
            if (matcher.group(1) != null)
            {
                result.put(matcher.group(1).trim(), matcher.group(2).trim());
            }
            else
            {
                result.put(matcher.group(0).trim(), "Không rõ");
            }
        }

        return result;
    }

    public void crawlFestivalFrom(String url) throws IOException
    {
        List<Festival> festivalList = new ArrayList<>();

        PrintStream outPutStream = new PrintStream("outputFestivalWiki.txt");

        String festivalName = "Không rõ";
        Set<String> aliases = new HashSet<String>(); 
        String time = "Không rõ";
        String location = "Không rõ";
        String overview = "Không rõ";
        Set<String> relatedCharacters = new HashSet<String>();

        Document doc = Jsoup.connect(url).get();
        Elements liTags = doc.select("#mw-content-text > div.mw-parser-output > ul:nth-child(27) > li");
        liTags.remove(liTags.size() - 1);
        liTags.select("sup").remove();
        //System.out.println(liTags.get(liTags.size()-1).text());

        for (Element liTag : liTags)
        {
            String[] splited_liContent = liTag.text().split(":");
            splited_liContent[1] = splited_liContent[1].replaceAll("\\(.+?\\)", "");
            location = splited_liContent[0];
    
            for (Map.Entry<String, String> set : extractFestivalNameAndTime(splited_liContent[1]).entrySet())
            {
                
                festivalName = set.getKey();
                time = set.getValue();

                outPutStream.println("festivalName: " + festivalName);
                outPutStream.println("aliases: " + aliases);
                outPutStream.println("time: " + time);
                outPutStream.println("location: " + location);
                outPutStream.println("overview: " + overview);
                outPutStream.println("relatedCharacters: " + relatedCharacters);
                outPutStream.println("=============================================================================================");
                
                festivalList.add(new Festival(festivalName, 
                                              location, 
                                              time, 
                                              overview, 
                                              aliases, 
                                              relatedCharacters));
                
                festivalName = "Không rõ";
                time = "Không rõ";
            }
            location = "Không rõ";

        }


        Elements wikiTableRow = doc.select("#mw-content-text > div.mw-parser-output > table.prettytable.wikitable > tbody > tr");
        wikiTableRow.select("sup").remove();
        for (int i = 1; i < wikiTableRow.size(); i++)
        {
            Element row = wikiTableRow.get(i);
            if (!row.child(2).text().isEmpty())
            {
                festivalName = row.child(2).text();
            }

            if (!row.child(1).text().isEmpty())
            {
                location = row.child(1).text();
            }

            if (!row.child(0).text().isEmpty())
            {
                time = row.child(0).text();
                if (!row.child(5).text().isEmpty())
                {
                    time += " - " + row.child(5).text();
                }
            }

            if (!row.child(4).text().isEmpty())
            {
                for (String charName : extractRelatedCharacters(row.child(4).text()))
                {
                    relatedCharacters.add(charName);
                }
            }

            List<String> result;
            Elements aTags = row.child(2).select("a");
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
            outPutStream.println("festivalName: " + festivalName);
            outPutStream.println("aliases: " + aliases);
            outPutStream.println("time: " + time);
            outPutStream.println("location: " + location);
            outPutStream.println("overview: " + overview);
            outPutStream.println("relatedCharacters: " + relatedCharacters);
            outPutStream.println("=============================================================================================");
            
            festivalList.add(new Festival(festivalName, 
                                              location, 
                                              time, 
                                              overview, 
                                              aliases, 
                                              relatedCharacters));
            
            
            festivalName = "Không rõ";
            aliases.clear();
            time = "Không rõ";
            location = "Không rõ";
            overview = "Không rõ";
            relatedCharacters.clear();
        }
        this.festivalCollection.setData(festivalList);
        outPutStream.close();

    }

    public void crawlFestival() throws IOException
    {
        this.crawlFestivalFrom("https://vi.wikipedia.org/wiki/L%E1%BB%85_h%E1%BB%99i_Vi%E1%BB%87t_Nam");
    }

    public static void main(String[] args) throws IOException
    {
        CrawlFestivalFromWikiLink crawlerFesWiki = new CrawlFestivalFromWikiLink();
        crawlerFesWiki.crawlFestival();
        System.out.println(crawlerFesWiki.getFestivalCollection().getData().get(0).getEntityName());
    }
}
