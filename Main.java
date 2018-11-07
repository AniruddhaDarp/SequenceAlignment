import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

enum Alignment {
    GLOBAL, LOCAL, DOVETAIL;
}

public class Main {
    public static void main(String args[]) {
        Map<Integer, List<String[]>> scoreMap = new TreeMap<>();
        List<String> querySequences = new ArrayList<>();
        List<String> dbSequences = new ArrayList<>();
        int[][] scoringMatrix;
        String alphabet;
        Alignment alignment = Alignment.GLOBAL;
        int inDelPenalty = -3;

        querySequences = getSequences("query.txt");
        dbSequences = getSequences("database.txt");
        alphabet = getAlphabet("alphabet.txt");
        scoringMatrix = getScoringMatrix("scoringmatrix.txt", alphabet);
        int count = 1;

        if(alignment == Alignment.GLOBAL) {
            for(String querySequence : querySequences) {
                for(String dbSequence : dbSequences) {
                    GlobalAlignment globalAlignment = new GlobalAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = globalAlignment.generateMatrix(alphabet, scoringMatrix);

//                    System.out.println("Distance Matrix: ");
//                    for (int i = 0; i < distanceMatrix.length; i++) {
//                        for (int j = 0; j < distanceMatrix[0].length; j++) {
//                            System.out.print("\t" + distanceMatrix[i][j]);
//                        }
//                        System.out.print("\n");
//                    }

                    int score = globalAlignment.getScore(distanceMatrix);
//                    System.out.println("Score = " + score);

                    String[] sequenceAlignment = new String[3];
                    sequenceAlignment[0] = querySequence;
                    sequenceAlignment[1] = dbSequence;
                    sequenceAlignment[2] = globalAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

//                    System.out.println("Alignment = " + sequenceAlignment[2]);

                    if(scoreMap.containsKey(score)) {
                        List<String[]> newList = scoreMap.get(score);
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }

                    else {
                        List<String[]> newList = new ArrayList<>();
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }
                    System.out.println("Processed sequence " + count);
                    count++;
                }
            }
        } else if(alignment == Alignment.LOCAL) {
            for (String querySequence : querySequences) {
                for (String dbSequence : dbSequences) {
                    LocalAlignment localAlignment = new LocalAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = localAlignment.generateMatrix(alphabet, scoringMatrix);
                    int score = localAlignment.getScore(distanceMatrix);
                    String[] sequenceAlignment = new String[3];
                    sequenceAlignment[0] = querySequence;
                    sequenceAlignment[1] = dbSequence;
                    sequenceAlignment[2] = localAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

                    if(scoreMap.containsKey(score)) {
                        List<String[]> newList = scoreMap.get(score);
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }

                    else {
                        List<String[]> newList = new ArrayList<>();
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }
                }
            }
        } else {
            for (String querySequence : querySequences) {
                for (String dbSequence : dbSequences) {
                    DovetailAlignment dovetailAlignment = new DovetailAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = dovetailAlignment.generateMatrix(alphabet, scoringMatrix);
                    int score = dovetailAlignment.getScore(distanceMatrix);
                    String[] sequenceAlignment = new String[3];
                    sequenceAlignment[0] = querySequence;
                    sequenceAlignment[1] = dbSequence;
                    sequenceAlignment[2] = dovetailAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

                    if(scoreMap.containsKey(score)) {
                        List<String[]> newList = scoreMap.get(score);
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }

                    else {
                        List<String[]> newList = new ArrayList<>();
                        newList.add(sequenceAlignment);
                        scoreMap.put(score, newList);
                    }
                }
            }
        }

        //Generate Graphs, Get Time

//        for (int i = 0; i < querySequences.size(); i++) {
//            System.out.print("\nQuery sequence " + (i + 1) + ": ");
//            System.out.print(querySequences.get(i));
//        }
//
//        for (int i = 0; i < dbSequences.size(); i++) {
//            System.out.print("\nDB sequence " + (i + 1) + ": ");
//            System.out.print(dbSequences.get(i));
//        }
    }

    public static List<String> getSequences(String fileName) {
        List<String> sequences = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == '>') {
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader("scoringmatrix.txt"));
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
