package com.example.cs4b_project;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Gameboard {

    public Button button1,  button2, button3, button4, button5, button6, button7, button8, button9 = null;

    // Need to REFACTOR to make event lister dynamic and reusable
    public void onCellClick(ActionEvent actionEvent) {
        //check if which player's turn
        this.button1.setText("X");
        //check win condition

        // disable button
        //this.button1.setDisabled(true);
    }
    public void onCellClick2(ActionEvent actionEvent) {
        this.button2.setText("X");
    }
    public void onCellClick3(ActionEvent actionEvent) {
        this.button3.setText("X");
    }
    public void onCellClick4(ActionEvent actionEvent) {
        this.button4.setText("X");
    }
    public void onCellClick5(ActionEvent actionEvent) {
        this.button5.setText("X");
    }
    public void onCellClick6(ActionEvent actionEvent) {
        this.button6.setText("X");
    }
    public void onCellClick7(ActionEvent actionEvent) {
        this.button7.setText("X");
    }
    public void onCellClick8(ActionEvent actionEvent) {
        this.button8.setText("X");
    }
    public void onCellClick9(ActionEvent actionEvent) {
        this.button9.setText("X");
    }

}
