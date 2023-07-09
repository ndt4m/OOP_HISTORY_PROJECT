package crawling;


import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


import vn.pipeline.*;


import newvncorenlp.NewAnnotation;
import newvncorenlp.NewSentence;
import newvncorenlp.NewVNCoreNLP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler   
{
    public Crawler()
    {

    }

    public boolean isUpperCase(String s, String delimiter)
    {
        String words[] = s.split("_");
        //System.out.println(new ArrayList<String>(Arrays.asList(s.split(" "))));
        for (String word : words)
        {   
            if (!Character.isUpperCase(word.charAt(0)))
            {
                return false;
            }
        }
    
        return true;
    }

    public List<String> extractPersonalNameEntityFrom(String text) throws IOException
    {
        List<String> result = new ArrayList<String>();

        String[] annotators = {"wseg", "pos", "ner", "parse"};
        NewVNCoreNLP pipeline = new NewVNCoreNLP(annotators);

        NewAnnotation newAnnotation = new NewAnnotation(text);
        pipeline.annotate(newAnnotation);
        for (NewSentence newSentence : newAnnotation.getNewSentences())
        {
            String normalWord;
            for (Word word : newSentence.getWords())
            {
                normalWord = word.getForm().replace("_", " ").replace("Vua", "").trim();
                if (word.getNerLabel().equals("B-PER") && isUpperCase(normalWord, "_"))
                {
                        
                        result.add(normalWord);
                        //System.out.println(normalWord);
                        
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException
    {
        Document doc = Jsoup.connect("https://vi.wikipedia.org/w/index.php?title=%C4%90%C3%ACnh_Nam_H%C6%B0%C6%A1ng&action=edit&redlink=1").get();
        Elements pTags = doc.select("#mw-content-text > div.mw-parser-output > p");
        pTags.select("sup").remove();
        Crawler crawler = new Crawler();
        System.out.println(crawler.extractPersonalNameEntityFrom("Ông Nguyễn Khắc Chúc  đang làm việc tại Đại học Quốc gia Hà Nội. Bà Lan, vợ ông Chúc, cũng làm việc tại đây."));
    }
}
