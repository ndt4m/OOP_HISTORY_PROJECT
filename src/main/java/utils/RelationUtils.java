package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collection.EraCollection;
import collection.EventCollection;
import collection.FestivalCollection;
import collection.HistoricalCharCollection;
import collection.HistoricalSiteCollection;
import entity.Era;
import entity.Event;
import entity.Festival;
import entity.HistoricalCharacter;
import entity.HistoricalSite;

/**
 * tạo những tìm kiếm có liên quan giữa các dữ liệu với nhau
 */
public class RelationUtils
{
    public static void relateCharToEra(EraCollection eraCollection, HistoricalCharCollection charCollection, EraCollection newEraCollection)
    {
        List<Era> newEras = new ArrayList<>();
        for (Era era: eraCollection.getData())
        {
            Map<String, Integer> newMap = new HashMap<>();
            for (Map.Entry<String, Integer> entity :era.getRelatedCharacters().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter figure: charCollection.getData())
                {
                    if (figure.getAllPossibleNames().contains(entity.getKey()))
                    {
                        newMap.put(entity.getKey(), figure.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMap.put(entity.getKey(), entity.getValue());
                }
            }
            era.setRelatedCharacters(newMap);
            newEras.add(era);
        }
        newEraCollection.setData(newEras);
    }

    public static void relateCharToEvent(EventCollection eventCollection, HistoricalCharCollection charCollection, EventCollection newEventCollection)
    {
        List<Event> newEvents = new ArrayList<>();
        for (Event event: eventCollection.getData())
        {
            Map<String, Integer> newMap = new HashMap<>();
            for (Map.Entry<String, Integer> entity :event.getRelatedCharacters().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter figure: charCollection.getData())
                {
                    if (figure.getAllPossibleNames().contains(entity.getKey()))
                    {
                        newMap.put(entity.getKey(), figure.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMap.put(entity.getKey(), entity.getValue());
                }
            }
            event.setRelatedCharacters(newMap);
            newEvents.add(event);
        }
        newEventCollection.setData(newEvents);
    }

    public static void relateCharToHistorycalSite(HistoricalSiteCollection siteCollection, HistoricalCharCollection charCollection, HistoricalSiteCollection newSiteCollection)
    {
        List<HistoricalSite> newSites = new ArrayList<>();
        for (HistoricalSite site: siteCollection.getData())
        {
            Map<String, Integer> newMap = new HashMap<>();
            for (Map.Entry<String, Integer> entity : site.getRelatedCharacters().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter figure: charCollection.getData())
                {
                    if (figure.getAllPossibleNames().contains(entity.getKey()))
                    {
                        newMap.put(entity.getKey(), figure.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMap.put(entity.getKey(), entity.getValue());
                }
            }
            site.setRelatedCharacters(newMap);
            newSites.add(site);
        }
        newSiteCollection.setData(newSites);
    }

    public static void relateCharToFestival(FestivalCollection festivalCollection, HistoricalCharCollection charCollection, FestivalCollection newFestivalCollection)
    {
        List<Festival> newFestivals = new ArrayList<>();
        for (Festival festival: festivalCollection.getData())
        {
            Map<String, Integer> newMap = new HashMap<>();
            for (Map.Entry<String, Integer> entity: festival.getRelatedCharacters().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter figure: charCollection.getData())
                {
                    if (figure.getAllPossibleNames().contains(entity.getKey()))
                    {
                        newMap.put(entity.getKey(), figure.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMap.put(entity.getKey(), entity.getValue());
                }
            }
            festival.setRelatedCharacters(newMap);
            newFestivals.add(festival);
        }
        newFestivalCollection.setData(newFestivals);
    }

    public static void relateEraToChar(HistoricalCharCollection charCollection, EraCollection eraCollection, HistoricalCharCollection newCharCollection)
    {
        List<HistoricalCharacter> newChars = new ArrayList<>();
        for (HistoricalCharacter figure: charCollection.getData())
        {
            Map<String, Integer> newMap = new HashMap<>();
            for (Map.Entry<String, Integer> entity: figure.getEraName().entrySet())
            {
                boolean hasRelation = false;
                for (Era era: eraCollection.getData())
                {
                    if (era.getAllPossibleNames().contains(entity.getKey()))
                    {
                        newMap.put(entity.getKey(), era.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMap.put(entity.getKey(), entity.getValue());
                }
            }
            figure.setEraName(newMap);
            newChars.add(figure);
        }
        newCharCollection.setData(newChars);
    }

    public static void relate_Father_Mother_ToChar(HistoricalCharCollection charCollection, HistoricalCharCollection newCharCollection)
    {
        List<HistoricalCharacter> newChars = new ArrayList<>();
        for (HistoricalCharacter figure: charCollection.getData())
        {
            Map<String, Integer> newFatherMap1 = new HashMap<>();
            Map<String, Integer> newMotherMap2 = new HashMap<>();

            for (Map.Entry<String, Integer> father: figure.getFatherName().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter f: charCollection.getData())
                {
                    if (f.getAllPossibleNames().contains(father.getKey()))
                    {
                        newFatherMap1.put(father.getKey(), f.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newFatherMap1.put(father.getKey(), father.getValue());
                }
            }


            for (Map.Entry<String, Integer> mother: figure.getMotherName().entrySet())
            {
                boolean hasRelation = false;
                for (HistoricalCharacter f: charCollection.getData())
                {
                    if (f.getAllPossibleNames().contains(mother.getKey()))
                    {
                        newMotherMap2.put(mother.getKey(), f.getId());
                        hasRelation = true;
                        break;
                    }
                }

                if (!hasRelation)
                {
                    newMotherMap2.put(mother.getKey(), mother.getValue());
                }
            }
            figure.setFatherName(newFatherMap1);
            figure.setMotherName(newMotherMap2);
            newChars.add(figure);
        }
        newCharCollection.setData(newChars);
    }

    public static void main(String[] args) throws IOException
    {
        HistoricalCharCollection charCollection = new HistoricalCharCollection();
        charCollection.loadJsonFiles();

        EraCollection eraCollection = new EraCollection();
        eraCollection.loadJsonFiles();

        EraCollection newEraCollection = new EraCollection();
        RelationUtils.relateCharToEra(eraCollection, charCollection, newEraCollection);
        newEraCollection.toJsonFiles();

        EventCollection eventCollection = new EventCollection();
        eventCollection.loadJsonFiles();

        EventCollection newEventCollection = new EventCollection();
        RelationUtils.relateCharToEvent(eventCollection, charCollection, newEventCollection);
        newEventCollection.toJsonFiles();

        FestivalCollection festivalCollection = new FestivalCollection();
        festivalCollection.loadJsonFiles();

        FestivalCollection newFestivalCollection = new FestivalCollection();
        RelationUtils.relateCharToFestival(festivalCollection, charCollection, newFestivalCollection);
        newFestivalCollection.toJsonFiles();

        HistoricalSiteCollection siteCollection = new HistoricalSiteCollection();
        siteCollection.loadJsonFiles();

        HistoricalSiteCollection newSiteCollection = new HistoricalSiteCollection();
        RelationUtils.relateCharToHistorycalSite(siteCollection, charCollection, newSiteCollection);
        newSiteCollection.toJsonFiles();

        HistoricalCharCollection newCharCollection1 = new HistoricalCharCollection();
        RelationUtils.relateEraToChar(charCollection, eraCollection, newCharCollection1);
        newCharCollection1.toJsonFiles();

        HistoricalCharCollection newCharCollection2 = new HistoricalCharCollection();
        RelationUtils.relate_Father_Mother_ToChar(charCollection, newCharCollection2);
        newCharCollection2.toJsonFiles();

    }
}