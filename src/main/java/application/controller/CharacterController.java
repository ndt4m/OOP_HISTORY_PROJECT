package application.controller;

import java.io.IOException;
import collection.HistoricalCharCollection;
import entity.HistoricalCharacter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class CharacterController extends PreviousStack{

    @FXML
    private TableColumn<HistoricalCharacter, Integer> colFigureId;

    @FXML
    private TableColumn<HistoricalCharacter, String> colFigureName;

    @FXML
    private TableColumn<HistoricalCharacter, String> colFigureOverview;

    @FXML
    private TableView<HistoricalCharacter> tblFigure;

    @FXML
    private SearchBarController searchBarController;

    @FXML
    private BorderPane charRoot;

    @FXML
    private BorderPane charNode1;

    @FXML
    void initialize() {

        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        colFigureId.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, Integer>("id"));
        colFigureName.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, String>("entityName"));
        colFigureOverview.setCellValueFactory(
                new PropertyValueFactory<HistoricalCharacter, String>("overview"));

        tblFigure.setItems(historicalCharCollection.getData());
        tblFigure.getSortOrder().addAll(colFigureId);

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
                            HistoricalCharacter character = historicalCharCollection.get(intId);
                            if (character != null) {
                                tblFigure.setItems(FXCollections.singletonObservableList(character));
                            } else {
                                System.err.println("Cannot find the entity with the ID " + id);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid ID format: " + id);
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
                        previous.addAll(FXCollections.observableArrayList(charRoot.getChildren()));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CharacterDetail.fxml"));
                        ScrollPane root = loader.load();
                        CharacterDetailController controller = loader.getController();
                        controller.setFigure(figure);
                        charRoot.setCenter(root);

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }


}