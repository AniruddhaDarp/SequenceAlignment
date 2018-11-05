import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GlobalAlignment {
    private String querySequence;
    private String dbSequence;
    private int inDelPenalty;

    public GlobalAlignment(String querySequence, String dbSequence, int inDelPenalty) {
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.inDelPenalty = inDelPenalty;
    }

    public int[][] generateMatrix() {
        int[][] distanceMatrix = new int[this.querySequence.length() + 1][this.dbSequence.length() + 1];
        StringBuilder alphabet = new StringBuilder();
        int[][] scoringMatrix;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("alphabet.txt"));
            String line;

            while((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (Character.isLetter(line.charAt(i)))
                        alphabet.append(line.charAt(i));
                }
            }
            bufferedReader.close();

            bufferedReader = new BufferedReader(new FileReader("scoringmatrix.txt"));
            scoringMatrix = new int[alphabet.length()][alphabet.length()];
            int rowCount = 0;

            while((line = bufferedReader.readLine()) != null) {
                int colCount = 0;

                for (int i = 0; i < line.length(); i++) {
                    if (Character.isDigit(line.charAt(i))) {
                        scoringMatrix[rowCount][colCount] = Integer.parseInt("" + line.charAt(i));
                        colCount++;
                    }
                }
                rowCount++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        

        return distanceMatrix;
    }

    public int getScore(int[][] matrix) {
        return 0;
    }

    public String getAlignment(int[][] matrix) {
        return "";
    }
}
