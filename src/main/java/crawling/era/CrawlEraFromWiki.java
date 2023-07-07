package crawling.era;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import collection.EraCollection;

import java.io.PrintStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.Era;

public class CrawlEraFromWiki extends CrawlEra
{
    public String extractOverview(String href) throws IOException
    {
        String link = "https://vi.wikipedia.org" + href;
        Document doc = Jsoup.connect(link).get();

        Elements childDivElement = doc.selectFirst("#mw-content-text > div.mw-parser-output").children();
        String overview = null;
        for (int i = 0; i < childDivElement.size(); i++)
        {
            Element element = childDivElement.get(i);

            if (!element.tagName().equals("p"))
            {
                continue;
            }

            if (element.selectFirst("b") == null)
            {
                continue;
            }

            overview = element.text();
            break;
        }
        return overview;
    }

    public ArrayList<String> extractEraNameAndtime(String text)
    {
        ArrayList<String> result = new ArrayList<String>(2);
        for (int i = 0; i < 2; i++)
        {
            result.add(null);
        }

        Pattern pattern = Pattern.compile("([\\p{L} –\\-]+) \\(([\\p{L} ,\\d-–]+)\\) *h*o*ặ*c* *\\(*([\\p{L} ,\\d-–]+)*\\)*\\(*([\\d-]+)*\\)*|[\\p{L} –\\-]+");
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find())
        {
            return result;
        }

