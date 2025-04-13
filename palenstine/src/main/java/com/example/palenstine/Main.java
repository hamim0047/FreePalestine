package com.example.palenstine;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URISyntaxException;

public class Main extends Application {

    private int score = 0;
    private Text scoreText;
    private Timeline gameTimer;
    private Timeline moveTarget;
    private int timeLeft = 30;
    private Text timerText;
    private MediaPlayer backgroundMusic;

    private double targetVelocityX = 3;  // Horizontal speed
    private double targetVelocityY = 3;  // Vertical speed

    @Override
    public void start(Stage primaryStage) {
        playBackgroundMusic();
        showHomePage(primaryStage);
    }

    private void playBackgroundMusic() {
        try {
            File musicFile = new File(getClass().getResource("/background_music.mp3").toURI());
            Media music = new Media(musicFile.toURI().toString());
            backgroundMusic = new MediaPlayer(music);

            // Auto-repeat music
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);

            // If it stops unexpectedly, restart it
            backgroundMusic.setOnEndOfMedia(() -> {
                backgroundMusic.seek(Duration.ZERO);
                backgroundMusic.play();
            });

            backgroundMusic.setOnStopped(() -> {
                backgroundMusic.seek(Duration.ZERO);
                backgroundMusic.play();
            });

            backgroundMusic.play();

        } catch (URISyntaxException | NullPointerException e) {
            System.out.println("⚠️ Background music file not found!");
        }
    }


    private void showHomePage(Stage stage) {
        Pane root = new Pane();
        Image homeBackground = new Image(getClass().getResourceAsStream("/home.jpg"));
        ImageView homeBackgroundImageView = new ImageView(homeBackground);
        homeBackgroundImageView.setFitWidth(600);
        homeBackgroundImageView.setFitHeight(400);

        // Apply Gaussian Blur effect to the background image
        GaussianBlur blurEffect = new GaussianBlur(3); // Adjust blur radius as needed
        homeBackgroundImageView.setEffect(blurEffect);

        root.getChildren().add(homeBackgroundImageView); // Add background to root

        Text gameTitle = new Text("PALESTINE WILL BE FREE");
        gameTitle.setFont(Font.font("Arial", 34));
        gameTitle.setStyle("-fx-fill: red;");
        gameTitle.setLayoutX(100);
        gameTitle.setLayoutY(180);

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 18; -fx-padding: 12 24; -fx-background-radius: 10;");
        startButton.setLayoutX(250);
        startButton.setLayoutY(200);

        startButton.setOnAction(e -> showGamePage(stage));

        root.getChildren().addAll(gameTitle, startButton);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Free Palestine Game");
        stage.show();
    }

    private void showGamePage(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #111;");

        score = 0;

        scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font(24));
        scoreText.setStyle("-fx-fill: white;");
        scoreText.setX(20);
        scoreText.setY(40);

        timerText = new Text("Time: 30");
        timerText.setFont(Font.font(24));
        timerText.setStyle("-fx-fill: white;");
        timerText.setX(480);
        timerText.setY(40);

        // First target
        Image targetImage1 = new Image(getClass().getResourceAsStream("/target.png"));
        ImageView target1 = new ImageView(targetImage1);
        target1.setFitHeight(80);
        target1.setFitWidth(80);
        target1.setX(100);
        target1.setY(100);
        target1 .setOnMousePressed(e -> handleSlap(target1, root, e.getSceneX(), e.getSceneY()));


        // Second target
        Image targetImage2 = new Image(getClass().getResourceAsStream("/target2.png")); // Different image for second target
        ImageView target2 = new ImageView(targetImage2);
        target2.setFitHeight(70);
        target2.setFitWidth(70);
        target2.setX(300);
        target2.setY(100);
        target2.setOnMousePressed(e -> handleSlap(target2, root, e.getSceneX(), e.getSceneY()));


        // First target movement (ping pong movement)
        double[] target1DX = {2}; // Horizontal velocity
        double[] target1DY = {2}; // Vertical velocity

        moveTarget = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            // Move target1
            double newX = target1.getX() + target1DX[0];
            double newY = target1.getY() + target1DY[0];

            // If target1 hits the right or left side of the screen, reverse horizontal velocity
            if (newX >= 600 - target1.getFitWidth() || newX <= 0) {
                target1DX[0] = -target1DX[0];
            }

            // If target1 hits the top or bottom of the screen, reverse vertical velocity
            if (newY >= 400 - target1.getFitHeight() || newY <= 0) {
                target1DY[0] = -target1DY[0];
            }

            target1.setX(newX);
            target1.setY(newY);
        }));
        moveTarget.setCycleCount(Timeline.INDEFINITE);
        moveTarget.play();

        // Second target movement (ping pong movement)
        double[] target2DX = {2}; // Horizontal velocity
        double[] target2DY = {1.5}; // Vertical velocity

        Timeline moveTarget2 = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            // Move target2
            double newX = target2.getX() + target2DX[0];
            double newY = target2.getY() + target2DY[0];

            // If target2 hits the right or left side of the screen, reverse horizontal velocity
            if (newX >= 600 - target2.getFitWidth() || newX <= 0) {
                target2DX[0] = -target2DX[0];
            }

            // If target2 hits the top or bottom of the screen, reverse vertical velocity
            if (newY >= 400 - target2.getFitHeight() || newY <= 0) {
                target2DY[0] = -target2DY[0];
            }

            target2.setX(newX);
            target2.setY(newY);
        }));
        moveTarget2.setCycleCount(Timeline.INDEFINITE);
        moveTarget2.play();

        root.getChildren().addAll(scoreText, timerText, target1, target2);

        Scene gameScene = new Scene(root, 600, 400);
        stage.setScene(gameScene);
        stage.setTitle("Game Play");
        stage.show();

        // Timer countdown
        timeLeft = 30;
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerText.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                gameTimer.stop();
                moveTarget.stop();
                moveTarget2.stop();
                showGameOver(stage);
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void handleSlap(ImageView target, Pane root, double x, double y) {
        score++;
        scoreText.setText("Score: " + score);

        // 👣 Show Sandal Effect at click
        Image sandalImage = new Image(getClass().getResourceAsStream("/sandal.png")); // make sure the image exists
        ImageView sandalEffect = new ImageView(sandalImage);
        sandalEffect.setFitWidth(80);
        sandalEffect.setFitHeight(80);
        sandalEffect.setX(x - 40); // center the image
        sandalEffect.setY(y - 40);
        root.getChildren().add(sandalEffect);

        // Remove sandal effect after short delay
        Timeline removeEffect = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            root.getChildren().remove(sandalEffect);
        }));
        removeEffect.play();

        // 🎵 Play slap sound
        try {
            File slapFile = new File(getClass().getResource("/slap.mp3").toURI());
            Media slapSound = new Media(slapFile.toURI().toString());
            MediaPlayer slapPlayer = new MediaPlayer(slapSound);
            slapPlayer.play();
        } catch (Exception e) {
            System.out.println("⚠️ Slap sound not found or error playing it.");
        }

        // Reset target position
        target.setY(Math.random() * 300);
        target.setX(Math.random() * 500);
    }




    private void showGameOver(Stage stage) {
        Pane gameOverRoot = new Pane();
        gameOverRoot.setStyle("-fx-background-color: black;");

        Text gameOverText = new Text("Time's Up!");
        gameOverText.setFont(Font.font("Arial", 28));
        gameOverText.setStyle("-fx-fill: red;");
        gameOverText.setX(200);
        gameOverText.setY(100);

        Text finalScoreText = new Text("Your score: " + score);
        finalScoreText.setFont(Font.font("Arial", 24));
        finalScoreText.setStyle("-fx-fill: white;");
        finalScoreText.setX(200);
        finalScoreText.setY(150);

        Button restartButton = new Button("Restart");
        restartButton.setLayoutX(250);
        restartButton.setLayoutY(200);
        restartButton.setOnAction(e -> showHomePage(stage));

        gameOverRoot.getChildren().addAll(gameOverText, finalScoreText, restartButton);

        Scene gameOverScene = new Scene(gameOverRoot, 600, 400);
        stage.setScene(gameOverScene);
        stage.setTitle("Game Over");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
