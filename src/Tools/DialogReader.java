package Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class DialogReader {
    public static int readNumberFromDialog(String title, String headerText, int defaultValue, int min, int max){
        try {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(defaultValue));
            dialog.setTitle(title);
            dialog.setHeaderText(headerText);
            dialog.setContentText("Choose a number between " + String.valueOf(min) + " and " + String.valueOf(max)+": ");
            Optional<String> result = dialog.showAndWait();

            int value = Integer.parseInt(result.get());
            return value;
        }catch (Exception e){
            return -1;
        }

    }
    public static File readDirectoryFromDialog(String title, AnchorPane pane){
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
