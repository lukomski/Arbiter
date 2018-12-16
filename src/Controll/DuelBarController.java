package Controll;

import Tools.DialogReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DuelBarController extends BarController {
    @FXML
    private Button btnChoosePlayer1Dir;
    @FXML
    private Button btnChoosePlayer2Dir;

    private File player1directory;
    private File player2directory;


    @FXML
    private void btnChoosePlayer1DirPressed(){
        player1directory = getFile("Choose Player 1 directory");
        if(player1directory != null) {
            btnChoosePlayer1Dir.setText(player1directory.getName());
        }
    }

    @FXML
    private void btnChoosePlayer2DirPressed() {
        player2directory = getFile("Choose Player 2 directory");
        if (player2directory != null) {
            btnChoosePlayer2Dir.setText(player2directory.getName());
        }
    }

    private File getFile(String message){
        DialogReader dialogReader = new DialogReader();
        File file = dialogReader.readDirectoryFromDialog(message, rootAnchorPane);
        return file;
    }


    @Override
    public void setDisableAll(boolean disable) {
        btnChoosePlayer1Dir.setDisable(disable);
        btnChoosePlayer2Dir.setDisable(disable);
    }
    public void setDisablePlayer2Dir(boolean disable){
        btnChoosePlayer2Dir.setDisable(disable);
    }

    @Override
    public List<File> getDirectories(){
        List<File> directories = new ArrayList<>();
        directories.add(player1directory);
        directories.add(player2directory);
        return directories;
    }
}
