package newApp.controller;

import java.io.IOException;
import java.util.Map;

import collection.HistoricalCharCollection;
import entity.Festival;
import entity.HistoricalCharacter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FestivalDetailController extends PreviousStack{

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text dateText;

    @FXML
    private Text locationText;

    @FXML
    private Text nameText;

    @FXML
    private Text overviewText;

    @FXML
    private FlowPane relatedCharsFlowPane;
    
    @FXML
    private ScrollPane fesDetailRoot;
    
    @FXML
    private Button back;
    
    @FXML
    void backPressed(ActionEvent event) {
    	BorderPane parent = (BorderPane)fesDetailRoot.getParent();
    	Node preNode = previous.remove(previous.size() - 1);
    	parent.setCenter(preNode);
    }
    
    
    
    public void setFestival(Festival fes) {
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
            System.out.println(historicalCharCollection.getData().size());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        nameText.setText(fes.getEntityName());
        dateText.setText(fes.getTime());
        locationText.setText(fes.getLocation());
        overviewText.setText(fes.getOverview());

        if (!fes.getAliases().isEmpty()){
            for (String alias : fes.getAliases()) {
                Text aliasText = new Text(alias);
                aliasFlowPane.getChildren().add(aliasText);
            }
        }else{
            aliasFlowPane.getChildren().add(new Text("Không rõ"));
        }

        for (Map.Entry<String, Integer> entry : fes.getRelatedCharacters().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
                    try {
                    	previous.addAll(FXCollections.observableArrayList(fesDetailRoot));
                    	
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/CharacterDetail"));
                        ScrollPane root = loader.load();
                        CharacterDetailController controller = loader.getController();
                        controller.setFigure(figure);
                        
                        BorderPane parent = (BorderPane)fesDetailRoot.getParent();
                        parent.setCenter(root);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
            }
            relatedCharsFlowPane.getChildren().add(figureText);
        }
    }

}
