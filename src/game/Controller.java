package game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Handles the users Key Inputs
 */
public class Controller implements Initializable, EventHandler<KeyEvent> {

    static final Object lock = new Object();
    @FXML
    private GridPane grid;

    @FXML
    private Text score;

    @FXML
    private GridPane next;

    private Model m;

    Thread thread;

    /**
     * Called after the App is started
     * calls setup
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setup();
    }

    /**
     * Initializes the Model and Starts the game
     * by calling refresh, redraw and the models drop and new PuyoPair
     */
    public void setup(){
        m = new Model();
        initFileds();
        m.newPuyoPair();
        m.drop();
        refresh();
        redraw();
    }

    /**
     * initializes the tread responsible for updating the gameboard, by repeatedly calling the redraw method
     */
    public void refresh(){
        this.thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        redraw();
                    }
                };

                while (!m.isLoose()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }

        });
        // don't let thread prevent JVM shutdown
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Creates the initial gameboard with Fields and sets it as the Models map
     */
    private void initFileds() {
        Field[][] map = new Field[this.m.getX()][this.m.getY()];
        Field temp;
        for (int x = 0; x < this.m.getX(); x++){
            for (int y = 0; y < this.m.getY(); y++){
                temp = new Field(x,y);
                this.grid.add(temp,x,y);
                map[x][y] = temp;
                this.m.setMap(map);
            }
        }
    }

    /**
     * First checks if the player has lost the game
     * Displays the next two Puyos
     * Draws the gameboard according to the models map
     */
    public void redraw(){
        if(m.isLoose())
            loose();
        setNext();
        this.score.setText("Score: "+m.getScore());
        grid.getChildren().clear();
        for (int x = 0; x < this.m.getX(); x++){
            for (int y = 0; y < this.m.getY(); y++){
                Field temp = this.m.getMap()[x][y];
                this.grid.add(temp,x,y);
            }
        }
        this.m.checkBelow();
    }

    /**
     * Sets the Next two puyos into the next grid
     */
    public void setNext(){
        this.next.getChildren().clear();
        this.next.add(m.getNext().getPuyo1(),0,0);
        this.next.add(m.getNext().getPuyo2(),1,0);
    }

    /**
     * Called when Game is lost or closed
     * Sets the models loose to true, prints the score & loose message and joins the thread
     */
    public void loose() {
        this.m.setLoose(true);
        this.score.setText("Score: "+m.getScore()+" You've Lost!");
        try {
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Players Inputs
     * and calls the corresponding Methods from the model
     * @param event
     */
    @Override
    public void handle(KeyEvent event) {
        if (!m.isLoose()) {
            synchronized (Controller.lock) {
                switch (event.getCode()) {
                    case LEFT:
                        if (m.checkLeft()) {
                            m.move(-1, 0);
                            redraw();
                        }
                        break;
                    case RIGHT:
                        if (m.checkRight()) {
                            m.move(1, 0);
                            redraw();
                        }
                        break;
                    case DOWN:
                        m.move(0, 1);
                        redraw();
                        break;
                    case E:
                        if (this.m.getCurPuyo().getPuyo1().getY() > 1) {
                            m.turn(false);
                            redraw();
                        }
                        break;
                    case R:
                        if (this.m.getCurPuyo().getPuyo1().getY() > 1) {
                            m.turn(true);
                            redraw();
                        }
                        break;
                }
            }
        }else
            loose();
    }
}
