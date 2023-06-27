import java.io.IOException;

import collection.EraCollection;
import entity.Era;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        EraCollection eraCollection = new EraCollection();
        eraCollection.loadJsonFiles();
        for (Era era: eraCollection.getData())
        {
            System.out.println(era.getEntityName());
        }
    }
}
