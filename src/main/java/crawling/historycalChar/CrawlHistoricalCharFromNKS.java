package crawling.historycalChar;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.HistoricalCharacter;

public class CrawlHistoricalCharFromNKS extends CrawlHistoricalChar
{

    public boolean isAliase(String s)
    {
        return s.equalsIgnoreCase("tên đầy đủ") ||
                s.equalsIgnoreCase("thụy hiệu")  ||
                s.equalsIgnoreCase("tên khác")   ||
                s.equalsIgnoreCase("tên thật")   ||
                s.equalsIgnoreCase("tên húy")    ||
                s.equalsIgnoreCase("niên hiệu")  ||
                s.equalsIgnoreCase("tự")         ||
                s.equalsIgnoreCase("bút danh")   ||
                s.equalsIgnoreCase("tước hiệu")  ||
                s.equalsIgnoreCase("miếu hiệu")  ||
                s.equalsIgnoreCase("tên bản ngữ")||
                s.equalsIgnoreCase("nghệ danh");
    }

    public boolean isUnique(String alterName, List<String> aliases, String charName)
    {
        if (alterName.equals(charName))
        {
            return false;
        }

        for (String name : aliases)
        {
            if (alterName.equals(name))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isUpperCase(String s)
    {
        String words[] = s.split(" ");
        for (String word : words)
        {
            if (!Character.isUpperCase(word.charAt(0)))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isEraName(String s)
    {
        return s.equalsIgnoreCase("triều đại")  ||
                s.equalsIgnoreCase("hoàng tộc")  ||
                s.equalsIgnoreCase("kỷ nguyên")  ||
                s.equalsIgnoreCase("gia tộc");
    }

    public boolean isDateOfBirth(String s)
    {
        return s.equalsIgnoreCase("sinh");
    }

    public boolean isDateOfDeath(String s)
    {
        return s.equalsIgnoreCase("mất");
    }

    public boolean isFatherName(String s)
    {
        return s.equalsIgnoreCase("thân phụ")   ||
                s.equalsIgnoreCase("cha mẹ")     ||
                s.equalsIgnoreCase("bố mẹ");
    }

    public boolean isMotherName(String s)
    {
        return s.equalsIgnoreCase("thân mẫu");
    }

    public boolean isHometown(String s)
    {
        return s.equalsIgnoreCase("quê quán")   ||
                s.equalsIgnoreCase("nơi ở")      ||
                s.equalsIgnoreCase("quê");
    }

    public boolean isOccupation(String s)
    {
        return s.equalsIgnoreCase("chức vụ")    ||
                s.equalsIgnoreCase("nghề nghiệp")||
                s.equalsIgnoreCase("công việc")  ||
                s.equalsIgnoreCase("cấp bậc")    ||
                s.equalsIgnoreCase("dơn vị")     ||
                s.equalsIgnoreCase("chức quan cao nhất");
    }

    public boolean isOccupation(Element nextElement)
    {
        return isWorkTenure(nextElement.text());
    }

    public boolean isWorkTenure(String s)
    {
        return s.equalsIgnoreCase("tại vị")     ||
                s.equalsIgnoreCase("nhiệm kì")   ||
                s.equalsIgnoreCase("năm tại ngũ")||
                s.equalsIgnoreCase("trị vì");
    }

    public List<String> extractAliasesBy(String pTagContent, List<String> aliases, String charName)
    {
        List<String> result = new ArrayList<String>();

        String regex = "(còn có tên là|phiên âm|hiệu là|hiệu|tên chữ là|hay|bút danh là|bút danh|tên thường gọi|còn gọi là|tên thật là|tên thật|tên khai sinh là|tên khai sinh|tự xưng là|tự là|tự|thuở nhỏ tên là|đổi tên thành|sau đổi thành|miếu hiệu là|miếu hiệu cho ông là|thụy hiệu đầy đủ là|tức|tước vị lúc sống là|nguyên danh|niên hiệu|tên cũ|dã sử xưng gọi|thụy hiệu|bút danh|thông gọi|đổi tên là|đổi là|bí danh|pháp danh|tên húy là|có sách gọi|tên gốc là|còn được gọi là|thường gọi là|thường gọi|trước tên là|đổi lại là|tên chữ|lấy tên là|húy là|cũng gọi là|cũng gọi|lấy húy kị|đời sau quen gọi là|tước hiệu|được gọi tắt là|và(?=[\\p{L} ]+\\([\\p{L} \\w\\d]*\\)))[ :]([^,\\.;()]*?)(?=(?:[,\\.;()]| là))";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pTagContent);

        while (matcher.find())
        {
            if (matcher.group(2).isEmpty())
            {
                continue;
            }

            if (isUpperCase(matcher.group(2).trim()) && isUnique(matcher.group(2).trim(), aliases, charName))
            {
                result.add(matcher.group(2).trim());
            }
        }
        return result;
    }

    public List<String> extractDateOfBirthAndDateOfDeathBy(String pTagContent)
    {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < 2; i++)
        {
            result.add("Không rõ");
        }

        String regex = "\\d+ tháng \\d+ năm \\d+ [-,–] \\d+ tháng \\d+ năm \\d+|(?<=\\(|[;,] )(?:\\?|[\\d?]+) ?[-,–] ?(?:\\?|[\\d?]+)(?=\\))|(?:sinh ngày |mất ngày)\\d+ tháng \\d+ năm \\d+|(?:sinh năm |sinh ngày |ngày )\\d+|sinh ngày \\d+ tháng \\d+ năm \\d+[-,–] mất ngày \\d+ tháng \\d+ năm \\d+|\\d+ tháng \\d+, \\d+ – \\d+ tháng \\d+, \\d+|tháng \\d+ năm \\d+ - \\d+ tháng \\d+ năm \\d+|\\d+ – \\d+ tháng \\d+ năm \\d+|[\\d?]+ - \\d+ tháng \\d+, \\d+|\\d+ tháng \\d+ năm \\d+ – \\d+";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pTagContent);

        if (!matcher.find())
        {
            return result;
        }

        List<String> birthAndDeath = new ArrayList<>(Arrays.asList(matcher.group(0).split("[-–]")));
        if (birthAndDeath.size() == 2)
        {
            result.set(0, birthAndDeath.get(0).trim());
            result.set(1, birthAndDeath.get(1).trim());
        }

        else if (birthAndDeath.get(0).contains("Sinh") || birthAndDeath.get(0).contains("sinh"))
        {
            result.set(0, birthAndDeath.get(0).trim());
        }

        else
        {
            result.set(1, birthAndDeath.get(0).trim());
        }

        return result;
    }

    public List<String> extractHometownBy(String pTagContent)
    {
        List<String> result = new ArrayList<String>();

        String regex = "quê(?!,).+?[;\\.]|huyện.+?\\)|người làng.+?[\\.;]|xã(?! hội).+?[\\.;]|thành viên của gia tộc.+?[\\.;]|người ở.+?[\\.;]|người đất.+?(?=[\\.;]|, là)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pTagContent);

        if (!matcher.find() || matcher.group(0).isEmpty())
        {
            result.add("Không rõ");
            return result;
        }

        result.add(matcher.group(0));
        return result;
    }

    public List<String> extractFatherNameAndMotherNameBy(String pTagContent)
    {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < 2; i++)
        {
            result.add("Không rõ");
        }

        String regex = "(?:con trai của|con gái của|ông là con của|mẹ của ông là|bố của ông là) *(.+?)[,.;)()\\[\\]]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pTagContent);

        int i = 0;
        while (matcher.find())
        {
            if (matcher.group(1).isEmpty())
            {
                continue;
            }

            result.set(i, matcher.group(1));
            i++;
        }
        return result;
    }



