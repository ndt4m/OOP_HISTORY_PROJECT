package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Event extends Entity
{
    private String location;
    private String time;
    private String result;
    private Map<String, Integer> relatedCharacters = new HashMap<String, Integer>();

    public Event()
    {

    }

    public Event(String eventName,
                 String location,
                 String time,
                 String result,
                 String overview,
                 Set<String> aliases,
                 Set<String> relatedCharacters)
    {
        super(eventName, aliases, overview);
        this.time = time;
        this.result = result;
        this.location = location;
        for (String charName : relatedCharacters)
        {
            this.relatedCharacters.put(charName, null);
        }
    }


    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getResult() {
        return result;
    }

    public Map<String, Integer> getRelatedCharacters() {
        return relatedCharacters;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRelatedCharacters(Map<String, Integer> relatedCharacters) {
        this.relatedCharacters = relatedCharacters;
    }
}