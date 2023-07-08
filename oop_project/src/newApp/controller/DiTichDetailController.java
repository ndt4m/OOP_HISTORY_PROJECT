package newApp.controller;

import java.io.IOException;
import java.util.Map;

import collection.HistoricalCharCollection;
import entity.HistoricalCharacter;
import entity.HistoricalSite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DiTichDetailController {

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
    
    private HistoricalSite site;

    
    public void setHistoricSite(HistoricalSite site) {
    	this.site = site;
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.site = site;
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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/CharacterDetail.fxml"));
                        
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
