package sample;

import java.util.LinkedList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    // created this array to save the position
    final int[] boardX = new int[484];
    final int[] boardY = new int[484];

    // we used this list to store the position of the snake so the "food" won't show up there
    LinkedList<Position> snake = new LinkedList();

    // the directions the snake can take
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    // Draw the face of the snake
    Image lookToRightImage = new Image(getClass().getResourceAsStream("/images/face-look-right.jpg"));
    Image lookToLeftImage = new Image(getClass().getResourceAsStream("/images/face-look-left.jpg"));
    Image lookToUpImage = new Image(getClass().getResourceAsStream("/images/face-look-up.jpg"));
    Image lookToDownImage = new Image(getClass().getResourceAsStream("/images/face-look-down.jpg"));

    // Draw the body of the snake
    Image snakeBodyImage = new Image(getClass().getResourceAsStream("/images/body.png"));

    // Draw the food of the snake
    Image fruitImage = new Image(getClass().getResourceAsStream("/images/fruit.png"));

    // The initial length of the snake
    int lengthOfSnake = 3;

    // This array shows
    int[] fruitXPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460,480,500,520,540,560};
    int[] fruitYPos = {20, 40, 60, 80, 100, 120, 140, 160, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420, 440, 460,480,500,520,540,560};


    // we used timeline so we can control the movement of the snake
    Timeline timeline = new Timeline();

    // check if the snake moves {Y / N}
    int moves = 0;

    // calculate the player's score
    // initial score is 0 then it changes as the snake eats food
    int totalScore = 0;
    int fruitEaten = 0;
    int scoreReverseCounter = 100;

    // if the player achieves high score he'll be known as best player
    // then we gt the readBestScore() function
    int bestScore = BestScore.readBestScoreFromTheFile();

    // We use random to generate the snake's food
    Random random = new Random();

    // Locate where The snake is gonna appear when we lunch the game
    int xPos = random.nextInt(27);
    int yPos = random.nextInt(15);

    // We use this to know if the player has won or lost
    boolean isGameOver = false;
    @Override
    public void start(Stage stage) {

        // canvas is a container we can draw over
        Canvas canvas = new Canvas(800, 600);

        //we created this starting from the GraphicContext class
        // its based on canvas so we can use it to draw on a canvas

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        stage.setTitle("Snake 2D");

        stage.setScene(scene);

        // we showed the window
        stage.show();

        stage.setResizable(false);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);


        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1), (ActionEvent event) -> Draw.drawShapes(this, gc)));



        // we called timeline and passed INDEFINITE as an argument to setCycleCount() to make sure it won't shutdown when it starts
        timeline.setCycleCount(Timeline.INDEFINITE);

        // timeline لتشغيل الكائن play() هنا قمنا باستدعاء الدالة
        timeline.play();

        // we override the keyPressed() to determine what direction it'll go to
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {

            if (null != e.getCode())
            {
                switch (e.getCode())
                {
                    // what happens when we press the space key
                    case SPACE:
                        // stop the game temporarily in case the screen is still on
                        if (timeline.getStatus() == Timeline.Status.RUNNING && !isGameOver )
                        {
                            timeline.stop();
                        }
                        // restart the game if it got stopped
                        else if (timeline.getStatus() != Timeline.Status.RUNNING && !isGameOver)
                        {
                            timeline.play();
                        }
                        //  we restart the game if the player has lost
                        else if (timeline.getStatus() != Timeline.Status.RUNNING && isGameOver)
                        {
                            isGameOver = false;
                            timeline.play();
                            moves = 0;
                            totalScore = 0;
                            fruitEaten = 0;
                            lengthOfSnake = 3;
                            right = true;
                            left = false;
                            xPos = random.nextInt(22);
                            yPos = 5 + random.nextInt(17);
                        }
                        break;

                    // what's gonna happen if the player presses right?
                    case RIGHT:
                        // if the snake isn't going left it would be directed right
                        moves++;
                        right = true;
                        if (!left) {
                            right = true;
                        }
                        else
                        {
                            right = false;
                            left = true;
                        }
                        up = false;
                        down = false;
                        break;

                    // what's gonna happen if the player presses left?
                    case LEFT:
                        // if the snake isn't going right it would be directed left
                        moves++;
                        left = true;
                        if (!right)
                        {
                            left = true;
                        }
                        else
                        {
                            left = false;
                            right = true;
                        }
                        up = false;
                        down = false;
                        break;

                    // what's gonna happen if the player presses up?
                    case UP:
                        // if the snake isn't going down it would be directed up
                        moves++;
                        up = true;
                        if (!down)
                        {
                            up = true;
                        }
                        else {
                            up = false;
                            down = true;
                        }
                        left = false;
                        right = false;
                        break;

                    // what's gonna happen if the player presses down?
                    case DOWN:
                        // if the snake isn't going up it would be directed down
                        moves++;
                        down = true;
                        if (!up)
                        {
                            down = true;
                        }
                        else {
                            up = true;
                            down = false;
                        }
                        left = false;
                        right = false;
                        break;
                }
            }
        });
    }

    // Here the app is gonna start
    public static void main(String[] args) {
        launch(args);
    }

}
