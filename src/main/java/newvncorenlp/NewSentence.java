package newvncorenlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vn.corenlp.parser.DependencyParser;
import vn.corenlp.postagger.PosTagger;
import vn.corenlp.tokenizer.Tokenizer;
import vn.corenlp.wordsegmenter.WordSegmenter;
import vn.pipeline.Sentence;
import vn.pipeline.Word;

public class NewSentence extends Sentence {
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