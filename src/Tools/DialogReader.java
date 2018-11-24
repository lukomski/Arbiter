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
    public File readDirectoryFromDialog(String title, AnchorPane pane){
        System.out.println("-$1");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File("."));
        System.out.println("#2" + pane);
        File selectedDirectory = directoryChooser.showDialog(pane.getScene().getWindow());
        System.out.println("#3");
        if(selectedDirectory != null){
            return selectedDirectory;
        }
        return null;
    }

}
