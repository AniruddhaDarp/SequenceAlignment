import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

enum Alignment {
    GLOBAL, LOCAL, DOVETAIL;
}

public class Main {
    public static void main(String args[]) {
        int algorithm = 3;
        String queryFile = "query.txt";
        String dbFile = "database.txt";
        String alphabetFile = "alphabet.txt";
        String scoringMatrixFile = "scoringmatrix.txt";
        int k = 10;
        int inDelPenalty = -1;
//        int algorithm = Integer.parseInt(args[0]);
//        String queryFile = args[1] + ".txt";
//        String dbFile = args[2] + ".txt";
//        String alphabetFile = args[3] + ".txt";
//        String scoringMatrixFile = args[4] + ".txt";
//        int k = Integer.parseInt(args[5]);
//        int inDelPenalty = Integer.parseInt(args[6]);
        List<String> querySequences;
        List<String> dbSequences;
        List<String> queryIds;
        List<String> dbIds;
        List<ResultObject> listOfResults = new ArrayList<>();
        int[][] scoringMatrix;
        String alphabet;
        Alignment alignment;

        if (algorithm == 1) {
            alignment = Alignment.GLOBAL;
        } else if (algorithm == 2) {
            alignment = Alignment.LOCAL;
        } else {
            alignment = Alignment.DOVETAIL;
        }

        FileHandler fileHandler = new FileHandler();
        querySequences = fileHandler.getSequences(queryFile);
        dbSequences = fileHandler.getSequences(dbFile);
        queryIds = fileHandler.getIds(queryFile);
        dbIds = fileHandler.getIds(dbFile);
        alphabet = fileHandler.getAlphabet(alphabetFile);
        scoringMatrix = fileHandler.getScoringMatrix(scoringMatrixFile, alphabet);

        if(alignment == Alignment.GLOBAL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    GlobalAlignment globalAlignment = new GlobalAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = globalAlignment.generateMatrix(alphabet, scoringMatrix);
                    int score = globalAlignment.getScore(distanceMatrix);
                    String[] sequenceAlignment = globalAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

                    listOfResults.add(new ResultObject(score, 0,
                            0, sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j)));
                }
            }
        } else if(alignment == Alignment.LOCAL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    LocalAlignment localAlignment = new LocalAlignment(querySequence, dbSequence, inDelPenalty);
                    int[][] distanceMatrix = localAlignment.generateMatrix(alphabet, scoringMatrix);
                    int score = localAlignment.getScore(distanceMatrix);
                    String[] sequenceAlignment = localAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

                    listOfResults.add(new ResultObject(score,
                            localAlignment.getQuerySequenceAlignmentBeginning(),
                            localAlignment.getDbSequenceAlignmentBeginning(),
                            sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j)));
                }
            }
        } else if (alignment == Alignment.DOVETAIL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    DovetailAlignment dovetailAlignment = new DovetailAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = dovetailAlignment.generateMatrix(alphabet, scoringMatrix);
                    int score = dovetailAlignment.getScore(distanceMatrix);
                    String[] sequenceAlignment = dovetailAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);

                    listOfResults.add(new ResultObject(score,
                            dovetailAlignment.getQuerySequenceAlignmentBeginning(),
                            dovetailAlignment.getDbSequenceAlignmentBeginning(),
                            sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j)));
                }
            }
        }

        printTopK(k, listOfResults);
    }

    public static void printTopK(int k, List<ResultObject> listOfResults) {
        Collections.sort(listOfResults, new SortByScore());
//        try {
//            FileWriter fileWriter = new FileWriter("outputDovetail.txt");
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//
//            for (int i = 0; i < k; i++) {
//                ResultObject r = listOfResults.get(i);
//                printWriter.print("\n" + (i + 1) + ") Score = " + r.getScore());
//                printWriter.print("\n" + r.getQueryId() + "\t\t" + r.getQuerySequenceStart() + "\t\t" + r.getQuerySequence());
//                printWriter.print("\n" + r.getDbId() + "\t\t" + r.getDbSequenceStart() + "\t\t" + r.getDbSequence());
//                printWriter.print("\n--------------------XXX-----------------------");
//            }
//
//            printWriter.close();
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        for (int i = 0; i < k; i++) {
            ResultObject r = listOfResults.get(i);
            System.out.println("\n" + (i + 1) + ") Score = " + r.getScore());
            System.out.println(r.getQueryId() + "\t\t" + r.getQuerySequenceStart() + "\t\t" + r.getQuerySequence());
            System.out.println(r.getDbId() + "\t\t" + r.getDbSequenceStart() + "\t\t" + r.getDbSequence());
            System.out.println("--------------------XXX-----------------------");
        }
    }

    static class SortByScore implements Comparator<ResultObject> {
        public int compare(ResultObject a, ResultObject b) {
            return b.getScore() - a.getScore();
        }
    }
}
