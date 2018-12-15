package Controll;

import Tools.DialogReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TournamentBarController extends BarController {
    @FXML
    private Button btnSelectTournamentDir;
    private File tournamentDirectory;

    @Override
    public List<File> getDirectories() {
        List<File> directories = new ArrayList<>();
        for (int i = 0; i < tournamentDirectory.listFiles().length; i++) {
            directories.add(tournamentDirectory.listFiles()[i]);
        }
        return directories;
    }

    @FXML
    private void selectTournamentDirPressed() {
        DialogReader dr = new DialogReader();
        tournamentDirectory = dr.readDirectoryFromDialog("Choose Arena directory", rootAnchorPane);
        if (tournamentDirectory != null) {
            btnSelectTournamentDir.setText(tournamentDirectory.getName());
        }
    }

    @Override
    public void setDisableAll(boolean disable){
        btnSelectTournamentDir.setDisable(disable);
    }
}
