package application.controller;

import application.App;
import history.model.HistoricSite;
import history.collection.HistoricSites;
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
    private TableView<HistoricSite> siteTable;

    @FXML
    private TableColumn<HistoricSite, Integer> colSiteId;

    @FXML
    private TableColumn<HistoricSite, String> colSiteName;

    @FXML
    private TableColumn<HistoricSite, String> colSiteDate;

    @FXML
    private TableColumn<HistoricSite, String> colSiteLocate;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {

        colSiteId.setCellValueFactory(
                new PropertyValueFactory<HistoricSite, Integer>("id")
        );
        colSiteName.setCellValueFactory(
                new PropertyValueFactory<HistoricSite, String>("name")
        );
        colSiteDate.setCellValueFactory(
                new PropertyValueFactory<HistoricSite, String>("constructionDate")
        );
        colSiteLocate.setCellValueFactory(
                new PropertyValueFactory<HistoricSite, String>("location")
        );
        siteTable.setItems(HistoricSites.collection.getData());

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

        siteTable.setRowFactory(tableView -> {
            TableRow<HistoricSite> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    HistoricSite site = row.getItem();
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
