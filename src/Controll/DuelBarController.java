package Controll;

import Tools.DialogReader;
import Tools.InfoReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;

public class DuelBarController {
    @FXML
    private Label player1Text;
    @FXML
    private Label player2Text;

    File player1directory;
    File player2directory;

    private Controller paretController;
    DuelBarController(Controller parentController){
        this.parentController = parentController;
    }

    File tournamentDirectory;
    private java.io.File playerDir;

    private Controller parentController;
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    private final StringProperty value = new SimpleStringProperty();

    public StringProperty valueProperty() {
        return value;
    }

    public void selectPlayersButtonPressed(){
        DialogReader dialogReader = new DialogReader();
        player1directory = dialogReader.readDirectoryFromDialog("Choose Player 1 directory",parentController.getMainPane());
        this.parentController.setPlayer1directory(player1directory);
        player1Text.setText("Player 1: "+ InfoReader.read(player1directory)[1]);

        player2directory = dialogReader.readDirectoryFromDialog("Choose Player 2 directory",parentController.getMainPane());
        this.parentController.setPlayer2directory(player2directory);
        player2Text.setText("Player 2: "+InfoReader.read(player2directory)[1]);
    }
}
