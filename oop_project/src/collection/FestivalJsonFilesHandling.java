package collection;

import java.io.IOException;

public interface FestivalJsonFilesHandling 
{
    public final static String DIR_NAME = "\\Festival";

    public abstract void toJsonFiles();
    
    public abstract void loadJsonFiles() throws IOException;
}
