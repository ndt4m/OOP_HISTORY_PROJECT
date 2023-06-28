package application.controller;

import application.App;
import collection.EraCollection;
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

public class FigureDetailScreenController {

    @FXML
    private Text nameText;

    @FXML
    private Text dateOfBirthText;

    @FXML
    private Text dateOfDeathText;

    @FXML
    private Text overviewText;

    @FXML
    private Text workTenureText;

    @FXML
    private Text eraText;

    @FXML
    private Text fatherText;

    @FXML
    private Text motherText;

    @FXML
    private FlowPane aliasFlowPane;

    @FXML
    private SidebarController sideBarController;

    private HistoricalCharacter character;

    @FXML
    public void onClickBack(ActionEvent event) throws IOException {
        sideBarController.switchByGetFxml("/application/view/HistoricalFiguresScreen.fxml", event);
    }

    public void setFigure(HistoricalCharacter character) {
        HistoricalCharCollection historicalCharCollection = new HistoricalCharCollection();
        try {
            historicalCharCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EraCollection eraCollection = new EraCollection();
        try {
            eraCollection.loadJsonFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.character = character;

        nameText.setText(character.getEntityName());

        for (String alias : character.getAliases()) {
            Text aliasText = new Text(alias);
            aliasFlowPane.getChildren().add(aliasText);
        }
        dateOfBirthText.setText(character.getDateOfBirth());
        dateOfDeathText.setText(character.getDateOfDeath());
        overviewText.setText(character.getOverview());
        workTenureText.setText(character.getWorkTenure());
        //System.out.println("==============================" +character.getId()+ "=============================");
        //System.out.println("==============================" +character.getEraName().get(eraText.getText())+ "=============================");
        
        eraText.setText(character.getEraName().keySet().toArray(new String[0])[0]);
        fatherText.setText(character.getFatherName().keySet().toArray(new String[0])[0]);
        motherText.setText(character.getMotherName().keySet().toArray(new String[0])[0]);

        Era era = eraCollection.get(character.getEraName().get(eraText.getText()));
        HistoricalCharacter father = historicalCharCollection.get(character.getFatherName().get(fatherText.getText()));
        HistoricalCharacter mother = historicalCharCollection.get(character.getMotherName().get(motherText.getText()));

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

    }
}
