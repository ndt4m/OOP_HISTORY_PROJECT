package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Festival extends Entity
{
    private String time;
    private String location;
    private Map<String, Integer> relatedCharacters = new HashMap<String, Integer>();

    public Festival()
    {

    }

    public Festival(String festivalName,
                    String location,
                    String time,
                    String overview,
                    Set<String> aliases,
                    Set<String> relatedCharacters)
    {
        super(festivalName, aliases, overview);
        this.time = time;
        this.location = location;
        for (String charName : relatedCharacters)
        {
            this.relatedCharacters.put(charName, null);
        }
    }


    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public Map<String, Integer> getRelatedCharacters() {
        return relatedCharacters;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRelatedCharacters(Map<String, Integer> relatedCharacters) {
        this.relatedCharacters = relatedCharacters;
    }
}