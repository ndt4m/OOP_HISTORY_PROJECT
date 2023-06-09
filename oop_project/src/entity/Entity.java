package entity;

import java.util.HashSet;
import java.util.Set;

public abstract class Entity 
{
    protected int id = -1;
    protected String entityName;
    protected Set<String> aliases = new HashSet<String>();
    protected String overview;

    public Entity()
    {

    }

    public Entity(String entityName, Set<String> aliases, String overview) 
    {
        this.entityName = entityName;
        this.aliases = aliases;
        this.overview = overview;
    }

    public int getId()
    {
        return this.id;
    }

    public String getEntityName()
    {
        return this.entityName;
    }

    public Set<String> getAliases()
    {
        return this.aliases;
    }

    public String getOverview()
    {
        return this.overview;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public void setAliases(Set<String> aliases) 
    {
        this.aliases = aliases;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public Set<String> getAllPossibleNames()
    {
        Set<String> allPossibleNames = new HashSet<String>(this.aliases);
        allPossibleNames.add(entityName);
        return allPossibleNames;
    }

    public boolean isOverlap(Entity obj)
    {
        for (String nameObj1 : this.getAllPossibleNames())
        {
            for (String nameObj2: obj.getAllPossibleNames())
            {
                if (nameObj1.equalsIgnoreCase(nameObj2))
                {
                    return true;
                }
            }
        }
        return false;
    }

    
}

