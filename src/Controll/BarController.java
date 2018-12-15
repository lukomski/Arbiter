package Controll;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.List;

public abstract class BarController {
    @FXML
    AnchorPane rootAnchorPane;

    public boolean isVisible(){
        return rootAnchorPane.isVisible();
    }

    public void setVisible(boolean visible){
        rootAnchorPane.setVisible(visible);
    }
    public abstract void setDisableAll(boolean disable);

    abstract List<File> getDirectories();
}
