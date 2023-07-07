package crawling;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.print.Doc;

import vn.pipeline.*;

import org.apache.log4j.Logger;

import edu.emory.mathcs.nlp.common.util.NLPUtils;
import edu.emory.mathcs.nlp.component.template.NLPComponent;
import edu.emory.mathcs.nlp.component.template.lexicon.GlobalLexica;
import edu.emory.mathcs.nlp.component.template.node.FeatMap;
import edu.emory.mathcs.nlp.component.template.node.NLPNode;
import edu.emory.mathcs.nlp.decode.NLPDecoder;

import vn.corenlp.ner.NerRecognizer;
import vn.corenlp.parser.DependencyParser;
import vn.corenlp.postagger.PosTagger;
import vn.corenlp.wordsegmenter.WordSegmenter;
import vn.corenlp.wordsegmenter.Vocabulary;
import vn.corenlp.tokenizer.Tokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler
{
    public Crawler()
    {

    }

    public boolean isUpperCase(String s)
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
                if (word.getNerLabel().equals("B-PER") && isUpperCase(normalWord))
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
        crawler.extractPersonalNameEntityFrom(pTags.text());
    }
}


class NewAnnotation extends Annotation
{
    private String rawText;
    private List<String> tokens;
    private String wordSegmentedText;
    private List<Word> words;
    private List<NewSentence> newSentences;

    public NewAnnotation(String rawText)
    {
        super(rawText);
        this.rawText = rawText.trim();
        this.tokens = new ArrayList<>();
        this.wordSegmentedText = "";
        this.words = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(newSentences != null)
            for(NewSentence newSentence : newSentences) {
                sb.append(newSentence.toString() + "\n\n");
            }
        else return rawText;
        return sb.toString();
    }

    public List<NewSentence> getNewSentences()
    {
        return newSentences;
    }

    public String getWordSegmentedTaggedText() {
        StringBuffer wordSegmentedTaggedText = new StringBuffer();
        for(NewSentence newSentence : newSentences) {
            wordSegmentedTaggedText.append(newSentence.getWordSegmentedTaggedSentence() + " ");
        }
        return wordSegmentedTaggedText.toString().trim();
    }

    public String getRawText()
    {
        return this.rawText;
    }

    public void setNewSentences(List<NewSentence> newSentences) {
        this.newSentences = newSentences;
    }
}

class NewSentence extends Sentence
{
    private String rawSentence;
    private List<String> tokens;
    private String wordSegmentedSentence;

    private List<Word> words;

    private WordSegmenter wordSegmenter ;
    private PosTagger posTagger;
    private NewNerRecognizer newNerRecognizer;
    private DependencyParser dependencyParser;


    public NewSentence(String rawSentence, WordSegmenter wordSegmenter, PosTagger tagger, NewNerRecognizer newNerRecognizer, DependencyParser dependencyParser) throws IOException
    {
        super(rawSentence, wordSegmenter, tagger, newNerRecognizer, dependencyParser);
        this.posTagger = tagger;
        this.newNerRecognizer = newNerRecognizer;
        this.dependencyParser = dependencyParser;
        this.wordSegmenter = wordSegmenter;
        init(rawSentence.trim());
    }

    private void init(String rawSentence) throws IOException {
        this.rawSentence = rawSentence;
        this.tokens = Tokenizer.tokenize(this.rawSentence);

        if(this.wordSegmenter != null) {
            this.wordSegmentedSentence = this.wordSegmenter.segmentTokenizedString(this.rawSentence);
        }
        else this.wordSegmentedSentence = String.join(" ", this.tokens);

        this.createWords();

    }

    private void createWords() throws IOException {

        if (this.posTagger != null)
            this.words = posTagger.tagSentence(this.wordSegmentedSentence);
        else {
            this.words = new ArrayList<>();
            String[] segmentedTokens = this.wordSegmentedSentence.split(" ");
            for (int i = 0; i < segmentedTokens.length; i++) {
                Word word = new Word((i+1), segmentedTokens[i]);
                this.words.add(word);
            }
        }

        if (this.newNerRecognizer != null)
            this.newNerRecognizer.tagSentence(this.words);
        if (this.dependencyParser != null)
            this.dependencyParser.tagSentence(this.words);
    }

}

