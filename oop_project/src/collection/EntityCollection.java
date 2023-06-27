package collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

import entity.Entity;

public abstract class EntityCollection <T extends Entity>
{
    protected ObservableList<T> data = FXCollections.observableArrayList();

    public void setData(Collection<T> data){
        this.data = FXCollections.observableArrayList(data);
    }

    public ObservableList<T> getData() {
        return data;
    }
    
    public void numberId()
    {
        int i = 1;
        for(Entity e: this.data)
        {
            e.setId(i);
            //System.out.println(i);
            i++;
        }

    }

    public T get(Integer id)
    {
        if (id == null)
        {
            return null;
        }

        for (T entity : this.data)
        {
            if (entity.getId() == id) 
            {
                return entity;
            }
        }
        return null;
    }

}
