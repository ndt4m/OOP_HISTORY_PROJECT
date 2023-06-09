package collection;

import java.io.IOException;

public interface EraJsonFilesHandling 
{
    public final static String DIR_NAME = "\\Era";

    public abstract void toJsonFiles();
    
    public abstract void loadJsonFiles() throws IOException;
}
