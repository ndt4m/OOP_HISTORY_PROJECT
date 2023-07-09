package newApp.controller;

import java.io.IOException;

import collection.EventCollection;
import entity.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EventController extends PreviousStack{

    @FXML
    private TableColumn<Event, Integer> colEventId;

    @FXML
    private TableColumn<Event, String> colEventLocate;

    @FXML
    private TableColumn<Event, String> colEventName;
    
    @FXML
    private TableColumn<Event, String> colEventDate;
    @FXML
    private TableView<Event> eventTable;
    
    @FXML
    private searchBarController searchBarController;
    
    @FXML
    private BorderPane eventRoot;
    
    @FXML
    private BorderPane eventNode1;
    

    
    @FXML
    void initialize() {
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
        colEventLocate.setCellValueFactory(
                new PropertyValueFactory<Event, String>("location")
        );
        colEventDate.setCellValueFactory(
                new PropertyValueFactory<Event, String>("time")
        );
        eventTable.setItems(eventCollection.getData());
        
        searchBarController.setSearchBoxListener(
                new searchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        eventTable.setItems(eventCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            Event event = eventCollection.get(intId);
                            if(event != null) {
                            	eventTable.setItems(FXCollections.singletonObservableList(event)) ;                           	
                            }else {
                            	System.err.println("Cannot find the entity with the ID " + id);
                            }
                        } catch (NumberFormatException e){
                            System.err.println("Invalid ID format " + id);
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
                    	previous.addAll(FXCollections.observableArrayList(eventRoot.getChildren()));
                    	
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/EventDetail.fxml"));
                        ScrollPane root = loader.load();
                        EventDetailController controller = loader.getController();
                        controller.setEvent(event);
                        eventRoot.setCenter(root);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

}