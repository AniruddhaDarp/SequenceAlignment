import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<String> getSequences(String fileName) {
        List<String> sequences = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = bufferedReader.readLine()) != null) {
                if (line.contains(">")) {
                    if(sb.length() == 0)
                        continue;

                    else {
                        sequences.add(sb.toString().toLowerCase().trim());
                        sb = new StringBuilder();
                        continue;
                    }
                }

                else
                    sb.append(line);
            }
            sequences.add(sb.toString().toLowerCase().trim());
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sequences;
    }

    public static List<String> getIds(String fileName) {
        List<String> ids = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = bufferedReader.readLine()) != null) {
                if (!line.contains(">")) {
                    continue;
                } else {
                    int i = 0;
                    while(!Character.isDigit(line.charAt(i))) {
                        i++;
                    }

                    while(Character.isDigit(line.charAt(i))) {
                        sb.append(line.charAt(i));
                        i++;
                    }

                    ids.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public static String getAlphabet(String fileName) {
        StringBuilder alphabet = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (Character.isLetter(line.charAt(i)))
                        alphabet.append(line.charAt(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alphabet.toString().toLowerCase().trim();
    }

    public static int[][] getScoringMatrix(String fileName, String alphabet) {
        int[][] scoringMatrix = new int[alphabet.length()][alphabet.length()];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            int rowCount = 0;

            while((line = bufferedReader.readLine()) != null) {
                int colCount = 0;

                for (int i = 0; i < line.length(); i++) {
                    if (Character.isDigit(line.charAt(i))) {
                        scoringMatrix[rowCount][colCount] = Integer.parseInt("" + line.charAt(i));
                        if(i > 0 && line.charAt(i - 1) == '-')
                            scoringMatrix[rowCount][colCount] = -1 * scoringMatrix[rowCount][colCount];
                        colCount++;
                    }
                }
                rowCount++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoringMatrix;
    }
}
