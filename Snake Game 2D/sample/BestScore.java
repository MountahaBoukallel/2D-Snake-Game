package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BestScore {
    // This function saves the high score to an external .txt file :)
    static void writeBestScoreInTheFile(int bestScore, int totalScore) {
        if (totalScore >= bestScore) {
            try {
                FileOutputStream fos = new FileOutputStream("./snake-game-best-score.txt");
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                osw.write(bestScore + "");
                osw.flush();
                osw.close();
            } catch (IOException e) {
            }
        }
    }

    // This function reads the high score from the external file if it doesn't exist
    // it'll create it this happens when the player lunches the game for the first time ever.
    static int readBestScoreFromTheFile() {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("./snake-game-best-score.txt"), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);

            String str = "";
            int c;
            while ((c = br.read()) != -1) {
                if (Character.isDigit(c)) {
                    str += (char) c;
                }
            }
            if (str.equals("")) {
                str = "0";
            }

            br.close();
            return Integer.parseInt(str);
        } catch (IOException e) {
        }
        return 0;
    }
}
