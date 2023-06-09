package collection;

import java.io.IOException;

public interface HistoricalCharJsonFilesHandling 
{
    public final static String DIR_NAME = "\\HistoricalCharacter";

    public abstract void toJsonFiles();
    
    public abstract void loadJsonFiles() throws IOException;
}
