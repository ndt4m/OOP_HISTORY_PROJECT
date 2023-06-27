package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.FileSystems;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JsonUtils 
{
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static final String PREFIX_URL = FileSystems.getDefault().getPath("./").normalize().toAbsolutePath() + "\\src\\json\\";

    public static void toJsonFile(String fileName, Object object)
    {
        try {
            FileWriter fileWriter = new FileWriter(PREFIX_URL + fileName);
            GSON.toJson(object, fileWriter);
            fileWriter.close();
            System.out.println("[+] Object has saved successfully into " + PREFIX_URL + fileName);

        } catch (IOException e){
            System.err.println("[-] Error in saving a json file.");
        }  
    }

    public static <T> T fromJsonFile(String filePath, Class<T> typeClass) {
        try {
            FileReader fileReader = new FileReader(filePath);
            T entity = GSON.fromJson(fileReader, typeClass);
            fileReader.close();
            System.out.println("[+] Object has been loaded successfully from " + filePath);
    
            return entity;
        } catch (IOException e) {
            System.err.println("[-] Error in reading the json file.");
            return null;
        }
    }
    
    
    public static void main(String[] args)
    {   
        Example epl1 = new Example();
        epl1.id = 1111;
        epl1.name = "tam";
        JsonUtils.toJsonFile("1.json", epl1);
        // Example employee = fromJsonFile(PREFIX_URL + "\\1.json", Example.class);
        // System.out.println(PREFIX_URL);
        
    }
}


class Example
{
    public int id;
    public String name;
    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}