    public List<String> crawlALLUrlsFrom(String url) throws IOException
    {
        List<String> urls = new ArrayList<String>();

        Document doc = Jsoup.connect(url).timeout(120000).get();

        int pageNum = Integer.parseInt(doc.selectFirst("#content > div.com-content-category-blog.blog > div.com-content-category-blog__navigation.w-100 > p").text().split("/")[1].substring(1));

        for (int i = 0; i < pageNum; i++)
        {
            doc = Jsoup.connect("https://nguoikesu.com/nhan-vat?start=" + i*5).timeout(120000).get();

            Elements aTags = doc.select("#content > div.com-content-category-blog.blog > div.com-content-category-blog__items.blog-items > div > div > div > h2 > a");

            for (Element a: aTags)
            {
                urls.add("https://nguoikesu.com" + a.attr("href"));
            }
            //break;
        }
        return urls;
    }

    public HistoricalCharacter crawlCharFrom(String url) throws IOException
    {
        FileWriter fw = new FileWriter("outputHistoricalCharNKS.txt",true);

        Document doc = Jsoup.connect(url).timeout(120000).get();

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
        List<String> aliases = new ArrayList<String>();

        Element headline = doc.selectFirst("div[class=page-header] > h2");
        if (headline != null)
        {
            charName = headline.text();
        }

        Element infobox = doc.selectFirst("#content > div.com-content-article.item-page.page-list-items > div.com-content-article__body > div.infobox > table > tbody");
        if (infobox != null)
        {

            for (int i = 0; i < infobox.children().size(); i++)
            {
                Element thTag = infobox.children().get(i).selectFirst("th");
                Element tdTag = infobox.children().get(i).selectFirst("td");

                if (thTag == null || tdTag == null)
                {
                    continue;
                }

                tdTag.select("sup").remove();

                String thContent = thTag.text();

                if (isOccupation(thContent) && occupation.equals("Không rõ"))
                {
                    occupation = tdTag.text();
                }

                else if (isAliase(thContent))
                {
                    aliases.add(tdTag.text());
                }

                else if (isEraName(thContent) && eraName.equals("Không rõ"))
                {
                    eraName = tdTag.text();
                }

                else if (isDateOfBirth(thContent) && dateOfBirth.equals("Không rõ"))
                {
                    dateOfBirth = tdTag.text();
                }

                else if (isDateOfDeath(thContent) && dateOfDeath.equals("Không rõ"))
                {
                    dateOfDeath = tdTag.text();
                }

                else if (isFatherName(thContent) && fatherName.equals("Không rõ"))
                {
                    fatherName = tdTag.text();
                }

                else if (isMotherName(thContent) && motherName.equals("Không rõ"))
                {
                    motherName = tdTag.text();
                }

                else if (isHometown(thContent) && hometown.equals("Không rõ"))
                {
                    hometown = tdTag.text();
                }

                else if (isWorkTenure(thContent) && workTenure.equals("Không rõ"))
                {
                    workTenure = tdTag.text();

                    if (!occupation.equals("Không rõ"))
                    {
                        continue;
                    }

                    Element before_thTag = infobox.children().get(i - 1).selectFirst("th");
                    if (before_thTag != null)
                    {
                        occupation = before_thTag.text();
                    }

                }
            }
        }

        Element first_pTag = doc.selectFirst("div[class=com-content-article__body] > p");
        if (first_pTag == null)
        {
            System.err.println("Don't have the first p Tag: " + url);
            fw.close();
            return new HistoricalCharacter(charName,
                    eraName,
                    dateOfBirth,
                    dateOfDeath,
                    fatherName,
                    motherName,
                    hometown,
                    occupation,
                    workTenure,
                    overview,
                    new HashSet<>(aliases));
        }

        first_pTag.select("sup").remove();
        String pTagContent = first_pTag.text();

        overview = pTagContent;

        List<String> result;

        result = extractAliasesBy(pTagContent, aliases, charName);
        for(String name : result)
        {
            aliases.add(name);
        }

        result = extractDateOfBirthAndDateOfDeathBy(pTagContent);
        if (dateOfBirth.equals("Không rõ") && !result.get(0).equals("?"))
        {
            dateOfBirth = result.get(0);
        }
        if (dateOfDeath.equals("Không rõ") && !result.get(1).equals("?"))
        {
            dateOfDeath = result.get(1);
        }

        result = extractHometownBy(pTagContent);
        if (hometown.equals("Không rõ"))
        {
            hometown = result.get(0);
        }

        result = extractFatherNameAndMotherNameBy(pTagContent);
        if (fatherName.equals("Không rõ"))
        {
            fatherName = result.get(0);
        }
        if (motherName.equals("Không rõ"))
        {
            motherName = result.get(1);
        }

        Elements aTags = first_pTag.select("a");
        for (Element a : aTags)
        {
            if (a.attr("href").contains("nha-") && eraName.equals("Không rõ"))
            {
                eraName = a.text();
                break;
            }
        }

        fw.write("charName: " + charName + "\n");
        fw.write("eraName: " + eraName + "\n");
        fw.write("dateOfBirth: " + dateOfBirth + "\n");
        fw.write("dateOfDeath: " + dateOfDeath + "\n");
        fw.write("fatherName: " + fatherName + "\n");
        fw.write("motherName: " + motherName + "\n");
        fw.write("hometown: " + hometown + "\n");
        fw.write("occupation: " + occupation + "\n");
        fw.write("workTenure: " + workTenure + "\n");
        fw.write("overview: " + overview + "\n");
        fw.write("aliases: " + aliases + "\n");
        fw.write("=========================================================================================" + "\n");
        fw.close();

        return new HistoricalCharacter(charName,
                eraName,
                dateOfBirth,
                dateOfDeath,
                fatherName,
                motherName,
                hometown,
                occupation,
                workTenure,
                overview,
                new HashSet<>(aliases));

    }

    public void crawlAllChar(List<String> urls) throws IOException
    {
        List<HistoricalCharacter> historicalCharList = new ArrayList<HistoricalCharacter>();
        for (String url : urls)
        {
            historicalCharList.add(crawlCharFrom(url));
            //break;
        }
        //this.historicalCharCollection.setData(historicalCharList);
        this.setHistoricalCharCollection(historicalCharList);
    }

    public void crawlHistoricalChar() throws IOException
    {
        List<String> urls = this.crawlALLUrlsFrom("https://nguoikesu.com/nhan-vat");
        this.crawlAllChar(urls);
    }

    public static void main(String[] args) throws IOException
    {
        CrawlHistoricalCharFromNKS crawler = new CrawlHistoricalCharFromNKS();
        crawler.crawlHistoricalChar();
        System.out.println(crawler.getHistoricalCharCollection().getData().get(0).getEntityName());
    }
}
