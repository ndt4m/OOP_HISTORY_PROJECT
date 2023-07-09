package collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

import entity.Entity;
import javafx.collections.transformation.FilteredList;

public abstract class EntityCollection <T extends Entity> implements JsonFilesHandling
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
            i++;
        }

    }

    /**
     * tìm kiếm theo id của dữ liệu
     * @param id id cần tìm kiếm
     */
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

    /**
     *
     * @param name kí tự cần tìm kiếm
     * @return danh sách các dữ liệu có chứa những kí tự tìm kiếm
     */
    public FilteredList<T> searchByName(String name){
        return new FilteredList<>(data, entity -> entity.searchName(name));
    }
}