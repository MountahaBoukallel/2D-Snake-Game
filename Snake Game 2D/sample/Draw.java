package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Draw {
    // drawShapes() aims to draw and design everything that appears on the screen
    static void drawShapes(Main main, GraphicsContext gc) {
        // هنا قمنا بتحديد مكان وجود الأفعى في كل مرة يقوم المستخدم ببدأ اللعبة من جديد
        if (main.moves == 0) {
            main.boardX[2] = 40;
            main.boardX[1] = 60;
            main.boardX[0] = 80;

            main.boardY[2] = 100;
            main.boardY[1] = 100;
            main.boardY[0] = 100;

            main.scoreReverseCounter = 100;
            main.timeline.play();
        }

        // Makes the current score a high score in case it's higher than the old high score
        if (main.totalScore > main.bestScore) {
            main.bestScore = main.totalScore;
        }

        // Created the board "color black"
        gc.setFill(Color.rgb(12, 12, 12));
        gc.fillRect(0, 0, 800, 600);

        // We created the border
        // each square is 13px with 5px margin b
        gc.setFill(Color.color(0.2, 0.2, 0.2));
        for (int i = 5; i <= 600; i += 20) {
            for (int j = 5; j <= 600; j += 20) {
                gc.fillRect(i, j, 15, 15);
            }
        }

        // creating a background
        gc.setFill(Color.BLACK);
        gc.fillRect(20, 20, 560, 560);

        // Writing design
        gc.setFill(Color.WHITESMOKE);
        gc.setFont(Font.font("Serif", FontWeight.BOLD, 26));
        gc.fillText("Snake Game", 630, 30);

        gc.setFill(Color.LIGHTGRAY);


        gc.setFont(Font.font("Serif", FontWeight.NORMAL, 18));

        // Total score
        gc.fillText("Total Score", 630, 100);
        gc.fillRect(630, 120, 140, 30);
        gc.setFill(Color.BLACK);
        gc.fillRect(630, 120, 140, 30);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(main.totalScore + "", 630 + (142 - new Text(main.totalScore + "").getLayoutBounds().getWidth()) / 2, 142);


        // Best Score
        gc.fillText("Best Score", 630, 200);
        gc.fillRect(630, 220, 140, 30);
        gc.setFill(Color.BLACK);
        gc.fillRect(630, 220, 140, 30);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(main.bestScore + "", 630 + (142 - new Text(main.bestScore + "").getLayoutBounds().getWidth()) / 2, 242);


        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.fillText("Controls", 630, 400);

        // Controls
        gc.setFill(Color.LIGHTSTEELBLUE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.fillText("Pause / Start : Space", 640, 425);
        gc.fillText("Move Up : Arrow Up", 640, 450);
        gc.fillText("Move Down : Arrow Down", 640, 475);
        gc.fillText("Move Left : Arrow Left", 640, 500);
        gc.fillText("Move Right : Arrow Right", 640, 525);

        // The snake starts from the right
        gc.drawImage(main.lookToRightImage, main.boardX[0], main.boardY[0]);

        // We clear the old snake to get a new one
        main.snake.clear();

        // a loop that draws the snake each 0.1s
        for (int i = 0; i < main.lengthOfSnake; i++) {
            if (i == 0 && main.left) {
                gc.drawImage(main.lookToLeftImage, main.boardX[i], main.boardY[i]);
            } else if (i == 0 && main.right) {
                gc.drawImage(main.lookToRightImage, main.boardX[i], main.boardY[i]);
            } else if (i == 0 && main.up) {
                gc.drawImage(main.lookToUpImage, main.boardX[i], main.boardY[i]);
            } else if (i == 0 && main.down) {
                gc.drawImage(main.lookToDownImage, main.boardX[i], main.boardY[i]);
            } else if (i != 0) {
                gc.drawImage(main.snakeBodyImage, main.boardX[i], main.boardY[i]);
            }

            // Save the current position of the snake
            main.snake.add(new Position(main.boardX[i], main.boardY[i]));
        }


        // تقل بشكل تدريجي و أدنى قيمة ممكن أن تصل إليها هي 10 scoreReverseCounter هنا جعلنا قيمة العداد
        if (main.scoreReverseCounter != 5) {
            main.scoreReverseCounter--;
        }
        // check if the snake's head touches the body
        for (int i = 1; i < main.lengthOfSnake; i++) {

            if (main.boardX[i] == main.boardX[0] && main.boardY[i] == main.boardY[0]) {
                if (main.right)
                    gc.drawImage(main.lookToRightImage, main.boardX[1], main.boardY[1]);

                else if (main.left)
                    gc.drawImage(main.lookToLeftImage, main.boardX[1], main.boardY[1]);

                else if (main.up)
                    gc.drawImage(main.lookToUpImage, main.boardX[1], main.boardY[1]);

                else if (main.down)
                    gc.drawImage(main.lookToDownImage, main.boardX[1], main.boardY[1]);


                // we made it's value true to show that the player has lost :(
                main.isGameOver = true;

                // the snake stops from moving using timeline.stop()
                main.timeline.stop();

                // show Game Over on the screen
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 50));
                gc.fillText("Game Over", (600 - 264) / 2, 300);

                // تحته Press Space To Restart و سيتم إظهار النص
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                gc.fillText("Your Score is: " + main.totalScore, (600 - 180) / 2, 330);
                gc.fillText("Press Space To Restart", (600 - 224) / 2, 360);

                // this function saves the final score
                BestScore.writeBestScoreInTheFile(main.bestScore, main.totalScore);
            }
        }

        // if the snake touches the food , it'll disappear we get higher points for the player
        if ((main.fruitXPos[main.xPos] == main.boardX[0]) && main.fruitYPos[main.yPos] == main.boardY[0]) {
            main.totalScore += main.scoreReverseCounter;
            main.scoreReverseCounter = 100;
            main.fruitEaten++;
            main.lengthOfSnake++;
        }

        // make sure the food won't appear near the snake
        for (int i = 0; i < main.snake.size(); i++) {
            // In case the food showed on top of the snake body its gonna relocate.
            if (main.snake.get(i).x == main.fruitXPos[main.xPos] && main.snake.get(i).y == main.fruitYPos[main.yPos]) {
                main.xPos = main.random.nextInt(27);
                main.yPos = main.random.nextInt(27);
            }
        }

        // the food appear far from the snake
        gc.drawImage(main.fruitImage, main.fruitXPos[main.xPos], main.fruitYPos[main.yPos]);

        if (main.right) {
            if (main.lengthOfSnake - 1 + 1 >= 0)
                System.arraycopy(main.boardY, 0, main.boardY, 1, main.lengthOfSnake - 1 + 1);

            for (int i = main.lengthOfSnake; i >= 0; i--) {
                if (i == 0)
                    main.boardX[i] = main.boardX[i] + 20;
                else
                    main.boardX[i] = main.boardX[i - 1];


                if (main.boardX[i] > 560)
                    main.boardX[i] = 20;
            }
        } else if (main.left) {
            if (main.lengthOfSnake - 1 + 1 >= 0)
                System.arraycopy(main.boardY, 0, main.boardY, 1, main.lengthOfSnake - 1 + 1);

            for (int i = main.lengthOfSnake; i >= 0; i--) {
                if (i == 0)
                    main.boardX[i] = main.boardX[i] - 20;

                else
                    main.boardX[i] = main.boardX[i - 1];

                if (main.boardX[i] < 20)
                    main.boardX[i] = 560;
            }
        } else if (main.up) {
            if (main.lengthOfSnake - 1 + 1 >= 0)
                System.arraycopy(main.boardX, 0, main.boardX, 1, main.lengthOfSnake - 1 + 1);

            for (int i = main.lengthOfSnake; i >= 0; i--) {
                if (i == 0)
                    main.boardY[i] = main.boardY[i] - 20;
                else
                    main.boardY[i] = main.boardY[i - 1];

                if (main.boardY[i] < 20)
                    main.boardY[i] = 560;
            }
        } else if (main.down) {
            if (main.lengthOfSnake - 1 + 1 >= 0)
                System.arraycopy(main.boardX, 0, main.boardX, 1, main.lengthOfSnake - 1 + 1);

            for (int i = main.lengthOfSnake; i >= 0; i--) {
                if (i == 0)
                    main.boardY[i] = main.boardY[i] + 20;
                else
                    main.boardY[i] = main.boardY[i - 1];

                if (main.boardY[i] > 560)
                    main.boardY[i] = 20;
            }
        }

    }
}