        if (matcher.group(1) == null)
        {
            result.set(0, matcher.group(0));
        }
        else if (matcher.group(3) != null && matcher.group(4) == null)
        {
            result.set(0, matcher.group(1));
            result.set(1, matcher.group(2) + " hoặc " + matcher.group(3));
        }
        else if (matcher.group(3) != null && matcher.group(4) != null)
        {
            result.set(0, matcher.group(1) + " " + matcher.group(3));
            result.set(1, matcher.group(2) + " và " + matcher.group(4));
        }
        else
        {
            result.set(0, matcher.group(1));
            result.set(1, matcher.group(2));
        }
        //System.out.println(result.get(0));
        //System.out.println(result.get(1));
        return result;
    }

    public ArrayList<String> extractH3Data(Element h3Element) throws IOException
    {
        ArrayList<String> result = new ArrayList<String>(3);
        for (int i = 0; i < 3; i++)
        {
            result.add(null);
        }

        Element spanElement = h3Element.selectFirst("span[class=mw-headline]");

        if (spanElement == null)
        {
            return result;
        }

        String text = spanElement.text();
        if (!text.isEmpty())
        {
            ArrayList<String> tmp = extractEraNameAndtime(text);
            result.set(0, tmp.get(0));
            result.set(1, tmp.get(1));
        }

        Element aElement = spanElement.selectFirst("a");

        if (aElement == null)
        {
            return result;
        }

        String href = spanElement.selectFirst("a").attr("href");
        if (!href.isEmpty())
        {
            result.set(2, extractOverview(href));
        }
        return result;
    }

    public ArrayList<String> extractTableCellPadding2(Element tableElement) throws IOException
    {
        Element h3Element = tableElement.selectFirst("h3");
        return extractH3Data(h3Element);
    }

    public ArrayList<String> extractTableWithoutCellpadding(Element tableElement) throws IOException
    {
        Element h3Element = tableElement.selectFirst("h3");
        return extractH3Data(h3Element);
    }

    public ArrayList<String> extractFounderAndCapital(String eraName, Element founderAndCapital)
    {
        ArrayList<String> result = new ArrayList<String>(3);
        for (int i = 0; i < 3; i++)
        {
            result.add(null);
        }

        Elements trTags = founderAndCapital.children();
        for (int i = 1; i < trTags.size(); i++)
        {
            Element trTag = trTags.get(i);
            Elements tdTags = trTag.children();

            if (eraName.contains(tdTags.get(0).text()) ||
                    tdTags.get(0).text().contains(eraName) ||
                    tdTags.get(1).text().contains(eraName))
            {
                tdTags.get(3).select("sup").remove();
                result.set(0, tdTags.get(1).text());
                result.set(1, tdTags.get(3).text());
                if (!eraName.equals(tdTags.get(0).text()))
                {
                    result.set(2, tdTags.get(0).text());
                }
                break;
            }

        }
        return result;
    }

    public void crawlFromWiki() throws IOException
    {
        List<Era> eraList = new ArrayList<Era>();

        PrintStream outPutStream = new PrintStream("outputEraWiki.txt");

        Document doc = Jsoup.connect("https://vi.wikipedia.org/wiki/Vua_Việt_Nam").get();
        Elements childDivElement = doc.selectFirst("div[class=mw-parser-output]").children();

        Element founderAndCapital = doc.selectFirst("#mw-content-text > div.mw-parser-output > table:nth-child(91) > tbody");
        //System.out.println(childDivElement.get(3).tagName());
        //int j = 1;
        for (int i = 0; i < childDivElement.size(); i++)
        {
            Element currentElement = childDivElement.get(i);
            if (!currentElement.tagName().equals("h2"))
            {
                continue;
            }
            String eraName = "Không rõ";
            String time = "Không rõ";
            String overview = "Không rõ";
            String founder = "Không rõ";
            String capital = "Không rõ";
            ArrayList<String> aliases = new ArrayList<>();
            ArrayList<String> relatedCharacters = new ArrayList<>();

            String textSpanClass = currentElement.selectFirst("span[class=mw-headline]").text();
            //System.out.println("----/"+ currentElement + "\\-----");
            if (textSpanClass.isEmpty())
            {
                continue;
            }

            if (!Pattern.matches("Thời.+|Bắc.+|Chống.+", textSpanClass))
            {
                continue;
            }


            while (!childDivElement.get(i+1).tagName().equals("h2"))
            {
                i++;
                Element nextElement = childDivElement.get(i);
                ArrayList<String> result;
                if (nextElement.tagName().equals("h3"))
                {
                    result = extractH3Data(nextElement);
                    eraName = (result.get(0) != null) ? result.get(0) : "Không rõ";
                    time = (result.get(1) != null) ? result.get(1) : "Không rõ";
                    overview = (result.get(2) != null) ? result.get(2) : "Không rõ";
                }

                if (nextElement.tagName().equals("table") && nextElement.attr("cellpadding").equals("2"))
                {
                    result = extractTableCellPadding2(nextElement);
                    eraName = (result.get(0) != null) ? result.get(0) : "Không rõ";
                    time = (result.get(1) != null) ? result.get(1) : "Không rõ";
                    overview = (result.get(2) != null) ? result.get(2) : "Không rõ";
                }

                if (nextElement.tagName().equals("table") && nextElement.hasClass("wikitable") && !nextElement.hasAttr("cellpadding"))
                {
                    result = extractTableWithoutCellpadding(nextElement);
                    eraName = (result.get(0) != null) ? result.get(0) : "Không rõ";
                    time = (result.get(1) != null) ? result.get(1) : "Không rõ";
                    overview = (result.get(2) != null) ? result.get(2) : "Không rõ";
                }

                if (nextElement.tagName().equals("table") && nextElement.attr("cellpadding").equals("0"))
                {
                    Elements tdTags = nextElement.select("tbody > tr > td:nth-child(2)");

                    for (Element td: tdTags)
                    {
                        Element aTag = td.selectFirst("a");
                        relatedCharacters.add(aTag.text());
                    }

                    result = extractFounderAndCapital(eraName, founderAndCapital);
                    founder = (result.get(0) != null) ? result.get(0) : "Không rõ";
                    capital = (result.get(1) != null) ? result.get(1) : "Không rõ";

                    if (result.get(2) != null)
                    {
                        aliases.add(result.get(2));
                    }

                    //System.out.println("-----------------------------------------" + j + "--------------------------------------------------------");
                    outPutStream.println("eraName: " + eraName);
                    outPutStream.println("time: " + time);
                    outPutStream.println("overview: " + overview);
                    outPutStream.println("founder: " + founder);
                    outPutStream.println("capital: " + capital);
                    outPutStream.println("aliases: " + aliases);
                    outPutStream.println("relatedCharacters: " + relatedCharacters);
                    outPutStream.println("===========================================================================");

                    eraList.add(new Era(eraName,
                            time,
                            founder,
                            capital,
                            overview,
                            new HashSet<>(aliases),
                            new HashSet<>(relatedCharacters)));


                    //j++;
                    overview = "Không rõ";
                    relatedCharacters.clear();
                    aliases.clear();
                }
            }

        }
        this.eraCollection.setData(eraList);
        outPutStream.close();


    }

    public void crawlEra() throws IOException
    {
        crawlFromWiki();
    }

    public static void main(String[] args) throws IOException
    {
        CrawlEraFromWiki crawlerWiki = new CrawlEraFromWiki();
        crawlerWiki.crawlEra();
        crawlerWiki.getEraCollection().toJsonFiles();
        EraCollection eraCollection = new EraCollection();
        eraCollection.loadJsonFiles();
        System.out.println(eraCollection.getData().get(2).getId());
        eraCollection.getData().get(2).setId(55);
        System.out.println(eraCollection.getData().get(2).getId());
        eraCollection.toJsonFiles();
    }
}