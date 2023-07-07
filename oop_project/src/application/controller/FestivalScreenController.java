package application.controller;

import application.App;
import entity.Festival;
import collection.FestivalCollection;
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

public class FestivalScreenController {
    @FXML
    private TableView<Festival> fesTable;

    @FXML
    private TableColumn<Festival, Integer> colFesId;

    @FXML
    private TableColumn<Festival, String> colFesName;

    @FXML
    private TableColumn<Festival, String> colFesDate;

    @FXML
    private TableColumn<Festival, String> colFesLocate;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {
        FestivalCollection festivalCollection = new FestivalCollection();
        try{
            festivalCollection.loadJsonFiles();
        } catch (IOException e){
            e.printStackTrace();
        }

        colFesId.setCellValueFactory(
                new PropertyValueFactory<Festival, Integer>("id")
        );
        colFesName.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("entityName")
        );
        colFesDate.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("time")
        );
        colFesLocate.setCellValueFactory(
                new PropertyValueFactory<Festival, String>("location")
        );

        fesTable.setItems(festivalCollection.getData());

        
        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        fesTable.setItems(festivalCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            fesTable.setItems(
                                    FXCollections.singletonObservableList(festivalCollection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        fesTable.setItems(festivalCollection.getData());
                    }
                }
        );
        
        fesTable.setRowFactory(tableView -> {
            TableRow<Festival> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    Festival fes = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/FesDetailScreen.fxml"));
                        Parent root = loader.load();
                        FesDetailScreenController controller = loader.getController();
                        controller.setFestival(fes);
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(false);
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
