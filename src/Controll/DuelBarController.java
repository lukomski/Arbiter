package Controll;

import Tools.DialogReader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DuelBarController extends BarController {
    @FXML
    private Label labelPlayer1Dir;
    @FXML
    private Label labelPlayer2Dir;

    private File player1directory;
    private File player2directory;


    @FXML
    private void btnChoosePlayer1DirPressed(){
        DialogReader dialogReader = new DialogReader();
        player1directory = dialogReader.readDirectoryFromDialog("Choose Player 1 directory", rootAnchorPane);
        labelPlayer1Dir.setText(player1directory.getName());
    }

    @FXML
    private void btnChoosePlayer2DirPressed(){
        DialogReader dialogReader = new DialogReader();
        player2directory = dialogReader.readDirectoryFromDialog("Choose Player 2 directory", rootAnchorPane);
        labelPlayer2Dir.setText(player2directory.getName());
    }

    @Override
    public List<File> getDirectories(){
        List<File> directories = new ArrayList<>();
        directories.add(player1directory);
        directories.add(player2directory);
        return directories;
    }
}
