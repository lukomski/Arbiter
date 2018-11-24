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

public class TournamentBarController {
    private Controller paretController;

    TournamentBarController(Controller parentController){
        this.parentController = parentController;
    }

    private File tournamentDirectory;
    private java.io.File playerDir;

    private Controller parentController;
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public void selectTournamentDirPressed(){
        DialogReader dr = new DialogReader();
        tournamentDirectory = dr.readDirectoryFromDialog("Choose Tournament directory", parentController.getMainPane());
        parentController.setTournamentDirectory(tournamentDirectory);
    }
}
