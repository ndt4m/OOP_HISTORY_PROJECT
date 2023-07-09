package newApp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class PreviousStack {
    protected static ObservableList<Node> previous = FXCollections.observableArrayList();;

	public static ObservableList<Node> getPrevious() {
		return previous;
	}

	public static void setPrevious(ObservableList<Node> previous) {
		PreviousStack.previous = previous;
	}

}