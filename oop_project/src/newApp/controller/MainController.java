package newApp.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

public class MainController extends PreviousStack{

    @FXML
    private ToggleButton Era;

    @FXML
    private ToggleButton HisSite;

    @FXML
    private ToggleButton character;

    @FXML
    private ToggleButton event;

    @FXML
    private ToggleButton festival;

    @FXML
    private BorderPane main;

    @FXML
    private ToggleButton trangchu;

    
    @FXML
    private FXMLLoader loadHomePage = new FXMLLoader(getClass().getResource("/newApp/fxml/trangchu.fxml"));

    private BorderPane characterBox;
    private BorderPane homeBox;
    private BorderPane diTichBox;
    private BorderPane eventBox;
    private BorderPane fesBox;
    private BorderPane eraBox;
 
    
    @FXML
    void EraPressed(ActionEvent event) {
    	FXMLLoader loadEra = new FXMLLoader(getClass().getResource("/newApp/fxml/Era.fxml"));
    	try {
			eraBox = loadEra.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	main.setCenter(eraBox);
    }

    @FXML
    void HisSitePressed(ActionEvent event) {
    	FXMLLoader loadDiTich = new FXMLLoader(getClass().getResource("/newApp/fxml/DiTich.fxml"));
    	try {
			diTichBox = loadDiTich.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.setCenter(diTichBox);
    }
    
    
    @FXML
    void characterPressed(ActionEvent event) {
    	FXMLLoader loadChar = new FXMLLoader(getClass().getResource("/newApp/fxml/Character.fxml"));
    	try {
			characterBox = loadChar.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.setCenter(characterBox);
    }

    @FXML
    void eventPressed(ActionEvent event) {
    	FXMLLoader loadEvent = new FXMLLoader(getClass().getResource("/newApp/fxml/Event.fxml"));
    	try {
			eventBox = loadEvent.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	main.setCenter(eventBox);
    }

    @FXML
    void festivalPressed(ActionEvent event) {
        FXMLLoader loadFes = new FXMLLoader(getClass().getResource("/newApp/fxml/Festival.fxml")); 	
		try {
			fesBox = loadFes.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	main.setCenter(fesBox);
    }

    @FXML
    void trangchuPressed(ActionEvent event) {
    	main.setCenter(homeBox);
    }

    @FXML
    void initialize() {
    	try {
			homeBox = loadHomePage.load();
			main.setCenter(homeBox);

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
