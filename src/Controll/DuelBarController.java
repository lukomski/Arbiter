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
    @FXML
    private AnchorPane duelAnchorPane;

    private File player1directory;
    private File player2directory;

    public void setVisible(boolean visible){
        duelAnchorPane.setVisible(visible);
    }

    public boolean isVisible(){
        return duelAnchorPane.isVisible();
    }

    public File getPlayer1directory(){
        return player1directory;
    }

    public File getPlayer2directory(){
        return player2directory;
    }

    @FXML
    private void btnChoosePlayer1DirPressed(){
        DialogReader dialogReader = new DialogReader();
        player1directory = dialogReader.readDirectoryFromDialog("Choose Player 1 directory",duelAnchorPane);
        labelPlayer1Dir.setText(player1directory.getName());
    }

    @FXML
    private void btnChoosePlayer2DirPressed(){
        DialogReader dialogReader = new DialogReader();
        player2directory = dialogReader.readDirectoryFromDialog("Choose Player 2 directory",duelAnchorPane);
        labelPlayer2Dir.setText(player2directory.getName());
    }
}
