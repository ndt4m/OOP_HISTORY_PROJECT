package application.controller;

import application.App;
import collection.FestivalCollection;
import entity.Festival;
import entity.HistoricalCharacter;
import collection.HistoricalCharCollection;
import entity.HistoricalSite;
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

public class SiteDetailScreenController {
    @FXML
    private Text nameText;

    @FXML
    private Text locationText;

    @FXML
    private Text overviewText;

    @FXML
    private Text categoryText;

    @FXML
    private Text approvedYearText;

    @FXML
    private Text festivalsText;

    @FXML
    private FlowPane relatedCharsFlowPane;

    @FXML
    private SidebarController sideBarController;

    private HistoricalSite site;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        sideBarController.switchByGetFxml("/application/view/HistoricSiteScreen.fxml", event);
    }

    public void setHistoricSite(HistoricalSite site) {
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

        /*
        for (Map.Entry<String, Integer> entry : site.getRelatedFestivalId().entrySet()) {
            festivalsText.setText(entry.getKey());
            if (entry.getValue() != null) {
                festivalsText.setFill(Color.web("#3498db"));
                festivalsText.setOnMouseClicked(mouseEvent -> {
                    Festival fes = Festivals.collection.get(entry.getValue());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/FesDetailScreen.fxml"));
                        Parent root = loader.load();
                        FesDetailScreenController controller = loader.getController();
                        controller.setFestival(fes);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        */

        for (Map.Entry<String, Integer> entry : site.getRelatedCharacters().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalCharacter figure = historicalCharCollection.get(entry.getValue());
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
            relatedCharsFlowPane.getChildren().add(figureText);
        }
    }
}
