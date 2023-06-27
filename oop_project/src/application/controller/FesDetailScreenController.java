package application.controller;

import application.App;
import history.model.Festival;
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
import java.util.Map;

public class FesDetailScreenController {
    @FXML
    private Text nameText;

    @FXML
    private Text dateText;

    @FXML
    private Text locationText;

    @FXML
    private Text firstTimeText;

    @FXML
    private Text noteText;

    @FXML
    private SidebarController sideBarController;

    @FXML
    private FlowPane relatedCharsFlowPane;

    private Festival fes;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        sideBarController.switchByGetFxml("/application/view/FestivalScreen.fxml", event);
    }

    public void setFestival(Festival fes) {
        this.fes = fes;
        nameText.setText(fes.getName());
        dateText.setText(fes.getDate());
        locationText.setText(fes.getLocation());
        firstTimeText.setText(fes.getFirstTime());
        noteText.setText(fes.getNote());
        for (Map.Entry<String, Integer> entry : fes.getRelatedFiguresId().entrySet()){
            Text figureText = new Text(entry.getKey());
            if(entry.getValue() != null) {
                figureText.setFill(Color.web("#3498db"));
                figureText.setOnMouseClicked(mouseEvent -> {
                    HistoricalFigure figure = HistoricalFigures.collection.get(entry.getValue());
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
