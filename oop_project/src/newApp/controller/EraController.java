package newApp.controller;

import java.io.IOException;

import collection.EraCollection;
import entity.Era;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class EraController extends PreviousStack{

    @FXML
    private TableColumn<Era, String> colEraCapital;

    @FXML
    private TableColumn<Era, String> colEraDate;

    @FXML
    private TableColumn<Era, Integer> colEraId;

    @FXML
    private TableColumn<Era, String> colEraName;

    @FXML
    private TableView<Era> eraTable;
    
    @FXML
    private searchBarController searchBarController;
    
    @FXML
    private BorderPane eraRoot;
    
    @FXML
    private BorderPane eraNode1;
    

    
    @FXML
    void initialize() {
    	EraCollection eraCollection = new EraCollection();
    	
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
        colEraCapital.setCellValueFactory(
                new PropertyValueFactory<Era, String>("capital")
        );
        colEraDate.setCellValueFactory(
        		new PropertyValueFactory<Era, String>("time"));
        
        eraTable.setItems(eraCollection.getData());
        
        searchBarController.setSearchBoxListener(
                new searchBoxListener() {
                    @Override
                    public void handleSearchName(String name) {
                        eraTable.setItems(eraCollection.searchByName(name));
                    }

                    @Override
                    public void handleSearchId(String id) {
                        try {
                            int intId = Integer.parseInt(id);
                            Era era = eraCollection.get(intId);
                            if(era != null) {
                            	eraTable.setItems(FXCollections.singletonObservableList(era)) ;                           	
                            }else {
                            	System.err.println("Cannot find the entity with the ID " + id);
                            }
                        } catch (NumberFormatException e){
                            System.err.println("Invalid ID format " + id);
                        }
                    }

                    @Override
                    public void handleBlank() {
                        eraTable.setItems(eraCollection.getData());
                    }
                }
        );
        
        eraTable.setRowFactory(tableView -> {
            TableRow<Era> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Era era = row.getItem();
                    try {
                    	previous.addAll(FXCollections.observableArrayList(eraRoot.getChildren()));
                    	
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/EraDetail.fxml"));
                        ScrollPane root = loader.load();
                        EraDetailController controller = loader.getController();
                        controller.setEra(era);
                        eraRoot.setCenter(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        
    }
}
