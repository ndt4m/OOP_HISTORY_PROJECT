package gui;

import java.io.IOException;

import collection.EraCollection;
import entity.Era;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class eraScreenController
{
    private EraCollection eraCollection = new EraCollection();

    @FXML
    private TableColumn<Era, String> colCapital;

    @FXML
    private TableColumn<Era, String> colName;

    @FXML
    private TableColumn<Era, String> colTime;

    @FXML
    private TableView<Era> tblEra;


    @FXML
    private void initialize()
    {
        try {
            eraCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        colName.setCellValueFactory(new PropertyValueFactory<Era, String>("entityName"));
        colTime.setCellValueFactory(new PropertyValueFactory<Era, String>("time"));
        colCapital.setCellValueFactory(new PropertyValueFactory<Era, String>("capital"));
        tblEra.setItems(this.eraCollection.getData());
    }
}