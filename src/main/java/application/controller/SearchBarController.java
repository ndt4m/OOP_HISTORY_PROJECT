package application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SearchBarController {

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TextField searchBox;

    private SearchBoxListener searchBoxListener;

    public void setSearchBoxListener(SearchBoxListener searchBoxListener) {
        this.searchBoxListener = searchBoxListener;
    }

    @FXML
    public void initialize() {

        filterComboBox.setItems(FXCollections.observableArrayList("By Name", "By ID"));

        filterComboBox.getSelectionModel().selectFirst();

        filterComboBox.setOnAction(e -> {
            if (searchBox.getText().isBlank()){
                searchBoxListener.handleBlank();
            } else {
                if (filterComboBox.getSelectionModel().getSelectedItem().equals("By ID")) {
                    /* Thực thi khi chuyển comboBox sang By ID */
                    searchBoxListener.handleSearchId(searchBox.getText());
                } else if (filterComboBox.getSelectionModel().getSelectedItem().equals("By Name")) {
                    /* Thực thi khi chuyển comboBox sang By Name */
                    searchBoxListener.handleSearchName(searchBox.getText());
                }
            }
        });

        searchBox.textProperty().addListener((
                (observableValue, oldValue, newValue) -> {
                    if (newValue.isBlank()){
                        searchBoxListener.handleBlank();
                    } else {
                        if (filterComboBox.getValue().equals("By ID")){
                            searchBoxListener.handleSearchId(newValue);
                        }
                        else if (filterComboBox.getValue().equals("By Name")){
                            searchBoxListener.handleSearchName(newValue);
                        }
                    }
                }
        ));
    }

}