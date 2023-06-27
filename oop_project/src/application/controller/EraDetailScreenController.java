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
import java.util.concurrent.Flow;

public class EraDetailScreenController {

    @FXML
    public FlowPane kingsFlowPane;

    @FXML
    private Text nameText;

    @FXML
    private Text timeStampText;

    @FXML
    private Text homelandText;

    @FXML
    private Text founderText;

    @FXML
    private Text capLocateText;

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
        this.era = era;
        nameText.setText(era.getEntityName());
        timeStampText.setText(era.get());
        homelandText.setText(era.getCapital());
        founderText.setText(era.getFounder());
        capLocateText.setText(era.get());
        timeText.setText(era.getTime());
        for (String alias : era.getAliases()) {
            Text aliasText = new Text(alias);
            aliasFlowPane.getChildren().add(aliasText);
        }
        overviewText.setText(era.getOverview());
        for(Map.Entry<String, Integer> entry : era.getRelatedCharacters().entrySet()){
            Text kingText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                kingText.setFill(Color.web("#3498db"));
                kingText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = HistoricalCharCollection.getData().get(1);
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
            kingsFlowPane.getChildren().add(kingText);
        }
    }
}
