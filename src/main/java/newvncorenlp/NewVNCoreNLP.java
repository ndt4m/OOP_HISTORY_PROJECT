package newvncorenlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import vn.corenlp.parser.DependencyParser;
import vn.corenlp.postagger.PosTagger;
import vn.corenlp.tokenizer.Tokenizer;
import vn.corenlp.wordsegmenter.WordSegmenter;
import vn.pipeline.VnCoreNLP;

public class NewVNCoreNLP extends VnCoreNLP
{
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