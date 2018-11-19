package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Optional;

public class CustomDialog {
    public int initNumberDialog(String title, String headerText, int defaultValue, int min, int max){
        TextInputDialog dialog = new TextInputDialog(String.valueOf(defaultValue));
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText("Choose a number between " + String.valueOf(min) + " and " + String.valueOf(max)+": ");
        Optional<String> result = dialog.showAndWait();
        int value = defaultValue;

        try {
            value = Integer.parseInt(result.get());
        }catch (NumberFormatException e){

        }



        return value;
    }
    public File initDirectoryChooser(String title,Pane pane){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File("."));
        File selectedDirectory = directoryChooser.showDialog(pane.getScene().getWindow());

        if(selectedDirectory != null){
            return selectedDirectory;
        }
        return null;
    }

}