class NewVNCoreNLP extends VnCoreNLP
{
    public final static Logger LOGGER = Logger.getLogger(NewAnnotation.class);

    private WordSegmenter wordSegmenter;
    private PosTagger posTagger;
    private NewNerRecognizer newNerRecognizer;
    private DependencyParser dependencyParser;

    public NewVNCoreNLP() throws IOException
    {
        String[] annotators = {"wseg", "pos", "ner", "parse"};
        initAnnotators(annotators);
    }

    public NewVNCoreNLP(String[] annotators) throws IOException {
        initAnnotators(annotators);

    }

    public void initAnnotators(String[] annotators) throws IOException{
        for(String annotator : annotators) {
            switch (annotator.trim()) {
                case "parse":
                    this.dependencyParser = DependencyParser.initialize();
                    break;
                case "ner":
                    this.newNerRecognizer = NewNerRecognizer.initialize();
                    break;
                case "pos":
                    this.posTagger = PosTagger.initialize();
                    break;
                case "wseg":
                    this.wordSegmenter = WordSegmenter.initialize();
                    break;
            }
        }

    }

    public void printToFile(NewAnnotation newAnnotation, PrintStream printer) throws IOException {
        for(NewSentence newSentence : newAnnotation.getNewSentences()) {
            printer.println(newSentence.toString());
        }
    }

    public void printToFile(NewAnnotation newAnnotation, String fileOut) throws IOException {
        PrintStream printer = new PrintStream(fileOut, "UTF-8");
        for(NewSentence newSentence : newAnnotation.getNewSentences()) {
            printer.println(newSentence.toString() + "\n");
        }
        printer.close();
    }

    public void annotate(NewAnnotation newAnnotation) throws IOException {
        List<String> rawSentences = Tokenizer.joinSentences(Tokenizer.tokenize(newAnnotation.getRawText()));
        // System.out.println("=============="+rawSentences.size()+"=============");
        // System.out.println("=============="+annotation.getRawText().split(".").length+"=============");
        newAnnotation.setNewSentences(new ArrayList<>());
        for (String rawSentence : rawSentences) {
            if (rawSentence.trim().length() > 0) {
                NewSentence newSentence = new NewSentence(rawSentence, wordSegmenter, posTagger, newNerRecognizer, dependencyParser);
                newAnnotation.getNewSentences().add(newSentence);
                newAnnotation.getTokens().addAll(newSentence.getTokens());
                newAnnotation.getWords().addAll(newSentence.getWords());
                newAnnotation.setWordSegmentedText(newAnnotation.getWordSegmentedTaggedText() + newSentence.getWordSegmentedSentence() + " ");
            }

        }

        newAnnotation.setWordSegmentedText(newAnnotation.getWordSegmentedTaggedText().trim());

    }
}

class NewNerRecognizer extends NerRecognizer
{
    private NLPDecoder nlpDecoder ;
    public final static Logger LOGGER = Logger.getLogger(NewNerRecognizer.class);
    private static NewNerRecognizer newNerRecognizer;
    public static NewNerRecognizer initialize() throws IOException{
        if(newNerRecognizer == null) {
            newNerRecognizer = new NewNerRecognizer();
        }
        return newNerRecognizer;
    }

    public NewNerRecognizer() throws IOException
    {
        LOGGER.info("Loading NER model");
        nlpDecoder = new NLPDecoder();
        List<NLPComponent<NLPNode>> components = new ArrayList();

        String modelPath = Utils.jarDir + "/models/ner/vi-ner.xz";
        if (!new File(modelPath).exists()) throw new IOException("NewNerRecognizer: " + modelPath + " is not found!");
        GlobalLexica lexica = LexicalInitializer.initialize(true).initializeLexica();
        if(lexica != null) {
            components.add(lexica);
        }
        components.add(NLPUtils.getComponent(modelPath));
        nlpDecoder.setComponents(components);
    }

