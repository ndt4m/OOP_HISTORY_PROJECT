package application.controller;

import application.App;
import entity.HistoricalSite;
import collection.HistoricalSiteCollection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class SiteScreenController {
    @FXML
    private TableView<HistoricalSite> siteTable;

    @FXML
    private TableColumn<HistoricalSite, Integer> colSiteId;

    @FXML
    private TableColumn<HistoricalSite, String> colSiteName;

    @FXML
    private TableColumn<HistoricalSite, String> colSiteDate;

    @FXML
    private TableColumn<HistoricalSite, String> colSiteLocate;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {
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
        
        /*
        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        siteTable.setItems(HistoricSites.collection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            siteTable.setItems(
                                    FXCollections.singletonObservableList(HistoricSites.collection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        siteTable.setItems(HistoricSites.collection.getData());
                    }
                }
        );
        */

        siteTable.setRowFactory(tableView -> {
            TableRow<HistoricalSite> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    HistoricalSite site = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/SiteDetailScreen.fxml"));
                        Parent root = loader.load();
                        SiteDetailScreenController controller = loader.getController();
                        controller.setHistoricSite(site);
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }
}
