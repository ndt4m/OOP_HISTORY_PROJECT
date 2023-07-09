package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HistoricalSite extends Entity
{
    private String location;
    private String establishment;
    private String category;
    private Map<String, Integer> relatedCharacters = new HashMap<String, Integer>();

    public HistoricalSite()
    {

    }

    public HistoricalSite(String historicalSiteName,
                          String location,
                          String establishment,
                          String category,
                          String overview,
                          Set<String> aliases,
                          Set<String> relatedCharacters)
    {
        super(historicalSiteName, aliases, overview);
        this.location = location;
        this.establishment = establishment;
        this.category = category;
        for (String charName : relatedCharacters)
        {
            this.relatedCharacters.put(charName, null);
        }
    }

    public String getLocation() {
        return location;
    }

    public String getEstablishment() {
        return establishment;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, Integer> getRelatedCharacters() {
        return relatedCharacters;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRelatedCharacters(Map<String, Integer> relatedCharacters) {
        this.relatedCharacters = relatedCharacters;
    }
}
