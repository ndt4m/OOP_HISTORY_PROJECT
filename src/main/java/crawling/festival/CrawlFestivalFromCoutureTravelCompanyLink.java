package crawling.festival;

import java.io.PrintStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import entity.Festival;

public class CrawlFestivalFromCoutureTravelCompanyLink extends CrawlFestival
{

    public void crawlFestivalFrom(String url) throws IOException
    {
        List<Festival> festivalList = new ArrayList<>();

        PrintStream outPutStream = new PrintStream("outputFestivalCoutureTravelCompany.txt");

        String festivalName = "Không rõ";
        Set<String> aliases = new HashSet<String>();
        String time = "Không rõ";
        String location = "Không rõ";
        String overview = "";
        Set<String> relatedCharacters = new HashSet<String>();

        Document doc = Jsoup.connect(url).get();
        Element td_post_content = doc.selectFirst("#post-3068 > div.td-post-template-8-box > div > div > div > div > div.td-post-content");
        for (int i = 0; i < td_post_content.children().size(); i++)
        {
            if (!td_post_content.child(i).tagName().equals("h4"))
            {
                continue;
            }

            festivalName = td_post_content.child(i).text().replaceAll("\\d+\\.", "").trim();
            while (i + 1 < td_post_content.children().size() && !td_post_content.child(i + 1).tagName().equals("h4"))
            {
                i++;
                if (td_post_content.child(i).tagName().equals("ul"))
                {
                    time = td_post_content.child(i).child(0).text().replaceAll("Thời gian:", "").trim();
                    location = td_post_content.child(i).child(1).text().replaceAll("Địa điểm:", "").trim();
                }

                if (td_post_content.child(i).tagName().equals("p") && !td_post_content.child(i).text().contains("Xem thêm:") && !td_post_content.child(i).text().contains("Trên đây"))
                {
                    overview += td_post_content.child(i).text();
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
            time = "Không rõ";
            location = "Không rõ";
            overview = "";
        }
        this.festivalCollection.setData(festivalList);
        outPutStream.close();
    }

    public void crawlFestival() throws IOException
    {
        this.crawlFestivalFrom("https://www.couturetravelcompany.com/cac-le-hoi-o-viet-nam/");
    }

    public static void main(String[] args) throws IOException
    {
        CrawlFestivalFromCoutureTravelCompanyLink crawlerFesCoutureTravel = new CrawlFestivalFromCoutureTravelCompanyLink();
        crawlerFesCoutureTravel.crawlFestival();
        System.out.println(crawlerFesCoutureTravel.getFestivalCollection().getData().get(0).getEntityName());
    }
}
