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
    @FXML
    private AnchorPane tournamentAnchorPane;

    private File tournamentDirectory;
    private java.io.File playerDir;

    public File getTournamentDirectory(){
        return tournamentDirectory;
    }
    public void setVisible(boolean visible){
        tournamentAnchorPane.setVisible(visible);
    }
    public boolean isVisible(){
        return tournamentAnchorPane.isVisible();
    }

    @FXML
    public void selectTournamentDirPressed(){
        DialogReader dr = new DialogReader();
        tournamentDirectory = dr.readDirectoryFromDialog("Choose Tournament directory", tournamentAnchorPane);
    }
}
