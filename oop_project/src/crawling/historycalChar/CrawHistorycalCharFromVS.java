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

import entity.HistoricalCharacter;


public class CrawHistorycalCharFromVS extends CrawlHistoricalChar
{   

    public String getCompleteSentence(String s)
    {
        int index;
        index = s.lastIndexOf(".");
        if (index != -1)
        {
            return s.substring(0, index);
        }

        index = s.lastIndexOf(";");
        if (index != -1)
        {
            return s.substring(0, index);
        }

        index = s.lastIndexOf("(");
        if (index != -1)
        {
            return s.substring(0, index);
        }

        index = s.lastIndexOf(",");
        if (index != -1)
        {
            return s.substring(0, index);
        }

        return s;
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

    public List<String> extractDateOfBirthAndDateOfDeath(String s)
    {
        List<String> result = new ArrayList<String>();
        result.add(s);
        for (int i = 0; i < 2; i++)
        {
            result.add("Không rõ");
        }

        String regex = "\\d+ tháng \\d+ năm \\d+ [-,–] \\d+ tháng \\d+ năm \\d+|(?<=\\(|[;,] )(?:\\?|[\\d?]+) ?[-,–] ?(?:\\?|[\\d?]+)(?=\\))|(?:sinh ngày |mất ngày)\\d+(?: tháng |-)\\d+(?: năm |-)\\d+|(?:sinh năm |sinh ngày |ngày )\\d+|sinh ngày \\d+ tháng \\d+ năm \\d+[-,–] mất ngày \\d+ tháng \\d+ năm \\d+|\\d+ tháng \\d+, \\d+ – \\d+ tháng \\d+, \\d+|tháng \\d+ năm \\d+ - \\d+ tháng \\d+ năm \\d+|\\d+ – \\d+ tháng \\d+ năm \\d+|[\\d?]+ - \\d+ tháng \\d+, \\d+|\\d+ tháng \\d+ năm \\d+ – \\d+|không rõ năm sinh, năm mất|không rõ năm sinh|không rõ năm mất|không rõ quê quán, năm sinh và năm mất";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find())
        {
            return result;
        }
        
        result.set(0, s.substring(0, matcher.start()));
        List<String> birthAndDeath = new ArrayList<>(Arrays.asList(matcher.group(0).split("[-–]")));
        if (birthAndDeath.size() == 2)
        {
            result.set(1, birthAndDeath.get(0).trim());
            result.set(2, birthAndDeath.get(1).trim());
        }

        else if (birthAndDeath.get(0).contains("Sinh") || birthAndDeath.get(0).contains("sinh"))
        {
            result.set(1, birthAndDeath.get(0).trim());
        }

        else
        {
            result.set(2, birthAndDeath.get(0).trim());
        }
        
        return result;
    }

