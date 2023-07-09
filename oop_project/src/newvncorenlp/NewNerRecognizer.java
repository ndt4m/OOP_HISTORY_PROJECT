package newvncorenlp;


import java.io.IOException;



import vn.corenlp.ner.NerRecognizer;

import vn.pipeline.Utils;
import vn.pipeline.Word;

public class NewNerRecognizer extends NerRecognizer
{
    private static NewNerRecognizer newNerRecognizer;
    public static NewNerRecognizer initialize() throws IOException{
        if(newNerRecognizer == null) {
            newNerRecognizer = new NewNerRecognizer();
        }
        return newNerRecognizer;
    }

    public NewNerRecognizer() throws IOException
    {
       super();
    }

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
