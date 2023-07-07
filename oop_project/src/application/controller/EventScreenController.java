package application.controller;

import application.App;
import entity.Event;
import collection.EventCollection;
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

public class EventScreenController {
    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, Integer> colEventId;

    @FXML
    private TableColumn<Event, String> colEventName;

    @FXML
    private TableColumn<Event, String> colEventDate;

    @FXML
    private TableColumn<Event, String> colEventLocate;

    @FXML
    private SearchBarController searchBarController;


    @FXML
    public void initialize() {
        EventCollection eventCollection = new EventCollection();
        try {
            eventCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        colEventId.setCellValueFactory(
                new PropertyValueFactory<Event, Integer>("id")
        );
        colEventName.setCellValueFactory(
                new PropertyValueFactory<Event, String>("entityName")
        );
        colEventDate.setCellValueFactory(
                new PropertyValueFactory<Event, String>("time")
        );
        colEventLocate.setCellValueFactory(
                new PropertyValueFactory<Event, String>("location")
        );

        eventTable.setItems(eventCollection.getData());

        
        searchBarController.setSearchBoxListener(
                new SearchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        eventTable.setItems(eventCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            eventTable.setItems(
                                    FXCollections.singletonObservableList(eventCollection.get(intId))
                            );
                        } catch (Exception e){
                            System.err.println("Cannot find the entity with the id " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        eventTable.setItems(eventCollection.getData());
                    }
                }
        );
        
        
        eventTable.setRowFactory(tableView -> {
            TableRow<Event> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount() == 2 && (!row.isEmpty())){
                    Event event = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/EventDetailScreen.fxml"));
                        Parent root = loader.load();
                        EventDetailScreenController controller = loader.getController();
                        controller.setEvent(event);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
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
