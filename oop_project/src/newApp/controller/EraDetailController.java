package newApp.controller;

import java.io.IOException;
import java.util.Map;

import collection.HistoricalCharCollection;
import entity.Era;
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

public class EraDetailController extends PreviousStack{

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text capitalText;

    @FXML
    private Text founderText;

    @FXML
    private Text nameText;

    @FXML
    private Text overviewText;

    @FXML
    private FlowPane relatedCharFlowPane;

    @FXML
    private Text timeText;
    
    @FXML
    private ScrollPane EraDetailRoot;
    
    @FXML
    private Button back;
    
    @FXML
    void backPressed(ActionEvent event) {
    	BorderPane parent = (BorderPane) EraDetailRoot.getParent();
		Node preNode = previous.remove(previous.size() - 1);
		parent.setCenter(preNode);
    }
    
    public void setEra(Era era) {
    	HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nameText.setText(era.getEntityName());
        timeText.setText(era.getTime());
        founderText.setText(era.getFounder());
        capitalText.setText(era.getCapital());
        
        if (!era.getAliases().isEmpty()){
            for (String alias : era.getAliases()) {
                Text aliasText = new Text(alias);
                aliasFlowPane.getChildren().add(aliasText);
            }
        }else{
            aliasFlowPane.getChildren().add(new Text("Không rõ"));
        }

        overviewText.setText(era.getOverview());
        for(Map.Entry<String, Integer> entry : era.getRelatedCharacters().entrySet()){
            Text relatedCharText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                relatedCharText.setFill(Color.web("#3498db"));
                relatedCharText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
                    try {
                    	previous.addAll(FXCollections.observableArrayList(EraDetailRoot));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/CharacterDetail.fxml"));
                        ScrollPane root = loader.load();
                        CharacterDetailController controller = loader.getController();
                        controller.setFigure(figure);
                 
                        BorderPane parent = (BorderPane) EraDetailRoot.getParent();
                        parent.setCenter(root);
                       

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
            }
            relatedCharFlowPane.getChildren().add(relatedCharText);
        }
    }

}
