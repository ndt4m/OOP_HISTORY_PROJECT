package application.controller;

import application.App;
import collection.EraCollection;
import entity.Era;
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

public class EraScreenController {

    private EraCollection eraCollection = new EraCollection();

    @FXML
    private TableView<Era> eraTable;

    @FXML
    private TableColumn<Era, Integer> colEraId;

    @FXML
    private TableColumn<Era, String> colEraName;

    @FXML
    private TableColumn<Era, String> colEraDate;

    @FXML
    private TableColumn<Era, String> colEraCapital;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {
        try {
            eraCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        colEraId.setCellValueFactory(
                new PropertyValueFactory<Era, Integer>("id")
        );
        colEraName.setCellValueFactory(
                new PropertyValueFactory<Era, String>("entityName")
        );
        colEraDate.setCellValueFactory(
                new PropertyValueFactory<Era, String>("time")
        );
        colEraCapital.setCellValueFactory(
                new PropertyValueFactory<Era, String>("capital")
        );
        eraTable.setItems(eraCollection.getData());

        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        eraTable.setItems(eraCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            eraTable.setItems(
                                    FXCollections.singletonObservableList(eraCollection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        eraTable.setItems(eraCollection.getData());
                    }
                }
        );

        // Tao listener khi click vao trieu dai trong table
        eraTable.setRowFactory(tableView -> {
            TableRow<Era> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Era era = row.getItem();
                    System.out.println("=============================");
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/EraDetailScreen.fxml"));
                        Parent root = loader.load();
                        EraDetailScreenController controller = loader.getController();
                        controller.setEra(era);
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setFullScreen(true);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }
}
