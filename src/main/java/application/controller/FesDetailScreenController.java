package application.controller;

import application.App;
import entity.Festival;
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

public class FesDetailScreenController {
    @FXML
    private Text overviewText;

    @FXML
    private Text nameText;

    @FXML
    private Text dateText;

    @FXML
    private Text locationText;

    @FXML
    private TopBarController topBarController = new TopBarController();

    @FXML
    private FlowPane relatedCharsFlowPane;

    @FXML
    private FlowPane aliasFlowPane;

    private Festival fes;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        topBarController.switchByGetFxml("/application/FestivalScreen.fxml", event);
    }

    public void setFestival(Festival fes) {
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
            System.out.println(historicalCharCollection.getData().size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.fes = fes;
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