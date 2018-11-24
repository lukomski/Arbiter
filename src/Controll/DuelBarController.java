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
    private Label labelPlayer1Dir;
    @FXML
    private Label labelPlayer2Dir;

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
    @FXML
    private void btnChoosePlayer1DirPressed(){
        DialogReader dialogReader = new DialogReader();
        File player1directory = dialogReader.readDirectoryFromDialog("Choose Player 1 directory",parentController.getMainPane());
        this.parentController.setPlayer1directory(player1directory);
        labelPlayer1Dir.setText(player1directory.getName());
    }
    @FXML
    private void btnChoosePlayer2DirPressed(){
        DialogReader dialogReader = new DialogReader();
        File player2directory = dialogReader.readDirectoryFromDialog("Choose Player 2 directory",parentController.getMainPane());
        this.parentController.setPlayer2directory(player2directory);
        labelPlayer2Dir.setText(player2directory.getName());
    }

    public void selectPlayersButtonPressed(){
        DialogReader dialogReader = new DialogReader();
        File player1directory = dialogReader.readDirectoryFromDialog("Choose Player 1 directory",parentController.getMainPane());
        this.parentController.setPlayer1directory(player1directory);
        labelPlayer1Dir.setText(player1directory.getName());

        File player2directory = dialogReader.readDirectoryFromDialog("Choose Player 2 directory",parentController.getMainPane());
        this.parentController.setPlayer2directory(player2directory);
    }
}
