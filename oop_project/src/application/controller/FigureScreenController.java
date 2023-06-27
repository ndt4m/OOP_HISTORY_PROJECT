package application.controller;

import application.App;
import history.model.HistoricalFigure;
import history.collection.HistoricalFigures;
import history.relation.Pair;
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

public class FigureScreenController {

    @FXML
    private TableView<HistoricalFigure> tblFigure;

    @FXML
    private TableColumn<HistoricalFigure, Integer> colFigureId;

    @FXML
    private TableColumn<HistoricalFigure, String> colFigureName;

    @FXML
    private TableColumn<HistoricalFigure, Pair<String, Integer> > colFigureEra;

    @FXML
    private TableColumn<HistoricalFigure, String> colFigureOverview;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {

        colFigureId.setCellValueFactory(
                new PropertyValueFactory<HistoricalFigure, Integer>("id"));
        colFigureName.setCellValueFactory(
                new PropertyValueFactory<HistoricalFigure, String>("name"));
        colFigureEra.setCellValueFactory(
                new PropertyValueFactory<HistoricalFigure, Pair<String, Integer> >("era"));
        colFigureOverview.setCellValueFactory(
                new PropertyValueFactory<HistoricalFigure, String>("overview"));
        tblFigure.setItems(HistoricalFigures.collection.getData());

        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        tblFigure.setItems(HistoricalFigures.collection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            tblFigure.setItems(
                                    FXCollections.singletonObservableList(HistoricalFigures.collection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        tblFigure.setItems(HistoricalFigures.collection.getData());
                    }
                }
        );

        tblFigure.setRowFactory(tableView -> {
            TableRow<HistoricalFigure> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    HistoricalFigure figure = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/FigureDetailScreen.fxml"));
                        Parent root = loader.load();
                        FigureDetailScreenController controller = loader.getController();
                        controller.setFigure(figure);
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