    // private NLPNode[] toNodeArray(List<Word> sentenceWords) {
    //     NLPNode[] nlpNodes = new NLPNode[sentenceWords.size() + 1];
    //     nlpNodes[0] = new NLPNode();
    //     for(int i = 0; i < sentenceWords.size(); i++) {
    //         Word word = sentenceWords.get(i);
    //         nlpNodes[i + 1] = new NLPNode(word.getIndex(), word.getForm(), word.getForm(), addLabelForPOSTag(word), new FeatMap());
    //     }
    //     return nlpNodes;
    // }

    @Override
    public String addLabelForPOSTag(Word word)
    {
        try
        {
            String[] tokens = word.getForm().split("_");
            String normalWord = word.getForm().replace("_", " ");
            String output = word.getPosTag();
            if (tokens.length >= 3 && word.getPosTag() != null && word.getPosTag().equals("Np") && !NewVocabulary.VN_ETHNICITY_NAMES.contains(normalWord) && !NewVocabulary.VN_ERA_NAMES.contains(normalWord) && Utils.detectLanguage(normalWord).equals("vi"))
            {
                if ((NewVocabulary.VN_FAMILY_NAMES.contains(tokens[0].toLowerCase())))
                {
                    output = word.getPosTag() + "-1";
                }
                else output = word.getPosTag() + "-0";
            }
            else
            {
                output = "A";
            }
            return output;
        }
        catch (IOException e) {
            // Exception handling code
            e.printStackTrace(); // Example: Printing the stack trace
            return "ERROR";
        }
    }
}

