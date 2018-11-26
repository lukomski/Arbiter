package Controll;

import Tools.DialogReader;
import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TournamentBarController extends BarController {

    private File tournamentDirectory;

    @Override
    public List<File> getDirectories(){
        List<File> directories = new ArrayList<>();
        for(int i = 0;i < tournamentDirectory.listFiles().length;i++){
            directories.add(tournamentDirectory.listFiles()[i]);
        }
        return directories;
    }

    @FXML
    private void selectTournamentDirPressed(){
        DialogReader dr = new DialogReader();
        tournamentDirectory = dr.readDirectoryFromDialog("Choose Tournament directory", rootAnchorPane);
    }
}
