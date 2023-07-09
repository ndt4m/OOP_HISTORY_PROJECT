package newvncorenlp;

import java.util.ArrayList;
import java.util.List;

import vn.pipeline.Annotation;
import vn.pipeline.Word;

public class NewAnnotation extends Annotation{
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

    public void setNewSentences(List<NewSentence> newSentences) {
        this.newSentences = newSentences;
    }
}