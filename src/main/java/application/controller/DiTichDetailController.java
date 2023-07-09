package application.controller;

import java.io.IOException;
import java.util.Map;

import collection.HistoricalCharCollection;
import entity.HistoricalCharacter;
import entity.HistoricalSite;
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

public class DiTichDetailController extends PreviousStack{

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private Text approvedYearText;

    @FXML
    private Text categoryText;

    @FXML
    private Text locationText;

    @FXML
    private Text nameText;

    @FXML
    private Text overviewText;

    @FXML
    private ScrollPane diTichDetailRoot;

    @FXML
    private FlowPane relatedCharsFlowPane;

    @FXML
    private Button back;

    @FXML
    void backPressed(ActionEvent event) {
        BorderPane parent = (BorderPane)diTichDetailRoot.getParent();
        Node preNode = previous.remove(previous.size() - 1);
        parent.setCenter(preNode);
    }

    public void setHistoricSite(HistoricalSite site) {
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        nameText.setText(site.getEntityName());
        locationText.setText(site.getLocation());
        overviewText.setText(site.getOverview());
        categoryText.setText(site.getCategory());
        approvedYearText.setText(site.getEstablishment());

        if (!site.getAliases().isEmpty()){
            for (String alias : site.getAliases()) {
                Text aliasText = new Text(alias);
                aliasFlowPane.getChildren().add(aliasText);
            }
        }else{
            aliasFlowPane.getChildren().add(new Text("Không rõ"));
        }

        for (Map.Entry<String, Integer> entry : site.getRelatedCharacters().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
                    try {
                        previous.addAll(FXCollections.observableArrayList(diTichDetailRoot));

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CharacterDetail.fxml"));

                        ScrollPane root = loader.load();
                        CharacterDetailController controller = loader.getController();
                        controller.setFigure(figure);
                        BorderPane parent = (BorderPane)diTichDetailRoot.getParent();
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