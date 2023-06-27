package application.controller;

import application.App;
import collection.HistoricalCharCollection;
import entity.Era;
import entity.HistoricalCharacter;
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

public class EraDetailScreenController {

    private HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
    
    @FXML
    public FlowPane relatedCharFlowPane;

    @FXML
    private Text nameText;

    @FXML
    private Text founderText;

    @FXML
    private Text capitalText;

    @FXML
    private Text timeText;

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text overviewText;

    @FXML
    private SidebarController sideBarController;

    private Era era;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        sideBarController.switchByGetFxml("/application/view/EraScreen.fxml", event);
    }

    public void setEra(Era era) {
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.era = era;
        nameText.setText(era.getEntityName());
        timeText.setText(era.getTime());
        founderText.setText(era.getFounder());
        capitalText.setText(era.getCapital());
        for (String alias : era.getAliases()) {
            Text aliasText = new Text(alias);
            aliasFlowPane.getChildren().add(aliasText);
        }
        overviewText.setText(era.getOverview());
        for(Map.Entry<String, Integer> entry : era.getRelatedCharacters().entrySet()){
            Text relatedCharText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                relatedCharText.setFill(Color.web("#3498db"));
                relatedCharText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.getData().get(entry.getValue());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/FigureDetailScreen.fxml"));
                        Parent root = loader.load();
                        FigureDetailScreenController controller = loader.getController();
                        controller.setFigure(figure);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });
            }
            relatedCharFlowPane.getChildren().add(relatedCharText);
        }
    }
}
