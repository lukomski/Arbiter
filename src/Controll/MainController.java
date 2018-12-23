package Controll;

import Model.Arena;
import Model.Board;
import Model.Duel;
import Tools.DialogReader;
import Tools.Guide;
import Tools.ScoreResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Tools.LogWriter;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

public class MainController {
    @FXML
    private ScrollPane duelsScrollPane;
    @FXML
    private Slider sizeSlider;
    @FXML
    public AnchorPane mainPane;
    @FXML
    private Label sizeText;
    @FXML
    private Canvas canvas;
    @FXML
    private ProgressIndicator arenaProgressIndicator;
    @FXML
    private TabPane leftTabPane;

    @FXML
    private Button startButton;
    @FXML
    private Button directoryButton;
    @FXML
    private TableView scoreTable;
    @FXML
    private ListView duelList;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private CheckBox randCheckBox;

    private Guide guide = null;
    private Arena arena;
    private int boardSize = 5;
    private int humanRectAngle=1;
    private File directory;
    private Board board;
    private int size;

    @FXML
    public void initialize(){
        startButton.setDisable(true);
        initScoreTable();
        initDuelList();
    }

    @FXML
    private void btnDirectoryPressed() {
        DialogReader dr = new DialogReader();
        directory = dr.readDirectoryFromDialog("Choose Arena directory", mainPane);
        if (directory != null) {
            directoryButton.setText(directory.getName());
            startButton.setDisable(false);
        }
    }
    private void setDisableLeftBarItems(boolean disable){
        directoryButton.setDisable(disable);
        sizeSlider.setDisable(disable);
        randCheckBox.setDisable(disable);
    }

    public void bntStartPressed(){
        if(startButton.getText() == "Stop"){
            arena.interrupt();
            startButton.setText("Start");
            setDisableLeftBarItems(false);

        } else {
            setDisableLeftBarItems(true);
            startButton.setText("Stop");
            size = (int) sizeSlider.getValue();
            board = new Board(size, canvas);
            board.draw();

            arena = new Arena(this);
            clearDuelList();
            arena.start();
        }
    }
    public void arenaEnded(){
        Platform.runLater(() -> startButton.setText("Start"));
        leftTabPane.getSelectionModel().select(leftTabPane.getTabs().get(1));
        setDisableLeftBarItems(false);
    }

    public File getDirectory() {
        return directory;
    }

    public Board getBoard() {
        return board;
    }
    public void clearScoreTable(){
        scoreTable.getItems().clear();
    }
    public void addItemToScoreTable(ScoreResult scoreResult){
        scoreTable.getItems().add(scoreResult);
    }
    public void clearDuelList(){
        duelList.getItems().clear();
    }
    public  void addItemToDuelList(Duel duel){

        Platform.runLater(()->duelList.getItems().add(duel));
    }

    public void initDuelList(){
        duelList.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {

                System.out.println(duelList.getSelectionModel()
                        .getSelectedItem());
                Duel duel = (Duel) duelList.getSelectionModel().getSelectedItem();
                List<String> moves = duel.loadLogFile();
                mainTabPane.getSelectionModel().select(mainTabPane.getTabs().get(1));
                board.clean();
                board.draw();
                Guide guide = new Guide(board, moves);

                mainPane.getScene().setOnKeyReleased(event -> {
                    if(event.getCode() == KeyCode.N || event.getCode() == KeyCode.RIGHT) {
                        guide.nextMove();
                    }
                });

            }
        });
        duelList.setCellFactory((Callback<ListView<Duel>, ListCell<Duel>>) param -> new ColoredCell());
    }
    public void initScoreTable(){
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(85);
        idCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(183);
        nameCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("name"));

        TableColumn nickCol = new TableColumn("Nick");
        nickCol.setMinWidth(140);
        nickCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, ScoreResult>("nick"));

        TableColumn winsCol = new TableColumn("Wins");
        winsCol.setMinWidth(30);
        winsCol.setPrefWidth(35);
        winsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("wins"));

        TableColumn losesCol = new TableColumn("Loses");
        losesCol.setMinWidth(30);
        losesCol.setPrefWidth(35);
        losesCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("loses"));

        TableColumn disqualificationsCol = new TableColumn("Disqualifications");
        disqualificationsCol.setMinWidth(100);
        disqualificationsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("disqualifications"));

        scoreTable.getColumns().addAll(idCol,nameCol,nickCol, winsCol, losesCol,disqualificationsCol);
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    static class ColoredCell extends ListCell<Duel>{

            @Override
            public void updateItem(Duel duel, boolean empty) {
                super.updateItem(duel, empty);

                if(duel == null || empty) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-control-inner-background: derive(-fx-base,80%);");
                } else {
                    setGraphic(createText(duel));
                    if(!duel.isNormalWin()){

                        //this.setTextFill(Color.RED);
                        setStyle("-fx-control-inner-background: derive(red, 90%);");

                    }
                    else{
                        //this.setTextFill(Color.BLACK);
                        setStyle("-fx-control-inner-background: derive(-fx-base,80%);");
                    }


                }
            }
        public FlowPane createText(Duel duel){
            FlowPane flowPane = new FlowPane();
            if(duel.getWinner().equals(duel.getPlayer1())){
                Text text = new Text(duel.getPlayer1().getNick());
                text.setStyle("-fx-fill: green;-fx-font-weight: bold;");
                flowPane.getChildren().add(text);
                flowPane.getChildren().add(new Text(" vs "+duel.getPlayer2().getNick()));
            }
            else{
                flowPane.getChildren().add(new Text(duel.getPlayer1().getNick()+" vs "));
                Text text = new Text(duel.getPlayer2().getNick());
                text.setStyle("-fx-fill: green;-fx-font-weight: bold;");
                flowPane.getChildren().add(text);

            }
            flowPane.getChildren().add(new Text("    "+duel.getWinReason()));
            return flowPane;
        }
    }
    @FXML
    private void randCheckBoxClicked(){
        if(randCheckBox.isSelected()){
            sizeSlider.setDisable(true);
        } else {
            sizeSlider.setDisable(false);
        }
    }
    public ProgressIndicator getArenaProgressIndicator() {
        return arenaProgressIndicator;
    }
}
