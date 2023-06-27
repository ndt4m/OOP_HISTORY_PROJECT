package application.controller;

import application.App;
import history.model.Era;
import history.collection.Eras;
import history.model.HistoricalFigure;
import history.collection.HistoricalFigures;
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

public class FigureDetailScreenController {

    @FXML
    private Text nameText;

    @FXML
    private Text realNameText;

    @FXML
    private Text bornText;

    @FXML
    private Text diedText;

    @FXML
    private Text overviewText;

    @FXML
    private Text workTimeText;

    @FXML
    private Text eraText;

    @FXML
    private Text fatherText;

    @FXML
    private Text motherText;

    @FXML
    private Text precededByText;

    @FXML
    private Text succeededByText;

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private SidebarController sideBarController;

    private HistoricalFigure figure;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        sideBarController.switchByGetFxml("/application/view/HistoricalFiguresScreen.fxml", event);
    }

    public void setFigure(HistoricalFigure figure) {
        this.figure = figure;
        nameText.setText(figure.getName());
        realNameText.setText(figure.getRealName());
        for (String alias : figure.getAliases()) {
            Text aliasText = new Text(alias);
            aliasFlowPane.getChildren().add(aliasText);
        }
        bornText.setText(figure.getBorn());
        diedText.setText(figure.getDied());
        overviewText.setText(figure.getOverview());
        workTimeText.setText(figure.getWorkTime());
        eraText.setText(figure.getEra().getKey());
        fatherText.setText(figure.getFather().getKey());
        motherText.setText(figure.getMother().getKey());
        precededByText.setText(figure.getPrecededBy().getKey());
        succeededByText.setText(figure.getSucceededBy().getKey());

        Era era = Eras.collection.get(figure.getEra().getValue());
        HistoricalFigure father = HistoricalFigures.collection.get(figure.getFather().getValue());
        HistoricalFigure mother = HistoricalFigures.collection.get(figure.getMother().getValue());
        HistoricalFigure precededFigure = HistoricalFigures.collection.get(figure.getPrecededBy().getValue());
        HistoricalFigure succeededFigure = HistoricalFigures.collection.get(figure.getSucceededBy().getValue());


        if(era != null) {
            eraText.setFill(Color.web("#3498db"));
            eraText.setOnMouseClicked(mouseEvent -> {
                try {
                    FXMLLoader loader = new FXMLLoader(App.convertToURL("/application/view/EraDetailScreen.fxml"));
                    Parent root = loader.load();
                    EraDetailScreenController controller = loader.getController();
                    controller.setEra(era);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e){
                    e.printStackTrace();
                }
            });
        } else {
            eraText.setFill(Color.web("#000000"));
        }
        if(father != null) {
            fatherText.setFill(Color.web("#3498db"));
            fatherText.setOnMouseClicked(mouseEvent -> setFigure(father));
        } else {
            fatherText.setFill(Color.web("#000000"));
        }
        if(mother != null) {
            motherText.setFill(Color.web("#3498db"));
            motherText.setOnMouseClicked(mouseEvent -> setFigure(mother));
        } else {
            motherText.setFill(Color.web("#000000"));
        }
        if(precededFigure != null) {
            precededByText.setFill(Color.web("#3498db"));
            precededByText.setOnMouseClicked(mouseEvent -> setFigure(precededFigure));
        } else {
            precededByText.setFill(Color.web("#000000"));
        }
        if(succeededFigure != null) {
            succeededByText.setFill(Color.web("#3498db"));
            succeededByText.setOnMouseClicked(mouseEvent -> setFigure(succeededFigure));
        } else {
            succeededByText.setFill(Color.web("#000000"));
        }
    }
}
