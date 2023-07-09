package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HistoricalCharacter extends Entity
{
    private String dateOfBirth;
    private String dateOfDeath;
    private String hometown;
    private String occupation;
    private String workTenure;
    private Map<String, Integer> fatherName = new HashMap<String, Integer>();
    private Map<String, Integer> motherName = new HashMap<String, Integer>();
    private Map<String, Integer> eraName = new HashMap<String, Integer>();

    public HistoricalCharacter()
    {

    }

    public HistoricalCharacter(String charName,
                               String eraName,
                               String dateOfBirth,
                               String dateOfDeath,
                               String fatherName,
                               String motherName,
                               String hometown,
                               String occupation,
                               String workTenure,
                               String overview,
                               Set<String> aliases)
    {
        super(charName, aliases, overview);
        if (!eraName.equals("Không rõ"))
        {
            this.eraName.put(eraName, null);
        }
        if (!fatherName.equals("Không rõ"))
        {
            this.fatherName.put(fatherName, null);
        }
        if (!motherName.equals("Không rõ"))
        {
            this.motherName.put(motherName, null);
        }
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.hometown = hometown;
        this.occupation = occupation;
        this.workTenure = workTenure;
    }

    public HistoricalCharacter(String charName,
                               Set<String> eraNames,
                               String dateOfBirth,
                               String dateOfDeath,
                               String fatherName,
                               String motherName,
                               String hometown,
                               String occupation,
                               String workTenure,
                               String overview,
                               Set<String> aliases)
    {
        super(charName, aliases, overview);
        if (!fatherName.equals("Không rõ"))
        {
            this.fatherName.put(fatherName, null);
        }
        if (!motherName.equals("Không rõ"))
        {
            this.motherName.put(motherName, null);
        }
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.hometown = hometown;
        this.occupation = occupation;
        this.workTenure = workTenure;
        for (String eraName: eraNames)
        {
            if (!eraName.equals("Không rõ"))
            {
                this.eraName.put(eraName, null);
            }
        }
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public String getHometown() {
        return hometown;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getWorkTenure() {
        return workTenure;
    }

    public Map<String, Integer> getFatherName() {
        return fatherName;
    }

    public Map<String, Integer> getMotherName() {
        return motherName;
    }

    public Map<String, Integer> getEraName() {
        return eraName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setWorkTenure(String workTenure) {
        this.workTenure = workTenure;
    }

    public void setFatherName(Map<String, Integer> fatherName) {
        this.fatherName = fatherName;
    }

    public void setMotherName(Map<String, Integer> motherName) {
        this.motherName = motherName;
    }

    public void setEraName(Map<String, Integer> eraName) {
        this.eraName = eraName;
    }
}
