package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import collection.EntityCollection;
import collection.EraCollection;
import collection.FestivalCollection;
import collection.HistoricalCharCollection;
import collection.HistoricalSiteCollection;
import crawling.era.CrawlEraFromVS;
import crawling.era.CrawlEraFromWiki;
import crawling.event.CrawlEventFromNKS;
import crawling.festival.CrawlFestivalFromCoutureTravelCompanyLink;
import crawling.festival.CrawlFestivalFromWikiLink;
import crawling.historicalsite.CrawlHistoricalSiteFromFirstWikiLink;
import crawling.historicalsite.CrawlHistoricalSiteFromNKS;
import crawling.historicalsite.CrawlHistoricalSiteFromSecondWikiLink;
import crawling.historycalChar.CrawHistorycalCharFromVS;
import crawling.historycalChar.CrawlHistoricalCharFromNKS;
import crawling.historycalChar.CrawlHistoricalCharFromWikiLink;
import entity.Era;
import entity.Festival;
import entity.HistoricalCharacter;
import entity.HistoricalSite;
import entity.Entity;

public class MergingUtils 
{
    public static <T> void mergeObjects(T obj1, T obj2, T mergedObject) 
    {
        Class<?> objClass = obj1.getClass();
        try {
             // Create a new instance of the same type
    
            while (objClass != null) 
            {
                Field[] fields = objClass.getDeclaredFields();
                for (Field field : fields) 
                {
                    field.setAccessible(true); // Set accessible to true if the field is private
    
                    Object value1 = field.get(obj1);
                    Object value2 = field.get(obj2);

                    if (value1 instanceof String)
                    {   String stringValue1 = (String) value1;
                        String stringValue2 = (String) value2;

                        if (field.getName().equals("entityName"))
                        {
                            if (stringValue1.length() > stringValue2.length())
                            {
                                field.set(mergedObject, value1);
                            }
                            else
                            {
                                field.set(mergedObject, value2);
                            }

                            continue;
                        }

                        if (stringValue1.equalsIgnoreCase("Không rõ"))
                        {
                            field.set(mergedObject, value2);
                        }
                        else if (stringValue1.equalsIgnoreCase(stringValue2)) 
                        {
                            field.set(mergedObject, value2);
                        } 
                        else 
                        {
                            String mergedValue = value1 + "\n" + "----------------------------------------------" + "\n" + value2;
                            field.set(mergedObject, mergedValue);
                        }
                    }
                    else if (value1 instanceof Set && 
                            (!((Set<?>) value1).isEmpty() && 
                            ((Set<?>) value1).iterator().next() instanceof String || 
                            !((Set<?>) value2).isEmpty() && 
                            ((Set<?>) value2).iterator().next() instanceof String))
                    {
                        Set<Object> mergeSet = new HashSet<>();
                        for (Object entry1: (Set<?>) value1)
                        {
                            mergeSet.add(entry1);
                        }
                        
                        for (Object entry2: (Set<?>) value2)
                        {
                            mergeSet.add(entry2);
                        }

                        field.set(mergedObject, mergeSet);
                    }
                    else if (value1 instanceof Map && 
                            (!((Map<?, ?>) value1).isEmpty() && 
                            ((Map<?, ?>) value1).keySet().iterator().next() instanceof String  && 
                            (((Map<?, ?>) value1).values().iterator().next() instanceof Integer || 
                            ((Map<?, ?>) value1).values().iterator().next() == null) || 
                            !((Map<?, ?>) value2).isEmpty() && 
                            ((Map<?, ?>) value2).keySet().iterator().next() instanceof String && 
                            (((Map<?, ?>) value2).values().iterator().next() instanceof Integer || 
                            ((Map<?, ?>) value2).values().iterator().next() == null)))
                    {
                        Map<Object, Object> mergeMap = new HashMap<>();
                        mergeMap.putAll((Map<?, ?>) value1);
                        mergeMap.putAll((Map<?, ?>) value2);

                        field.set(mergedObject, mergeMap);
                    }
                    
                }
    
                objClass = objClass.getSuperclass();
            }
    
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
     // Return null if merging fails
    }

    // public static <T extends Entity> void merge(EntityCollection<? extends T> collection1, EntityCollection<? extends T> collection2, EntityCollection<T> mergedEntityCollection, Class<? extends T> clazz)
    // {   
    //     Set<T> mergedEntitySet = new HashSet<>();
    //     Set<T> commonEntitySet1 = new HashSet<>();
    //     Set<T> commonEntitySet2 = new HashSet<>();
    //     for (Entity entity1: collection1.getData())
    //     {
    //         T newEntity1 = clazz.cast(entity1);
    //         for (Entity entity2: collection2.getData())
    //         {
    //             T newEntity2 = clazz.cast(entity2);
    //             if (entity2.isOverlap(entity1))
    //             {   
    //                 System.out.println("entity1" + newEntity1.getEntityName());
    //                 System.out.println("entity2" + newEntity2.getEntityName());
    //                 try{
    //                     T mergeEntity = clazz.getDeclaredConstructor().newInstance();

    //                     mergeObjects(newEntity1, newEntity2, mergeEntity);
    //                     mergedEntitySet.add(mergeEntity);
    //                     commonEntitySet1.add(newEntity1);
    //                     commonEntitySet2.add(newEntity2);
    //                     // mergedEntitySet.remove(newEntity1);
    //                     // mergedEntitySet.remove(newEntity2);
    //                 }
    //                 catch(IllegalAccessException e)
    //                 {
    //                     e.printStackTrace();
    //                 }
    //                 catch(NoSuchMethodException e)
    //                 {
    //                     e.printStackTrace();
    //                 }
    //                 catch(InstantiationException e)
    //                 {
    //                     e.printStackTrace();
    //                 }
    //                 catch(InvocationTargetException e)
    //                 {
    //                     e.printStackTrace();
    //                 }
    //             }
    //             else
    //             {
    //                 mergedEntitySet.add(newEntity1);
    //                 mergedEntitySet.add(newEntity2);
    //             }
    //         }
    //     }
    //     mergedEntitySet.removeAll(commonEntitySet1);
    //     mergedEntitySet.removeAll(commonEntitySet2);
    //     //System.out.println(mergedEntitySet.size());
    //     mergedEntityCollection.setData(mergedEntitySet);

    // }

    public static <T extends Entity> void merge(EntityCollection<? extends T> collection1, EntityCollection<? extends T> collection2, EntityCollection<T> mergedEntityCollection, Class<? extends T> clazz)
    {
        Set<T> mergedEntitySet = new HashSet<>();
        for (Entity entity1: collection1.getData())
        {
            boolean isOverlap = false;
            T newEntity1 = clazz.cast(entity1);
            for (Entity entity2: collection2.getData())
            {
                T newEntity2 = clazz.cast(entity2);
                if (entity1.isOverlap(entity2))
                {
                    isOverlap = true;
                    try
                    {
                        T mergeEntity = clazz.getDeclaredConstructor().newInstance();
                    
                        mergeObjects(newEntity1, newEntity2, mergeEntity);
                        mergedEntitySet.add(mergeEntity);
                        break;
                                    
                    }
                    catch(IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                    catch(NoSuchMethodException e)
                    {
                        e.printStackTrace();
                    }
                    catch(InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch(InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            if(!isOverlap)
            {
                mergedEntitySet.add(newEntity1);
            }

        }

        for (Entity entity2: collection2.getData())
        {
            boolean isOverlap = false;
            T newEntity2 = clazz.cast(entity2);
            for (Entity mergedEntity: mergedEntitySet)
            {
                if (entity2.isOverlap(mergedEntity))
                {
                    isOverlap = true;
                    break;
                }
            }

            if (!isOverlap)
            {
                mergedEntitySet.add(newEntity2);
            }
        }

        mergedEntityCollection.setData(mergedEntitySet);
    }


    public static void main(String[] args) throws IOException
    {
        CrawlEraFromVS crawlerVS = new CrawlEraFromVS();
        CrawlEraFromWiki crawlerWiki = new CrawlEraFromWiki();
        crawlerVS.crawlEra();
        crawlerWiki.crawlEra();
        EntityCollection<Era> mergedEntityCollection = new EraCollection();
        MergingUtils.merge(crawlerVS.getEraCollection(), crawlerWiki.getEraCollection(), mergedEntityCollection, Era.class);
        mergedEntityCollection.numberId();
        ((EraCollection) mergedEntityCollection).toJsonFiles();


        CrawlEventFromNKS crawlerNKS = new CrawlEventFromNKS();
        crawlerNKS.crawlEvent();
        crawlerNKS.getEventCollection().numberId();
        crawlerNKS.getEventCollection().toJsonFiles();

        CrawlFestivalFromWikiLink crawlerWiki2 = new CrawlFestivalFromWikiLink();
        CrawlFestivalFromCoutureTravelCompanyLink crawlerCoutureTravelCompany = new CrawlFestivalFromCoutureTravelCompanyLink();
        crawlerWiki2.crawlFestival();
        crawlerCoutureTravelCompany.crawlFestival();
        EntityCollection<Festival> mergedEntityCollection1 = new FestivalCollection();
        MergingUtils.merge(crawlerWiki2.getFestivalCollection(), crawlerCoutureTravelCompany.getFestivalCollection(), mergedEntityCollection1, Festival.class);
        mergedEntityCollection1.numberId();
        ((FestivalCollection) mergedEntityCollection1).toJsonFiles();

        CrawlHistoricalSiteFromFirstWikiLink crawler1 = new CrawlHistoricalSiteFromFirstWikiLink();
        CrawlHistoricalSiteFromSecondWikiLink crawler2 = new CrawlHistoricalSiteFromSecondWikiLink();
        CrawlHistoricalSiteFromNKS crawler3 = new CrawlHistoricalSiteFromNKS();
        crawler1.crawlHistoricalSite();
        crawler2.crawlHistoricalSite();
        crawler3.crawlHistoricalSite();
        EntityCollection<HistoricalSite> mergedEntityCollection2 = new HistoricalSiteCollection();
        MergingUtils.merge(crawler1.getHistoricalSiteCollection(), crawler2.getHistoricalSiteCollection(), mergedEntityCollection2, HistoricalSite.class);
        EntityCollection<HistoricalSite> mergedEntityCollection3 = new HistoricalSiteCollection();
        MergingUtils.merge(crawler3.getHistoricalSiteCollection(), mergedEntityCollection2, mergedEntityCollection3, HistoricalSite.class);
        mergedEntityCollection3.numberId();
        ((HistoricalSiteCollection) mergedEntityCollection3).toJsonFiles();

        System.out.println("mergedEntityCollection2 size: " + mergedEntityCollection2.getData().size());
        System.out.println("mergedEntityCollection3 size: " + mergedEntityCollection3.getData().size());


        CrawlHistoricalCharFromNKS crawler1NKS = new CrawlHistoricalCharFromNKS();
        CrawHistorycalCharFromVS crawler2VS = new CrawHistorycalCharFromVS();
        CrawlHistoricalCharFromWikiLink crawlerWikiLink = new CrawlHistoricalCharFromWikiLink();
        System.out.println("Running......NKS.");
        crawler1NKS.crawlHistoricalChar();
        System.out.println("Running......VS.");
        crawler2VS.crawlHistoricalChar();

        EntityCollection<HistoricalCharacter> mergedEntityCollection4 = new HistoricalCharCollection();
        MergingUtils.merge(crawler1NKS.getHistoricalCharCollection(), crawler2VS.getHistoricalCharCollection(), mergedEntityCollection4, HistoricalCharacter.class);
        
        System.out.println("Running......Wiki.");
        crawlerWikiLink.crawlHistoricalChar();
        EntityCollection<HistoricalCharacter> mergedEntityCollection5 = new HistoricalCharCollection();
        MergingUtils.merge(mergedEntityCollection4, crawlerWikiLink.getHistoricalCharCollection(), mergedEntityCollection5, HistoricalCharacter.class);
        mergedEntityCollection5.numberId();
        ((HistoricalCharCollection) mergedEntityCollection5).toJsonFiles();
        
    }
}
