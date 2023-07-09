package crawling.era;

import java.io.IOException;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import entity.Era;

public class CrawlEraFromVS extends CrawlEra
{
    public void crawlEventFrom(String url) throws IOException
    {
        List<Era> eraList = new ArrayList<Era>();

        PrintStream outPutStream = new PrintStream("outputEraVS.txt");

        String regex = "(?<=-)(?! *nước Việt Nam mới *| *thời tiền sử *)([^()]+?)\\((.+?)\\)|nước Việt Nam mới|thời tiền sử";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher;

        Map<String, String> eraNamesAndTimes = new HashMap<String, String>();
        String overview = "Không rõ";
        String founder = "Không rõ";
        String capital = "Không rõ";
        Set<String> aliases = new HashSet<String>();
        Set<String> relatedCharacters = new HashSet<String>();

        for (int i = 0; i < 120; i++)
        {
            Document doc = Jsoup.connect(url + i).get();
            for (Element second_tdTag : doc.select("body > div.ui.container > table > tbody > tr > td:nth-child(2)"))
            {
                matcher = pattern.matcher(second_tdTag.text());
                while (matcher.find())
                {
                    if (matcher.group(1) == null)
                    {
                        eraNamesAndTimes.put(matcher.group(0).trim(), "Không rõ");
                    }
                    else
                    {
                        eraNamesAndTimes.put(matcher.group(1).trim(), matcher.group(2).trim());
                    }
                }
            }
        }

        for (Map.Entry<String, String> eraNameAndTime : eraNamesAndTimes.entrySet())
        {
            outPutStream.println("eraName: " + eraNameAndTime.getKey());
            outPutStream.println("time: " + eraNameAndTime.getValue());
            outPutStream.println("overview: " + overview);
            outPutStream.println("founder: " + founder);
            outPutStream.println("capital: " + capital);
            outPutStream.println("aliases: " + aliases);
            outPutStream.println("relatedCharacters: " + relatedCharacters);
            outPutStream.println("===========================================================================");

            eraList.add(new Era(eraNameAndTime.getKey(),
                    eraNameAndTime.getValue(),
                    founder,
                    capital,
                    overview,
                    aliases,
                    relatedCharacters));
        }
        this.eraCollection.setData(eraList);
        outPutStream.close();

    }

    public void crawlEra() throws IOException
    {
        crawlEventFrom("https://vansu.vn/viet-nam/viet-nam-nhan-vat?page=");
    }

    public static void main(String[] args) throws IOException
    {
        CrawlEraFromVS crawlerVS = new CrawlEraFromVS();
        crawlerVS.crawlEra();
    }
}