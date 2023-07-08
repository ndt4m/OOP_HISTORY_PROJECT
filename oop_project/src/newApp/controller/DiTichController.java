package newApp.controller;

import java.io.IOException;

import application.App;
import collection.HistoricalSiteCollection;
import entity.HistoricalSite;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class DiTichController {

    @FXML
    private TableColumn<HistoricalSite, Integer> colSiteId;

    @FXML
    private TableColumn<HistoricalSite, String> colSiteLocate;

    @FXML
    private TableColumn<HistoricalSite, String> colSiteName;

    @FXML
    private TableView<HistoricalSite> siteTable;
    
    @FXML
    private searchBarController searchBarController;
    
    @FXML
    private BorderPane siteRoot;
    
    @FXML 
    private BorderPane siteNode1;
    

    
    
    @FXML
    void initialize() {
        HistoricalSiteCollection historicalSiteCollection = new HistoricalSiteCollection();
        try {
            historicalSiteCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        colSiteId.setCellValueFactory(
                new PropertyValueFactory<HistoricalSite, Integer>("id")
        );
        colSiteName.setCellValueFactory(
                new PropertyValueFactory<HistoricalSite, String>("entityName")
        );
        colSiteLocate.setCellValueFactory(
                new PropertyValueFactory<HistoricalSite, String>("location")
        );
        siteTable.setItems(historicalSiteCollection.getData());
        
        searchBarController.setSearchBoxListener(
                new searchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        siteTable.setItems(historicalSiteCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            HistoricalSite site = historicalSiteCollection.get(intId);
                            if(site != null) {
                            	siteTable.setItems(FXCollections.singletonObservableList(site)) ;                           	
                            }else {
                            	System.err.println("Cannot find the entity with the ID " + id);
                            }
                        } catch (NumberFormatException e){
                            System.err.println("Invalid ID format " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        siteTable.setItems(historicalSiteCollection.getData());
                    }
                }
        );
//doan code nay de di vao chi tiet
        
        siteTable.setRowFactory(tableView -> {
            TableRow<HistoricalSite> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    HistoricalSite site = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/newApp/fxml/DiTichDetail.fxml"));
                        ScrollPane root = loader.load();
                        DiTichDetailController controller = loader.getController();
                        controller.setHistoricSite(site);
                        siteRoot.setCenter(root);
                        
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }

}