class NewVocabulary extends Vocabulary
{
    public static Set<String> VN_ETHNICITY_NAMES;
    static
    {
        VN_ETHNICITY_NAMES = new HashSet<String>();
        VN_ETHNICITY_NAMES.add("Kinh");
        VN_ETHNICITY_NAMES.add("Chứt");
        VN_ETHNICITY_NAMES.add("Xá La Vàng");
        VN_ETHNICITY_NAMES.add("Chà Củi");
        VN_ETHNICITY_NAMES.add("Tắc Củi");
        VN_ETHNICITY_NAMES.add("Mày");
        VN_ETHNICITY_NAMES.add("Mã Liềng");
        VN_ETHNICITY_NAMES.add("Rục");
        VN_ETHNICITY_NAMES.add("Mol");
        VN_ETHNICITY_NAMES.add("Mual");
        VN_ETHNICITY_NAMES.add("Mường");
        VN_ETHNICITY_NAMES.add("Thổ");
        VN_ETHNICITY_NAMES.add("Kẹo");
        VN_ETHNICITY_NAMES.add("Mọn");
        VN_ETHNICITY_NAMES.add("Họ");
        VN_ETHNICITY_NAMES.add("Cuối");
        VN_ETHNICITY_NAMES.add("Đan Lai");
        VN_ETHNICITY_NAMES.add("Ly Hà");
        VN_ETHNICITY_NAMES.add("Tày Poọng");
        VN_ETHNICITY_NAMES.add("Tày Poọng");
        VN_ETHNICITY_NAMES.add("Chủng Chá");
        VN_ETHNICITY_NAMES.add("Trung Gia");
        VN_ETHNICITY_NAMES.add("Pầu Y");
        VN_ETHNICITY_NAMES.add("Pầu Y");
        VN_ETHNICITY_NAMES.add("Pầu Y");
        VN_ETHNICITY_NAMES.add("Nhắng");
        VN_ETHNICITY_NAMES.add("Giắng");
        VN_ETHNICITY_NAMES.add("Sa Nhân");
        VN_ETHNICITY_NAMES.add("Pấu Thỉn");
        VN_ETHNICITY_NAMES.add("Chủng Chá");
        VN_ETHNICITY_NAMES.add("Pu Năm");
        VN_ETHNICITY_NAMES.add("Lào Bốc");
        VN_ETHNICITY_NAMES.add("Lào Nọi");
        VN_ETHNICITY_NAMES.add("Lào");
        VN_ETHNICITY_NAMES.add("Lự");
        VN_ETHNICITY_NAMES.add("Lừ");
        VN_ETHNICITY_NAMES.add("Duôn");
        VN_ETHNICITY_NAMES.add("Nhuồn");
        VN_ETHNICITY_NAMES.add("Nùng");
        VN_ETHNICITY_NAMES.add("Sán Chay");
        VN_ETHNICITY_NAMES.add("Mán");
        VN_ETHNICITY_NAMES.add("Cao Lan");
        VN_ETHNICITY_NAMES.add("Sán Chỉ");
        VN_ETHNICITY_NAMES.add("Cao Lan");
        VN_ETHNICITY_NAMES.add("Hờn Chùng");
        VN_ETHNICITY_NAMES.add("Sơn Tử");
        VN_ETHNICITY_NAMES.add("Tày");
        VN_ETHNICITY_NAMES.add("Thái");
        VN_ETHNICITY_NAMES.add("Thổ");
        VN_ETHNICITY_NAMES.add("Táy");
        VN_ETHNICITY_NAMES.add("Thái Trắng,");
        VN_ETHNICITY_NAMES.add("Thái Đen");
        VN_ETHNICITY_NAMES.add("Thái Đỏ");
        VN_ETHNICITY_NAMES.add("Cờ Lao");
        VN_ETHNICITY_NAMES.add("Gelao");
        VN_ETHNICITY_NAMES.add("La Chí");
        VN_ETHNICITY_NAMES.add("Thổ Đen");
        VN_ETHNICITY_NAMES.add("Cù Tê");
        VN_ETHNICITY_NAMES.add("Xá");
        VN_ETHNICITY_NAMES.add("La Ti");
        VN_ETHNICITY_NAMES.add("Mán Chí");
        VN_ETHNICITY_NAMES.add("Xá Khao");
        VN_ETHNICITY_NAMES.add("Xá Cha");
        VN_ETHNICITY_NAMES.add("Xá La Nga");
        VN_ETHNICITY_NAMES.add("La Ha");
        VN_ETHNICITY_NAMES.add("Pu Péo");
        VN_ETHNICITY_NAMES.add("Ka Bẻo");
        VN_ETHNICITY_NAMES.add("Mán");
        VN_ETHNICITY_NAMES.add("Ba Na");
        VN_ETHNICITY_NAMES.add("Chơ Ro");
        VN_ETHNICITY_NAMES.add("Bru - Vân Kiều");
        VN_ETHNICITY_NAMES.add("Brâu");
        VN_ETHNICITY_NAMES.add("Vân Kiều");
        VN_ETHNICITY_NAMES.add("Ma Coong");
        VN_ETHNICITY_NAMES.add("Khùa");
        VN_ETHNICITY_NAMES.add("Trì");
        VN_ETHNICITY_NAMES.add("Châu Ro");
        VN_ETHNICITY_NAMES.add("Dơ Ro");
        VN_ETHNICITY_NAMES.add("Co");
        VN_ETHNICITY_NAMES.add("Trầu");
        VN_ETHNICITY_NAMES.add("Col");
        VN_ETHNICITY_NAMES.add("Cơ Ho");
        VN_ETHNICITY_NAMES.add("Ca Tu");
        VN_ETHNICITY_NAMES.add("Ca Tang");
        VN_ETHNICITY_NAMES.add("Hạ");
        VN_ETHNICITY_NAMES.add("Giẻ Triêng");
        VN_ETHNICITY_NAMES.add("Hrê");
        VN_ETHNICITY_NAMES.add("Khmer");
        VN_ETHNICITY_NAMES.add("Khơ Mú");
        VN_ETHNICITY_NAMES.add("Mạ");
        VN_ETHNICITY_NAMES.add("Thạch Bích");
        VN_ETHNICITY_NAMES.add("Chăm Rê");
        VN_ETHNICITY_NAMES.add("Chăm Rê");
        VN_ETHNICITY_NAMES.add("Cà Tang");
        VN_ETHNICITY_NAMES.add("Xá Tú Lăng");
        VN_ETHNICITY_NAMES.add("Xá Đón");
        VN_ETHNICITY_NAMES.add("Xá Đón");
        VN_ETHNICITY_NAMES.add("Xá Cẩu");
        VN_ETHNICITY_NAMES.add("Tày Hạy");
        VN_ETHNICITY_NAMES.add("Việt Cang");
        VN_ETHNICITY_NAMES.add("Khá Klậu");
        VN_ETHNICITY_NAMES.add("Mảng");
        VN_ETHNICITY_NAMES.add("Mảng Ư");
        VN_ETHNICITY_NAMES.add("Xá Lá Vàng");
        VN_ETHNICITY_NAMES.add("Niễng O");
        VN_ETHNICITY_NAMES.add("Niễng O");
        VN_ETHNICITY_NAMES.add("Niễng O");
        VN_ETHNICITY_NAMES.add("M’Nông");
        VN_ETHNICITY_NAMES.add("Ơ Đu");
        VN_ETHNICITY_NAMES.add("Tày Hạt");
        VN_ETHNICITY_NAMES.add("Rơ Măm");
        VN_ETHNICITY_NAMES.add("Tà Ôi");
        VN_ETHNICITY_NAMES.add("Tôi Ôi");
        VN_ETHNICITY_NAMES.add("Ta Hoi");
        VN_ETHNICITY_NAMES.add("Ta Ôih");
        VN_ETHNICITY_NAMES.add("Tà Uất");
        VN_ETHNICITY_NAMES.add("A tuất");
        VN_ETHNICITY_NAMES.add("Pa Cô");
        VN_ETHNICITY_NAMES.add("Xinh Mun");
        VN_ETHNICITY_NAMES.add("Xơ Đăng");
        VN_ETHNICITY_NAMES.add("Con Lan");
        VN_ETHNICITY_NAMES.add("Con Lan");
        VN_ETHNICITY_NAMES.add("Tơ-dra");
        VN_ETHNICITY_NAMES.add("X’Tiêng");
        VN_ETHNICITY_NAMES.add("Xa Điêng");
        VN_ETHNICITY_NAMES.add("Tà Mun");
        VN_ETHNICITY_NAMES.add("Dao");
        VN_ETHNICITY_NAMES.add("Động");
        VN_ETHNICITY_NAMES.add("Kìm Mùn");
        VN_ETHNICITY_NAMES.add("Pà Thẻn");
        VN_ETHNICITY_NAMES.add("Pà Hưng");
        VN_ETHNICITY_NAMES.add("Pà Hưng");
        VN_ETHNICITY_NAMES.add("Miêu Tộc");
        VN_ETHNICITY_NAMES.add("Pà Thẻn");
        VN_ETHNICITY_NAMES.add("Pà Hưng");
        VN_ETHNICITY_NAMES.add("Mán Pa Teng");
        VN_ETHNICITY_NAMES.add("Chăm");
        VN_ETHNICITY_NAMES.add("Chiêm Thành");
        VN_ETHNICITY_NAMES.add("Chăm Pa");
        VN_ETHNICITY_NAMES.add("Chơ Ru");
        VN_ETHNICITY_NAMES.add("Chu Ru");
        VN_ETHNICITY_NAMES.add("Ê Đê");
        VN_ETHNICITY_NAMES.add("Ra đê");
        VN_ETHNICITY_NAMES.add("Gia Rai");
        VN_ETHNICITY_NAMES.add("Ra Glai");
        VN_ETHNICITY_NAMES.add("Ra Glay");
        VN_ETHNICITY_NAMES.add("O Rang");
        VN_ETHNICITY_NAMES.add("Hoa");
        VN_ETHNICITY_NAMES.add("Ngái");
        VN_ETHNICITY_NAMES.add("Ngái");
        VN_ETHNICITY_NAMES.add("Sán Dìu");
        VN_ETHNICITY_NAMES.add("Trại Đát");
        VN_ETHNICITY_NAMES.add("Sán Rợ");
        VN_ETHNICITY_NAMES.add("Mán quần cộc");
        VN_ETHNICITY_NAMES.add("Mán váy xẻ");
        VN_ETHNICITY_NAMES.add("Cống");
        VN_ETHNICITY_NAMES.add("Hà Nhì");
        VN_ETHNICITY_NAMES.add("U Ní");
        VN_ETHNICITY_NAMES.add("Xá U Ní");
        VN_ETHNICITY_NAMES.add("Hà Nhì Già");
        VN_ETHNICITY_NAMES.add("Lô Lô");
        VN_ETHNICITY_NAMES.add("La Hủ");
        VN_ETHNICITY_NAMES.add("Mùn Di");
        VN_ETHNICITY_NAMES.add("Ô Man");
        VN_ETHNICITY_NAMES.add("Lu Lọc Màn");
        VN_ETHNICITY_NAMES.add("Qua La");
        VN_ETHNICITY_NAMES.add("La La");
        VN_ETHNICITY_NAMES.add("Ma Di");
        VN_ETHNICITY_NAMES.add("Phù Lá");
        VN_ETHNICITY_NAMES.add("Phú Lá");
        VN_ETHNICITY_NAMES.add("Xá Phó");
        VN_ETHNICITY_NAMES.add("Si La");
        VN_ETHNICITY_NAMES.add("Cú Đề Xừ");
        VN_ETHNICITY_NAMES.add("Tây Nguyên");
        VN_ETHNICITY_NAMES.add("Pa Kô");
        VN_ETHNICITY_NAMES.add("Nguồn");
        VN_ETHNICITY_NAMES.add("Arem");
        VN_ETHNICITY_NAMES.add("Đan Lai");
        VN_ETHNICITY_NAMES.add("Tà Mun");
        VN_ETHNICITY_NAMES.add("Thủy");
        VN_ETHNICITY_NAMES.add("Xạ Phang");
        VN_ETHNICITY_NAMES.add("Pú Nả");
        VN_ETHNICITY_NAMES.add("Ngái");
        VN_ETHNICITY_NAMES.add("Đản");
        VN_ETHNICITY_NAMES.add("Hoa Nùng");
        VN_ETHNICITY_NAMES.add("En");
        VN_ETHNICITY_NAMES.add("Mơ Piu");
        VN_ETHNICITY_NAMES.add("Thu Lao");
        VN_ETHNICITY_NAMES.add("Pa Dí");
    }

