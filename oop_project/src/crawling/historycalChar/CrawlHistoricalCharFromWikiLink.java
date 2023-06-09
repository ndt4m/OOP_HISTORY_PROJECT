package crawling.historycalChar;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.HistoricalCharacter;

public class CrawlHistoricalCharFromWikiLink extends CrawlHistoricalChar
{
    public List<String> extractAliasesBy(List<Element> elements)
    {
        List<String> result = new ArrayList<String>();
        for (Element element : elements)
        {
            if (element.text().equals("không có"))
            {
                continue;
            }
            String[] texts = element.html().split("<br>");

            for (String text : texts) 
            {
                for (String name : text.split(","))
                {
                    if (name.equals("Không rõ"))
                    {
                        continue;
                    }
                    result.add(Jsoup.parse(name).text().replaceAll("\\(.+?\\)", "").trim());
                }
            }
            result.removeIf(String::isEmpty);
        }

        return result;
    }

    public String extractFatherNameAndMotherNameBy(String s)
    {
        String regex = "(?:con|Con).+?(\\p{Lu}[\\p{L} ]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find())
        {
            return "Không rõ";
        }

        return matcher.group(1);
    }

    public String extractOverviewBy(Element aTag) throws IOException
    {
        String result = "Không rõ";

        if (aTag.attr("title").contains("trang không tồn tại"))
        {
            return result;
        }

        String url = "https://vi.wikipedia.org" + aTag.attr("href");
        Document doc = Jsoup.connect(url).timeout(120000).get();
        doc.select("sup").remove();
        Element content = doc.selectFirst("#mw-content-text > div.mw-parser-output");
        for (Element element : content.children())
        {
            if (!element.tagName().equals("p") || element.text().isEmpty())
            {
                continue;
            }
            result = element.text();
            break;
        }
        return result;
    }

    public void crawlCharFrom(String url) throws IOException
    {
        List<HistoricalCharacter> historicalCharList = new ArrayList<HistoricalCharacter>();

        PrintStream outPutStream = new PrintStream("outputHistoricalCharWiki.txt");

        String charName = "Không rõ";
        String eraName = "Không rõ";
        String dateOfBirth = "Không rõ";
        String dateOfDeath = "Không rõ";
        String fatherName = "Không rõ";
        String motherName = "Không rõ";
        String hometown = "Không rõ";
        String occupation = "Không rõ";
        String workTenure = "Không rõ";
        String overview = "Không rõ";
        Set<String> aliases = new HashSet<String>();

        Document doc = Jsoup.connect(url).timeout(120000).get();
        doc.select("sup").remove();
        doc.selectFirst("#mw-content-text > div.mw-parser-output > div.thumb > div > table").remove();
        Elements h3Tags = doc.select("h3");
        Elements tableTags = doc.select("table[cellpadding=0]");

        for (int i = 0; i < tableTags.size(); i++)
        {   
            Elements rows = tableTags.get(i).select("tr");

            eraName = h3Tags.get(i).text().replaceAll("\\(.+?\\)", "").replaceAll("\\[.+?\\]", "").trim();
            occupation = rows.get(0).child(1).text();
            for (int j = 1; j < rows.size(); j++)
            {
                Element row = rows.get(j);
                if (row.select("td").size() == 10)
                {
                    for (String name: extractAliasesBy(row.select("td").subList(2, 6)))
                    {
                        aliases.add(name);
                    }

                    workTenure = row.child(7).text() + row.child(8).text() + row.child(9).text();
                    workTenure.replace("?", "Không rõ");
                    workTenure.trim();

                    fatherName = extractFatherNameAndMotherNameBy(row.child(6).text());

                    charName = row.child(1).text();

                    Element a = row.child(1).selectFirst("a");
                    if (a != null)
                    {
                        overview = extractOverviewBy(a);
                    }
                    aliases.remove("Không rõ");
                    aliases.remove("không rõ");
                    outPutStream.println("charName: " + charName);
                    outPutStream.println("eraName: " + eraName);
                    outPutStream.println("dateOfBirth: " + dateOfBirth);
                    outPutStream.println("dateOfDeath: " + dateOfDeath);
                    outPutStream.println("fatherName: " + fatherName);
                    outPutStream.println("motherName: " + motherName);
                    outPutStream.println("hometown: " + hometown);
                    outPutStream.println("occupation: " + occupation);
                    outPutStream.println("workTenure: " + workTenure);
                    outPutStream.println("overview: " + overview);
                    outPutStream.println("aliases: " + aliases);
                    outPutStream.println("===================================================================================================");
                    
                    historicalCharList.add(new HistoricalCharacter(charName, 
                                                                   eraName, 
                                                                   dateOfBirth, 
                                                                   dateOfDeath, 
                                                                   fatherName, 
                                                                   motherName, 
                                                                   hometown, 
                                                                   occupation, 
                                                                   workTenure, 
                                                                   overview, 
                                                                   aliases));
                    
                    charName = "Không rõ";
                    dateOfBirth = "Không rõ";
                    dateOfDeath = "Không rõ";
                    fatherName = "Không rõ";
                    motherName = "Không rõ";
                    hometown = "Không rõ";
                    workTenure = "Không rõ";
                    overview = "Không rõ";
                    aliases.clear();
                }
            }
            occupation = "Không rõ";
            eraName = "Không rõ";
        }
        this.historicalCharCollection.setData(historicalCharList);
        outPutStream.close();
    }

    public void crawlHistoricalChar() throws IOException
    {
        this.crawlCharFrom("https://vi.wikipedia.org/wiki/Vua_Vi%E1%BB%87t_Nam");
    }

    public static void main(String[] args) throws IOException
    {
        CrawlHistoricalCharFromWikiLink crawler = new CrawlHistoricalCharFromWikiLink();
        crawler.crawlHistoricalChar();
        System.out.println(crawler.getHistoricalCharCollection().getData().get(0).getEntityName());
    }
}
