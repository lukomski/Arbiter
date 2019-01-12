package Controll;

import Model.Arena;
import Model.Board;
import Model.Duel;
import Tools.DialogReader;
import Tools.Guide;
import Tools.ScoreResult;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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
import java.util.Random;

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
    @FXML
    private Label pressNText;
    @FXML
    private Label theEndText;

    private Guide guide = null;
    private Arena arena;
    private int boardSize = 5;
    private int humanRectAngle=1;
    private File directory;
    private Board board;
    private int size;
    private boolean randSizeBoard;

    @FXML
    public void initialize(){
        startButton.setDisable(true);
        sizeSlider.setDisable(true);
        initScoreTable();
        initDuelList();
    }

    @FXML
    private void btnDirectoryPressed() {
        directory = DialogReader.readDirectoryFromDialog("Choose Arena directory", mainPane);
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
        if(startButton.getText().equals("Stop")){
            arena.interrupt();
            startButton.setText("Start");
            setDisableLeftBarItems(false);

        } else {
            setDisableLeftBarItems(true);
            arenaProgressIndicator.setProgress(0);
            leftTabPane.getSelectionModel().select(leftTabPane.getTabs().get(0));
            startButton.setText("Stop");
            if(randCheckBox.isSelected()){
                Random random = new Random();
                int max = (int) sizeSlider.getMax();
                int min = (int) sizeSlider.getMin();
                int range = max - min;
                // rand in range [0, range]
                int randNumber = random.nextInt(range + 1) - 1;
                randNumber += min;
                size = randNumber;
            } else {
                size = (int) sizeSlider.getValue();
            }
            System.out.println("MainContriller: size = " + size);
            board = new Board(size, canvas);
            board.setRandomStartPoints();
            board.draw();

            arena = new Arena(this);
            clearDuelList();
            arena.start();
        }
    }
    public void arenaEnded(){
        Platform.runLater(() -> startButton.setText("Start"));
        leftTabPane.getSelectionModel().select(leftTabPane.getTabs().get(1));
        // disable
        setDisableLeftBarItems(false);
        if(randCheckBox.isSelected()) {
            sizeSlider.setDisable(true);
        }
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
                theEndText.setVisible(false);
                pressNText.setVisible(true);
                mainTabPane.getSelectionModel().select(mainTabPane.getTabs().get(1));
                board.clean();
                board.draw();
                Guide guide = new Guide(board, moves);

                mainPane.getScene().setOnKeyReleased(event -> {
                    if(event.getCode() == KeyCode.N || event.getCode() == KeyCode.RIGHT) {
                        pressNText.setVisible(false);
                        if(!guide.nextMove()){
                            theEndText.setVisible(true);
                        }
                    }
                });

            }
        });
        duelList.setCellFactory((Callback<ListView<Duel>, ListCell<Duel>>) param -> new ColoredCell());
    }
    public void initScoreTable(){
        TableColumn numberCol = new TableColumn("#");
        numberCol.setMinWidth(20);
        numberCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ScoreResult, ScoreResult>, ObservableValue<ScoreResult>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

        numberCol.setCellFactory(new Callback<TableColumn<ScoreResult, ScoreResult>, TableCell<ScoreResult, ScoreResult>>() {
            @Override public TableCell<ScoreResult, ScoreResult> call(TableColumn<ScoreResult, ScoreResult> param) {
                return new TableCell<ScoreResult, ScoreResult>() {
                    @Override protected void updateItem(ScoreResult item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex()+1+"");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        numberCol.setSortable(false);
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
        winsCol.setMinWidth(35);
        winsCol.setPrefWidth(35);
        winsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("wins"));

        TableColumn losesCol = new TableColumn("Loses");
        losesCol.setMinWidth(30);
        losesCol.setPrefWidth(35);
        losesCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("loses"));

        TableColumn disqualificationsCol = new TableColumn("Disqualifications");
        disqualificationsCol.setMinWidth(100);
        disqualificationsCol.setCellValueFactory( new PropertyValueFactory<ScoreResult, Integer>("disqualifications"));

        scoreTable.getColumns().addAll(numberCol,idCol,nameCol,nickCol, winsCol, losesCol,disqualificationsCol);
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
                    getStyleClass().add("coloredCellClass");
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
        public FlowPane createText(Duel duel) {
            String greenStyle = "-fx-fill: green;-fx-font-weight: bold;";
            String orangeStyle = "-fx-fill: #ff6600;";
            Text firstPlayer = new Text(duel.getPlayer1().getDirName());
            Text secondPlayer = new Text(duel.getPlayer2().getDirName());
            FlowPane flowPane = new FlowPane();
            if (duel.getWinner().equals(duel.getPlayer1())) {
                firstPlayer.setStyle(greenStyle);
                secondPlayer.setStyle(orangeStyle);
            } else {
                secondPlayer.setStyle(greenStyle);
                firstPlayer.setStyle(orangeStyle);
            }
            flowPane.getChildren().add(firstPlayer);
            flowPane.getChildren().add(new Text(" vs "));
            flowPane.getChildren().add(secondPlayer);
            if(!duel.getWinReason().equals("NORMAL WIN")) {
                flowPane.getChildren().add(new Text("    " + duel.getWinReason()));
            }
            flowPane.getStyleClass().add("flowPaneClass");
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
