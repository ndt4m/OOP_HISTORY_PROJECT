package collection;

import java.io.IOException;

public interface HistoricalSiteJsonFilesHandling
{
    public final static String DIR_NAME = "\\HistoricalSite";

    public abstract void toJsonFiles();

    public abstract void loadJsonFiles() throws IOException;
}