    public static Set<String> VN_ERA_NAMES;
    static
    {
        VN_ERA_NAMES = new HashSet<String>();
        VN_ERA_NAMES.add("Hồng Bàng thị");
        VN_ERA_NAMES.add("Nhà Thục");
        VN_ERA_NAMES.add("Nhà Triệu");
        VN_ERA_NAMES.add("Họ Trưng");
        VN_ERA_NAMES.add("Nhà Tiền Lý");
        VN_ERA_NAMES.add("Tiền Lý");
        VN_ERA_NAMES.add("Họ Triệu");
        VN_ERA_NAMES.add("Họ Mai");
        VN_ERA_NAMES.add("Họ Phùng");
        VN_ERA_NAMES.add("Họ Khúc");
        VN_ERA_NAMES.add("Họ Dương");
        VN_ERA_NAMES.add("Họ Kiều");
        VN_ERA_NAMES.add("Nhà Ngô");
        VN_ERA_NAMES.add("Nhà Đinh");
        VN_ERA_NAMES.add("Nhà Tiền Lê");
        VN_ERA_NAMES.add("Tiền Lê");
        VN_ERA_NAMES.add("Nhà Lý");
        VN_ERA_NAMES.add("Nhà Trần");
        VN_ERA_NAMES.add("Nhà Hồ");
        VN_ERA_NAMES.add("Nhà Hậu Trần");
        VN_ERA_NAMES.add("Hậu Trần");
        VN_ERA_NAMES.add("Nhà Hậu Lê");
        VN_ERA_NAMES.add("Hậu Lê");
        VN_ERA_NAMES.add("Nhà Mạc");
        VN_ERA_NAMES.add("Chúa Trịnh");
        VN_ERA_NAMES.add("Đàng Trong");
        VN_ERA_NAMES.add("Chúa Nguyễn");
        VN_ERA_NAMES.add("Đàng Ngoài");
        VN_ERA_NAMES.add("Nhà Tây Sơn");
        VN_ERA_NAMES.add("Nhà Nguyễn");
    }
}







