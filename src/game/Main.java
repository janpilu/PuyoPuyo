package game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class Main extends Application implements EventHandler<WindowEvent> {

    Controller c;
    MediaPlayer mediaPlayer;
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        this.c = loader.getController();
        primaryStage.setTitle("PuyoPuyo");
        Scene scene = new Scene(root, 300, 550);
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(c);
        primaryStage.setOnCloseRequest(this);

        String musicFile = "src/game/music.mp3";     // For example

        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(WindowEvent event) {
        c.loose();
        primaryStage.close();
    }
}
