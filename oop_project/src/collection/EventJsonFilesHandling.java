package collection;

import java.io.IOException;

public interface EventJsonFilesHandling 
{
    public final static String DIR_NAME = "\\Event";

    public abstract void toJsonFiles();
    
    public abstract void loadJsonFiles() throws IOException;
}
