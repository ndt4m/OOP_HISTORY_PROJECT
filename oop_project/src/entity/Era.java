package entity;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Era extends Entity
{
    private String time;
    private String founder;
    private String capital;
    private Map<String, Integer> relatedCharacters = new HashMap<String, Integer>();


    public Era()
    {

    }

    public Era(String eraName,
               String time,
               String founder,
               String capital,
               String overview,
               Set<String> aliases,
               Set<String> relatedCharacters)          
    {
        super(eraName, aliases, overview);
        this.time = time;
        this.founder = founder;
        this.capital = capital;
        for (String charName : relatedCharacters)
        {
            this.relatedCharacters.put(charName, null);
        }
    }

    public String getTime()
    {
        return this.time;
    }

    public String getFounder() 
    {
        return this.founder;
    }

    public String getCapital() 
    {
        return this.capital;
    }

    public Map<String, Integer> getRelatedCharacters() 
    {
        return this.relatedCharacters;
    }

    public void setTime(String time) 
    {
        this.time = time;
    }

    public void setFounder(String founder) 
    {
        this.founder = founder;
    }

    public void setCapital(String capital) 
    {
        this.capital = capital;
    }

    public void setRelatedCharacters(Map<String, Integer> relatedCharacters) 
    {
        this.relatedCharacters = relatedCharacters;
    }
}