    public List<String> extractFatherNameAndMotherNameBy(String s)
    {
        List<String> result = new ArrayList<String>();
        //System.out.println(result);
        String regex = "(?:con trai của|con gái của|ông là con của|mẹ của ông là|bố của ông là|thân sinh là|Ông là con|con chí sĩ|con trai lớn của|con trai lớn (?:ông|bà)|con xử sĩ|con ông|con trưởng|con thứ (?:hai|ba|tư|năm|sáu|bảy|tám|chín|mười|\\d+)(?: của|)|con tiến sĩ|con ông|con của|con trai|con trai của) *(.+?)[,.;()]";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find())
        {
            if (matcher.group(1).isEmpty())
            {
                continue;
            }

            for (String name: matcher.group(1).trim().split("(và bà|và)"))
            {
                if (name.isEmpty())
                {
                    continue;
                }
                result.add(name.trim());
            }
        }
        return result;
    }

    public String extractOccupationBy(String s)
    {   
        String occupation = s;

        List<String> result;


        result = extractAliasesBy(s);
        

        if (!result.get(0).equals(s) && !result.get(0).isEmpty())
        {   
            occupation = getCompleteSentence(result.get(0));
            return occupation;
        }
        

        result = extractDateOfBirthAndDateOfDeath(s);
        if (!result.get(0).equals(s) && !result.get(0).isEmpty())
        {
            occupation = getCompleteSentence(result.get(0));
            return occupation;
        }

        result = extractHometownBy(s);
        if (!result.get(0).equals(s) && !result.get(0).isEmpty())
        {
            occupation = getCompleteSentence(result.get(0));
            return occupation;
        }

        return occupation;
        
    }

    public List<String> extractHometownBy(String s)
    {
        List<String> result = new ArrayList<String>();
        result.add(s);

        String regex = "(?:quê(?!,).+?|huyện.+?|người (?:làng|thôn).+?|xã(?! hội).+?|thành viên của gia tộc.+?|người ở.+?|người đất.+?|sinh tại.+?|nguyên quán.+?|tại(?! các).+?|quán làng.+?|ở làng.+?|sinh quán.+?)(?=[\\.;]|, là)|không rõ quê quán|không rõ năm sinh, năm mất và quê quán";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find() || matcher.group(0).isEmpty())
        {
            result.add("Không rõ");
            return result;
        }
        
        result.set(0, s.substring(0, matcher.start()));
        result.add(matcher.group(0));
        return result;
    }

    public List<String> extractEraNameBy(String s)
    {
        List<String> result = new ArrayList<String>();

        String regex = "(?<=-)(?! *nước Việt Nam mới *| *thời tiền sử *)[^()]+?(?=\\(.+?\\))|nước Việt Nam mới|thời tiền sử";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        while (matcher.find())
        {
            if (matcher.group(0).isEmpty())
            {
                continue;
            }

            result.add(matcher.group(0).trim());
        }

        return result;
    }

    public List<String> extractAliasesBy(String s)
    {
        List<String> result = new ArrayList<String>();
        result.add(s);

        String regex = "(tên thực là|cũng có tên khác nữa là|cũng có tên khác là|tên khác là|tước phong là|pháp tự|đạo hiệu|đạo hiệu là|tên thánh là|nghệ danh là|nghệ danh|còn có tên là|phiên âm|biệt hiệu là|hiệu là|hiệu|tên chữ là|hay|bút danh là|bút danh|tên thường gọi|còn gọi là|tên thật là|tên thật|tên khai sinh là|tên khai sinh|tự xưng là|tự là|tự|thuở nhỏ tên là|đổi tên thành|sau đổi thành|miếu hiệu là|miếu hiệu cho ông là|thụy hiệu đầy đủ là|tức|tước vị lúc sống là|nguyên danh|niên hiệu|tên cũ|dã sử xưng gọi|thụy hiệu|bút danh|thông gọi|đổi tên là|đổi là|bí danh|pháp danh|tên húy là|có sách gọi|tên gốc là|còn được gọi là|thường gọi là|thường gọi|trước tên là|đổi lại là|tên chữ|lấy tên là|húy là|cũng gọi là|cũng gọi|lấy húy kị|đời sau quen gọi là|tước hiệu|được gọi tắt là|chính tên là|và(?=[\\p{L} ]+\\([\\p{L} \\w\\d]*\\)))[ :]([^,\\.;()]*?)(?=(?:[,\\.;()]| là| nên))";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        int i = 0;
        while (matcher.find())
        {
            if (matcher.group(2).isEmpty())
            {
                continue;
            }

            if (i == 0)
            {
                result.set(0, s.substring(0, matcher.start()));
            }

            for (String name : matcher.group(2).trim().split("(hoặc|hay|và)"))
            {   
                if (name.isEmpty())
                {
                    continue;
                }
                result.add(name.trim());
            }

            i++;
        }
        return result;
    }

    public List<String> crawlAllUrlsFrom(String url) throws IOException
    {
        List<String> urls = new ArrayList<String>();

        for (int i = 0; i < 120; i++)
        {
            Document doc = Jsoup.connect(url + i).timeout(120000).get();
            for (Element a : doc.select("body > div.ui.container > table > tbody > tr > td:nth-child(1) > a:nth-child(1)"))
            {
                urls.add("https://vansu.vn" + a.attr("href"));
                //System.out.println("https://vansu.vn" + a.attr("href"));
                //break;/////////////////REMEMBER TO DELETE THIS//////////
            }
            //break;
        }
        //System.out.println(urls.size());

        return urls;
    }


    public HistoricalCharacter crawlCharFrom(String url) throws IOException
    {
        FileWriter fw = new FileWriter("outputHistoricalCharVS.txt",true);

        Document doc = Jsoup.connect(url).timeout(120000).get();

        String charName = "Không rõ";
        List<String> eraName = new ArrayList<String>();
        String dateOfBirth = "Không rõ";
        String dateOfDeath = "Không rõ";
        String fatherName = "Không rõ";
        String motherName = "Không rõ";
        String hometown = "Không rõ";
        String occupation = "Không rõ";
        String workTenure = "Không rõ";
        String overview = "Không rõ";
        List<String> aliases = new ArrayList<String>();
        


        charName = (doc.selectFirst("div[class=active section]") != null) ? doc.selectFirst("div[class=active section]").text() : "Không rõ";
        //System.out.println(charName);
    
        Element table = doc.selectFirst("table[class=ui selectable celled table] > tbody");
        if (table == null)
        {
            System.out.println("[-] ERROR: Can't get the table Element of the charName - \"" + charName + "\"" + url);
            fw.close();
            return new HistoricalCharacter(charName, 
                                       new HashSet<>(eraName), 
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

        int tableSize = table.children().size();

        overview = table.child(tableSize - 1).select("p:not(:first-child)").text();
        //System.out.println(overview);

        eraName = extractEraNameBy(table.child(tableSize - 2).selectFirst("td:nth-child(2)").text());
        //System.out.println(eraName);
          
        List<String> result;
        
        String first_pTagContent = table.child(tableSize - 1).selectFirst("p").text();

        result = extractFatherNameAndMotherNameBy(first_pTagContent);
        if (result.size() >= 2)
        {
            fatherName = result.get(0);
            motherName = result.get(1);
        }
        else if (result.size() == 1)
        {
            fatherName = result.get(0);
        }
        
        occupation = extractOccupationBy(first_pTagContent);
        //System.out.println("------/" + occupation + "\\-----");
        result = extractHometownBy(first_pTagContent);
        //System.out.println("------/" + result + "\\-----");

        if (result.get(1).equals("Không rõ"))
        {
            hometown = table.child(tableSize - 3).selectFirst("td:nth-child(2)").text();
        }
        else
        {
            hometown = result.get(1);
        }
        //System.out.println(first_pTagContent);
        
        if (tableSize == 3)
        {   
            for (String name : extractAliasesBy(first_pTagContent))
            {
                if (isUpperCase(name))
                {
                    aliases.add(name.trim());
                }
            }
        }

        if (tableSize == 4 && table.child(0).selectFirst("td:nth-child(1)").text().equals("Tên khác"))
        {
            for (String name : table.child(0).selectFirst("td:nth-child(2)").text().split("-"))
            {
                aliases.add(name.trim());
            }
        }
        
        if (tableSize == 4 && table.child(0).select("td:nth-child(1)").text().equals("Năm sinh"))
        {
            List<String> birthAndDeath = new ArrayList<String>(Arrays.asList(table.child(0).selectFirst("td:nth-child(2)").text().split("-")));
            if (birthAndDeath.size() == 2)
            {
                dateOfBirth = (!birthAndDeath.get(0).trim().equals("...")) ? birthAndDeath.get(0).trim() : "Không rõ";
                dateOfDeath = (!birthAndDeath.get(1).trim().equals("...")) ? birthAndDeath.get(1).trim() : "Không rõ";
            }
            else
            {
                dateOfBirth = birthAndDeath.get(0);
            }

            for (String name : extractAliasesBy(first_pTagContent))
            {
                if (name.isEmpty())
                {
                    continue;
                }

                if (isUpperCase(name))
                {
                    aliases.add(name.trim());
                }
            }
        }

        if (tableSize == 5)
        {
            for (String name : table.child(0).selectFirst("td:nth-child(2)").text().split("-"))
            {
                aliases.add(name.trim());
            }

            List<String> birthAndDeath = new ArrayList<String>(Arrays.asList(table.child(1).selectFirst("td:nth-child(2)").text().split("-")));
            if (birthAndDeath.size() == 2)
            {
                dateOfBirth = (!birthAndDeath.get(0).trim().equals("...")) ? birthAndDeath.get(0).trim() : "Không rõ";
                dateOfDeath = (!birthAndDeath.get(1).trim().equals("...")) ? birthAndDeath.get(1).trim() : "Không rõ";
            }
            else
            {
                dateOfBirth = birthAndDeath.get(0);
            }

        }

        // System.out.println("charName: " + charName);
        // System.out.println("eraName: " + eraName);
        // System.out.println("dateOfBirth: " + dateOfBirth);
        // System.out.println("dateOfDeath: " + dateOfDeath);
        // System.out.println("fatherName: " + fatherName);
        // System.out.println("motherName: " + motherName);
        // System.out.println("hometown: " + hometown);
        // System.out.println("occupation: " + occupation);
        // System.out.println("workTenure: " + workTenure);
        // System.out.println("overview: " + overview);
        // System.out.println("aliases: " + aliases);
        // System.out.println("=================================================================");
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
        fw.write("==============================================================" + "\n");
        fw.close();
        return new HistoricalCharacter(charName, 
                                       new HashSet<>(eraName), 
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
        this.historicalCharCollection.setData(historicalCharList);
    }

    public void crawlHistoricalChar() throws IOException
    {
        List<String> urls = this.crawlAllUrlsFrom("https://vansu.vn/viet-nam/viet-nam-nhan-vat?page=");
        this.crawlAllChar(urls);
    }

    public static void main(String[] args) throws IOException
    {
        CrawHistorycalCharFromVS crawler = new CrawHistorycalCharFromVS();
        crawler.crawlHistoricalChar();
        System.out.println(crawler.getHistoricalCharCollection().getData().get(0).getEntityName());

    }
}
