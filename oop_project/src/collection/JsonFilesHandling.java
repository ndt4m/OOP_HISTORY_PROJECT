package collection;

import java.io.IOException;

public interface JsonFilesHandling {
    public abstract void toJsonFiles();
    
    public abstract void loadJsonFiles() throws IOException;
}
