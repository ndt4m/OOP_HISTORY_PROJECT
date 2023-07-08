package newApp.controller;

import java.io.IOException;

import application.App;
import application.controller.SearchBarController;
import application.controller.SearchBoxListener;
import application.controller.SiteDetailScreenController;
import collection.EventCollection;
import collection.FestivalCollection;
import collection.HistoricalSiteCollection;
import entity.Event;
import entity.Festival;
import entity.HistoricalSite;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FestivalController {

    @FXML
    private TableColumn<Festival, Integer> colFesId;

    @FXML
    private TableColumn<Festival, String> colFesLocate;

    @FXML
    private TableColumn<Festival, String> colFesName;
    
    @FXML
    private TableColumn<Festival, String> colFesDate;
    @FXML
    private TableView<Festival> fesTable;
    
    @FXML
    private searchBarController searchBarController;
    
    @FXML
    private BorderPane fesRoot;
    
    @FXML
    private BorderPane fesNode1;
    

    
    
    @FXML
    void initialize() {
        FestivalCollection festivalCollection = new FestivalCollection();
        try {
            festivalCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        colFesId.setCellValueFactory(
                new PropertyValueFactory<Festival, Integer>("id")
        );
        colFesName.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("entityName")
        );
        colFesLocate.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("location")
        );
        colFesDate.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("time")
        );
        fesTable.setItems(festivalCollection.getData());
        
        searchBarController.setSearchBoxListener(
                new searchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        fesTable.setItems(festivalCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            Festival fes = festivalCollection.get(intId);
                            if(fes != null) {
                            	fesTable.setItems(FXCollections.singletonObservableList(fes)) ;                           	
                            }else {
                            	System.err.println("Cannot find the entity with the ID " + id);
                            }
                        } catch (NumberFormatException e){
                            System.err.println("Invalid ID format " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        fesTable.setItems(festivalCollection.getData());
                    }
                }
        );
//doan code nay de di vao chi tiet
        fesTable.setRowFactory(tableView -> {
            TableRow<Festival> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Festival fes = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/FestivalDetail.fxml"));
                        ScrollPane root = loader.load();
                        FestivalDetailController controller = loader.getController();
                        controller.setFestival(fes);
                        fesRoot.setCenter(root);
                        
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

}