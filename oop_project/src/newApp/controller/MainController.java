package newApp.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import newApp.App.App;

public class MainController {

    @FXML
    private Button Era;

    @FXML
    private Button HisSite;

    @FXML
    private Button character;

    @FXML
    private Button event;

    @FXML
    private Button festival;

    @FXML
    private BorderPane main;

    @FXML
    private Button trangchu;

    
    
    
    @FXML
    private FXMLLoader loadChar = new FXMLLoader(getClass().getResource("/newApp/fxml/Character.fxml"));

    
    @FXML
    private FXMLLoader loadHomePage = new FXMLLoader(getClass().getResource("/newApp/fxml/trangchu.fxml"));

    
    @FXML
    private FXMLLoader loadDiTich = new FXMLLoader(getClass().getResource("/newApp/fxml/DiTich.fxml"));

    @FXML
    private FXMLLoader loadEra = new FXMLLoader(getClass().getResource("/newApp/fxml/Era.fxml"));

    
    @FXML
    private FXMLLoader loadEvent = new FXMLLoader(getClass().getResource("/newApp/fxml/Event.fxml"));
    
    @FXML
    private FXMLLoader loadFes = new FXMLLoader(getClass().getResource("/newApp/fxml/Festival.fxml"));
    
    
    private BorderPane characterBox;
    private BorderPane homeBox;
    private BorderPane diTichBox;
    private BorderPane eventBox;
    private BorderPane fesBox;
    private BorderPane eraBox;
 
    
    @FXML
    void EraPressed(ActionEvent event) {
    	main.setCenter(eraBox);
    }

    @FXML
    void HisSitePressed(ActionEvent event) {
		main.setCenter(diTichBox);
    }
    
    @FXML
    void characterPressed(ActionEvent event) {
		main.setCenter(characterBox);
    }

    @FXML
    void eventPressed(ActionEvent event) {
    	main.setCenter(eventBox);
    }

    @FXML
    void festivalPressed(ActionEvent event) {
    	main.setCenter(fesBox);
    }

    @FXML
    void trangchuPressed(ActionEvent event) {
    	main.setCenter(homeBox);
    }

    @FXML
    void initialize() {
    	try {
			characterBox = loadChar.load();
			homeBox = loadHomePage.load();
			diTichBox = loadDiTich.load();
			eraBox = loadEra.load();
			fesBox = loadFes.load();
			eventBox = loadEvent.load();
			main.setCenter(homeBox);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
