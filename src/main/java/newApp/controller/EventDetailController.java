package newApp.controller;

import java.io.IOException;
import java.util.Map;

import application.App;
import application.controller.FigureDetailScreenController;
import collection.HistoricalCharCollection;
import entity.Event;
import entity.HistoricalCharacter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EventDetailController {

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text locationText;

    @FXML
    private Text nameText;

    @FXML
    private Text overviewText;

    @FXML
    private FlowPane relatedCharsFlowPane;

    @FXML
    private Text resultText;

    @FXML
    private Text timeText;

    @FXML
    private ScrollPane eventDetailRoot;

    @FXML
    private searchBarController searchBarController;


    public void setEvent(Event event) {
        nameText.setText(event.getEntityName());
        timeText.setText(event.getTime());
        locationText.setText(event.getLocation());
        overviewText.setText(event.getOverview());
        resultText.setText(event.getResult());

        if (!event.getAliases().isEmpty()){
            for (String alias : event.getAliases()) {
                Text aliasText = new Text(alias);
                aliasFlowPane.getChildren().add(aliasText);
            }
        }else{
            aliasFlowPane.getChildren().add(new Text("Không rõ"));
        }

        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for(Map.Entry<String, Integer> entry : event.getRelatedCharacters().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null){
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/CharacterDetail.fxml"));
                        ScrollPane root = loader.load();
                        CharacterDetailController controller = loader.getController();
                        controller.setFigure(figure);

                        BorderPane parent = (BorderPane)eventDetailRoot.getParent();
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