package Controll;

import Model.Arena;
import Model.Board;
import Tools.DialogReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import Tools.LogWriter;

import java.util.List;

public class MainController {
    @FXML
    private AnchorPane duelBar;
    @FXML
    private CheckBox controlCheckBox;

    @FXML
    public AnchorPane mainPane;
    @FXML
    private Button btnChangeBoardSize;
    @FXML
    private Label sizeText;
    @FXML
    private Canvas canvas;
    @FXML
    private TextArea tournamentText;
    @FXML
    private Button btnChoiceDuel;
    @FXML
    private Button btnChoiceTournament;


    @FXML
    public DuelBarController duelBarController;
    @FXML
    private TournamentBarController tournamentBarController;

    @FXML
    private Button btnNextMove;

    @FXML
    private Button acceptButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button fillBoardButton;
    @FXML
    private Button startButton;
    @FXML
    private Button randomButton;
    @FXML
    private CheckBox programUserCheckBox;

    private Board board;
    private Arena arena;
    private int boardSize = 5;
    private boolean isControlled = false;
    private String humanMove="";
    private boolean humanTurn=false;
    private int humanRectAngle=1;

    @FXML
    public void initialize(){
        board = new Board(Integer.parseInt(btnChangeBoardSize.getText()), canvas);
        board.draw();
        tournamentText.setEditable(false);


    }
    public void fillBoardButtonPressed(){
        setButtonsDisable(true);

        canvas.setOnMouseClicked(event -> {
            double x = event.getX(), y = event.getY();
            board.setStartPoint(x,y);
        });
        canvas.setOnMouseMoved(event -> {
            double x = event.getX(), y = event.getY();
            boolean hoverRec = board.hoverRect(x,y,-1,false);
            System.out.println(hoverRec);
        });
        canvas.setOnMouseExited(event -> {
            board.clean();
            board.draw();

        });

    }
    public void acceptButtonPressed(){
        setButtonsDisable(false);
        disableUserInput();
    }
    public void clearButtonPressed(){
        board.clearFromPoints();
    }
    public void randomButtonPressed(){
        board.setRandomStartPoints();
        acceptButtonPressed();
    }
    public void programUserCheckBox(){
        controlCheckBox.setSelected(programUserCheckBox.isSelected());
        controlCheckBox.setDisable(programUserCheckBox.isSelected());
        duelBarController.setDisablePlayer2Dir(programUserCheckBox.isSelected());
    }

    public void changeSizeButtonPressed() {
        int boardSize = DialogReader.readNumberFromDialog("Board size", "Set board size", 5, 3, 50);
        if (boardSize >= 3 && boardSize <= 50) {
            this.boardSize = boardSize;
            btnChangeBoardSize.setText("" + boardSize);
            board = new Board(Integer.parseInt(btnChangeBoardSize.getText()), canvas);
            board.draw();
        }
    }

    public void bntStartPressed(){
        if(startButton.getText() == "Stop"){
            arena.setForceStop(true);
            return;
        }
        setDisableLeftBar(true);

        System.out.println(tournamentText);
        board.clean();
        tournamentText.setText("Progressing");
        startButton.setText("Stop");
        controlCheckBox.setDisable(true);
        programUserCheckBox.setDisable(true);

        if(controlCheckBox.isSelected() && duelBar.isVisible()) {
            btnNextMove.setDisable(false);
            isControlled = true;
        } else {
            btnNextMove.setDisable(true);
            isControlled = false;
        }
        if(programUserCheckBox.isSelected()) {
            btnNextMove.setDisable(true);
            humanTurn = true;

            canvas.setOnMouseMoved(event -> {
                if (humanTurn) {
                    double xx = event.getX(), yy = event.getY();
                    board.hoverRect(xx, yy, humanRectAngle, false);
                }
            });
            canvas.setOnMouseExited(event -> {
                if (humanTurn)
                    board.cleanHover();

            });
        }
        canvas.setOnMouseClicked(event -> {

            if(event.getButton().equals(MouseButton.SECONDARY ) && humanTurn){
                humanRectAngle=(humanRectAngle+1)%2;

                board.hoverRect(event.getX(),event.getY(),humanRectAngle,true);
                board.draw();

            }else if(event.getButton().equals(MouseButton.PRIMARY)){
                handleUserInput(event.getX(),event.getY());
            }

        });
        arena = new Arena(this,programUserCheckBox.isSelected());
        arena.start();
    }
    public void handleUserInput(double posX, double posY){
        if(humanTurn){
            int x = board.countPosition(posX), y = board.countPosition(posY);
            humanMove = board.getHovers();
            System.out.println("humanMove: " + humanMove);
            if(humanMove != null ){
                btnNextMove.setDisable(false);
                doNextMove();
            }
        }
        // do nonhuman move immediately
        if(!humanTurn){
            board.cleanHover();
            doNextMove();
        }
    }

