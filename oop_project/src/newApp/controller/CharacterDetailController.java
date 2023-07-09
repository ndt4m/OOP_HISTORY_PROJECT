package newApp.controller;

import java.io.IOException;

import collection.EraCollection;
import collection.HistoricalCharCollection;
import entity.Era;
import entity.HistoricalCharacter;
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

public class CharacterDetailController extends PreviousStack{

	@FXML
	private FlowPane aliasFlowPane;

	@FXML
	private Text dateOfBirthText;

	@FXML
	private Text dateOfDeathText;

	@FXML
	private Text eraText;

	@FXML
	private Text fatherText;

	@FXML
	private Text hometownText;

	@FXML
	private Text motherText;

	@FXML
	private Text nameText;

	@FXML
	private Text occupationText;

	@FXML
	private Text overviewText;
	
	@FXML
	private ScrollPane charDetailRoot;

	@FXML
	private Text workTenureText;
	
	@FXML
	private Button back;
	
	@FXML
	void backPressed(ActionEvent event) {
		BorderPane parent = (BorderPane) charDetailRoot.getParent();
		Node preNode = previous.remove(previous.size() - 1);
		parent.setCenter(preNode);
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

        nameText.setText(character.getEntityName());

        if (!character.getAliases().isEmpty()){
            for (String alias : character.getAliases()) {
                Text aliasText = new Text(alias);
                aliasFlowPane.getChildren().add(aliasText);
            }
        }else{
            aliasFlowPane.getChildren().add(new Text("Không rõ"));
        }

        dateOfBirthText.setText(character.getDateOfBirth());
        dateOfDeathText.setText(character.getDateOfDeath());
        overviewText.setText(character.getOverview());
        workTenureText.setText(character.getWorkTenure());
        hometownText.setText(character.getHometown());
        occupationText.setText(character.getOccupation());

        if (!character.getEraName().isEmpty())
        {
            eraText.setText(character.getEraName().keySet().toArray(new String[0])[0]);
        }
        else
        {
            eraText.setText("Không rõ");
            
        }
        if (!character.getFatherName().isEmpty())
        {
            fatherText.setText(character.getFatherName().keySet().toArray(new String[0])[0]);

        }
        else
        {
            fatherText.setText("Không rõ");
        }

        if (!character.getMotherName().isEmpty())
        {
            motherText.setText(character.getMotherName().keySet().toArray(new String[0])[0]);
        }
        else
        {
            motherText.setText("Không rõ");
        }

        Era era = eraCollection.get(character.getEraName().get(eraText.getText()));
        HistoricalCharacter father = historicalCharCollection.get(character.getFatherName().get(fatherText.getText()));
        HistoricalCharacter mother = historicalCharCollection.get(character.getMotherName().get(motherText.getText()));

        if(era != null) {
            eraText.setFill(Color.web("#3498db"));
            eraText.setOnMouseClicked(mouseEvent -> {
                try {
                	previous.addAll(FXCollections.observableArrayList(charDetailRoot));
                	
                	FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/EraDetail.fxml"));
                	ScrollPane root = loader.load();
                    EraDetailController controller = loader.getController();
                    controller.setEra(era);
                    BorderPane parent =(BorderPane) charDetailRoot.getParent();
                    parent.setCenter(root);
                    
                } catch (IOException e){
                    e.printStackTrace();
                }
            });
        } else {
            eraText.setFill(Color.web("#000000"));
        }

        if(father != null) {
            fatherText.setFill(Color.web("#3498db"));
            fatherText.setOnMouseClicked(mouseEvent ->{
            	try {
            		previous.addAll(FXCollections.observableArrayList(charDetailRoot));
            		FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/CharacterDetail.fxml"));
            		ScrollPane root = loader.load();
            		CharacterDetailController controller = loader.getController();
            		controller.setFigure(father);
					BorderPane parent =(BorderPane) charDetailRoot.getParent();
                    parent.setCenter(root);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            );
        } else {
            fatherText.setFill(Color.web("#000000"));
        }

        if(mother != null) {
            motherText.setFill(Color.web("#3498db"));
            motherText.setOnMouseClicked(mouseEvent -> {
            	try {
            		previous.addAll(FXCollections.observableArrayList(charDetailRoot));
            		
            		FXMLLoader loader = new FXMLLoader(getClass().getResource("/newApp/fxml/CharacterDetail.fxml"));
            		ScrollPane root = loader.load();
            		CharacterDetailController controller = loader.getController();
            		controller.setFigure(mother);
					BorderPane parent =(BorderPane) charDetailRoot.getParent();
                    parent.setCenter(root);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            );
        } else {
            motherText.setFill(Color.web("#000000"));
        }

    }


}
