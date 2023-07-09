package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class MainController extends PreviousStack{

    @FXML
    private ButtonBar buttonBar;

    @FXML
    private ToggleButton era;

    @FXML
    private ToggleButton hisSite;

    @FXML
    private ToggleButton character;

    @FXML
    private ToggleButton event;

    @FXML
    private ToggleButton festival;

    @FXML
    private ToggleGroup identify;

    @FXML
    private BorderPane main;

    @FXML
    private ToggleButton trangChu;


    @FXML
    private FXMLLoader loadHomePage = new FXMLLoader(getClass().getResource("/application/trangchu.fxml"));

    private BorderPane characterBox;
    private BorderPane homeBox;
    private BorderPane diTichBox;
    private BorderPane eventBox;
    private BorderPane fesBox;
    private BorderPane eraBox;
    private ToggleButton currentButton = trangChu;  // lưu trang đang hiện thị hiện tại

    @FXML
    void eraPressed(ActionEvent event) {
        checkPrevious();
        if (currentButton != era){
            FXMLLoader loadEra = new FXMLLoader(getClass().getResource("/application/Era.fxml"));
            try {
                eraBox = loadEra.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.setCenter(eraBox);
            currentButton = era;
        }
        era.setSelected(true);
    }

    @FXML
    void hisSitePressed(ActionEvent event) {
        checkPrevious();
        if (currentButton != hisSite){
            FXMLLoader loadDiTich = new FXMLLoader(getClass().getResource("/application/DiTich.fxml"));
            try {
                diTichBox = loadDiTich.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.setCenter(diTichBox);
            currentButton = hisSite;
        }
        hisSite.setSelected(true);
    }


    @FXML
    void characterPressed(ActionEvent event) {
        checkPrevious();
        if (currentButton != character){
            FXMLLoader loadChar = new FXMLLoader(getClass().getResource("/application/Character.fxml"));
            try {
                characterBox = loadChar.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.setCenter(characterBox);
            currentButton = character;
        }
        character.setSelected(true);
    }

    @FXML
    void eventPressed(ActionEvent event1) {
        checkPrevious();
        if (currentButton != event){
            FXMLLoader loadEvent = new FXMLLoader(getClass().getResource("/application/Event.fxml"));
            try {
                eventBox = loadEvent.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.setCenter(eventBox);
            currentButton = event;
        }
        event.setSelected(true);
    }

    @FXML
    void festivalPressed(ActionEvent event) {
        checkPrevious();
        if (currentButton != festival){
            FXMLLoader loadFes = new FXMLLoader(getClass().getResource("/application/Festival.fxml"));
            try {
                fesBox = loadFes.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.setCenter(fesBox);
            currentButton = festival;
        }
        festival.setSelected(true);

    }

    @FXML
    void trangChuPressed(ActionEvent event) {
        checkPrevious();
        if(currentButton != trangChu){
            main.setCenter(homeBox);
            currentButton = trangChu;
        }
        trangChu.setSelected(true);
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

    void checkPrevious(){
        if (!previous.isEmpty()) {
            currentButton = null;
            previous.clear();
        }
    }
}