    public void disableUserInput(){
        canvas.setOnMouseClicked(e->{ });
        canvas.setOnMouseExited(e->{ });
        canvas.setOnMouseMoved(e->{ });
    }

    private void switchBars(BarController fromBar, Button fromButton, BarController toBar, Button toButton){
        fromBar.setVisible(false);
        toBar.setVisible(true);
        /* button shadow */
        fromButton.getStyleClass().removeAll("doubleShadowed");
        fromButton.getStyleClass().addAll("doubleShadowed");
        toButton.getStyleClass().removeAll("doubleShadowed");
    }

    public void tournamentChoicePressed(){
        switchBars(duelBarController, btnChoiceDuel, tournamentBarController, btnChoiceTournament);
        controlCheckBox.setDisable(true);
        programUserCheckBox.setSelected(false);
        programUserCheckBox.setDisable(true);
    }

    public void duelChoicePressed(){
        switchBars(tournamentBarController, btnChoiceTournament, duelBarController, btnChoiceDuel);
        controlCheckBox.setDisable(false);
        programUserCheckBox.setDisable(false);
    }
    public void forceEnd(String msg){
        disableUserInput();
        setDisableLeftBar(false);
        Platform.runLater(() -> startButton.setText("Start"));

        btnNextMove.setDisable(true);
        if(duelBar.isVisible()){
            if(!programUserCheckBox.isSelected())
                controlCheckBox.setDisable(false);
            programUserCheckBox.setDisable(false);
        }
        Platform.runLater(() -> tournamentText.setText(msg));
    }

    private void setDisableLeftBar(boolean disable){
        duelBarController.setDisableAll(disable);
        tournamentBarController.setDisableAll(disable);
        btnChoiceDuel.setDisable(disable);
        btnChoiceTournament.setDisable(disable);
        btnChangeBoardSize.setDisable(disable);
        fillBoardButton.setDisable(disable);

        if(programUserCheckBox.isSelected()){
            duelBarController.setDisablePlayer2Dir(true);
            controlCheckBox.setDisable(true);
        }
    }
    public void tournamentEnded(){
        disableUserInput();
        setDisableLeftBar(false);
        Platform.runLater(() -> startButton.setText("Start"));

        btnNextMove.setDisable(true);
        if(duelBar.isVisible()){
            if(!programUserCheckBox.isSelected())
                controlCheckBox.setDisable(false);
            programUserCheckBox.setDisable(false);
        }

        System.out.println("Arena has just ended");
        Platform.runLater(() -> tournamentText.setText(arena.buildScoreTable()));

        // TODO check if Log is working conrrectly here
        LogWriter log = new LogWriter("tournamentLog");
        log.writeTournamentList(arena.getScoreList());
    }
    @FXML
    public void doNextMove(){

        arena.doNextMove(humanMove);

        if(programUserCheckBox.isSelected()) {
            humanTurn = !humanTurn;
            if(humanTurn){
                btnNextMove.setDisable(true);
            }
        }

        System.out.println("MainController: received doNextMove");
    }
    public Board getBoard(){
        return board;
    }
    public List getDirectories(){
        BarController barController = duelBarController.isVisible()? duelBarController : tournamentBarController;
        return barController.getDirectories();
    }
    public boolean isControlled(){
        return isControlled;
    }

    private void setButtonsDisable(boolean disable){
        startButton.setDisable(disable);
        btnChangeBoardSize.setDisable(disable);
        fillBoardButton.setDisable(disable);
        clearButton.setDisable(!disable);
        acceptButton.setDisable(!disable);
        randomButton.setDisable(!disable);

    }
}
