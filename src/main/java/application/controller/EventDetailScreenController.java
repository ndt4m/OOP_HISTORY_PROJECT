package application.controller;

import application.App;
import entity.Event;
import entity.HistoricalCharacter;
import collection.HistoricalCharCollection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class EventDetailScreenController {
    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text nameText;

    @FXML
    private Text timeText;

    @FXML
    private Text locationText;

    @FXML
    private Text overviewText;

    @FXML
    private Text resultText;

    @FXML
    private FlowPane relatedCharsFlowPane;

    @FXML
    private TopBarController topBarController = new TopBarController();

    private Event event;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        topBarController.switchByGetFxml("/application/EventScreen.fxml", event);
    }

    public void setEvent(Event event) {
        this.event = event;
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
            e.printStackTrace();
        }

        for(Map.Entry<String, Integer> entry : event.getRelatedCharacters().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null){
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/FigureDetailScreen.fxml"));
                        Parent root = loader.load();
                        FigureDetailScreenController controller = loader.getController();
                        controller.setFigure(figure);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
            }
            relatedCharsFlowPane.getChildren().add(figureText);
        }
    }
}