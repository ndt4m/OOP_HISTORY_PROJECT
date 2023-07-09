package application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public abstract class PreviousStack {
    protected static ObservableList<Node> previous = FXCollections.observableArrayList();
}