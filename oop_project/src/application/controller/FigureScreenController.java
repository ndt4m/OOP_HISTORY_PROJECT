package application.controller;

import application.App;
import entity.HistoricalCharacter;
import collection.HistoricalCharCollection;
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
import javafx.util.Pair;

import java.io.IOException;
import java.util.Map;

public class FigureScreenController {

    @FXML
    private TableView<HistoricalCharacter> tblFigure;

    @FXML
    private TableColumn<HistoricalCharacter, Integer> colFigureId;

    @FXML
    private TableColumn<HistoricalCharacter, String> colFigureName;

    @FXML
    private TableColumn<HistoricalCharacter, Map<String, Integer> > colFigureEra;

    @FXML
    private TableColumn<HistoricalCharacter, String> colFigureOverview;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    public void initialize() {
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        colFigureId.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, Integer>("id"));
        colFigureName.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, String>("entityName"));
        colFigureEra.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, Map<String, Integer> >("eraName"));
        colFigureOverview.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, String>("overview"));

        tblFigure.setItems(historicalCharCollection.getData());

        
        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        tblFigure.setItems(historicalCharCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            tblFigure.setItems(
                                    FXCollections.singletonObservableList(historicalCharCollection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        tblFigure.setItems(historicalCharCollection.getData());
                    }
                }
        );
        

        tblFigure.setRowFactory(tableView -> {
            TableRow<HistoricalCharacter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    HistoricalCharacter figure = row.getItem();
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