import java.util.*;

enum Alignment {
    GLOBAL, LOCAL, DOVETAIL;
}

public class Main {
    public static void main(String args[]) {
        List<String> querySequences;
        List<String> dbSequences;
        List<String> queryIds;
        List<String> dbIds;
        List<ResultObject> listOfResults = new ArrayList<>();
        int[][] scoringMatrix;
        String alphabet;
        Alignment alignment = Alignment.GLOBAL;
        int inDelPenalty = -1;

        FileHandler fileHandler = new FileHandler();
        querySequences = fileHandler.getSequences("query.txt");
        dbSequences = fileHandler.getSequences("database.txt");
        queryIds = fileHandler.getIds("query.txt");
        dbIds = fileHandler.getIds("database.txt");
        alphabet = fileHandler.getAlphabet("alphabet.txt");
        scoringMatrix = fileHandler.getScoringMatrix("scoringmatrix.txt", alphabet);
        int count = 0;
        int k = 10;

        if(alignment == Alignment.GLOBAL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    long startTime = System.currentTimeMillis();
                    GlobalAlignment globalAlignment = new GlobalAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = globalAlignment.generateMatrix(alphabet, scoringMatrix);
                    String[] sequenceAlignment = globalAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);
                    long timeTaken = System.currentTimeMillis() - startTime;

                    listOfResults.add(new ResultObject(globalAlignment.getScore(distanceMatrix), 0,
                            0, sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j),
                            timeTaken));

                    System.out.println(count + "; " + timeTaken);
                    count++;
                }
            }
        } else if(alignment == Alignment.LOCAL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    long startTime = System.currentTimeMillis();
                    LocalAlignment localAlignment = new LocalAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = localAlignment.generateMatrix(alphabet, scoringMatrix);
                    String[] sequenceAlignment = localAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);
                    long timeTaken = System.currentTimeMillis() - startTime;

                    listOfResults.add(new ResultObject(localAlignment.getScore(distanceMatrix),
                            localAlignment.getQuerySequenceAlignmentBeginning(),
                            localAlignment.getDbSequenceAlignmentBeginning(),
                            sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j),
                            timeTaken));

                    System.out.println(count + "; " + timeTaken);
                    count++;
                }
            }
        } else if (alignment == Alignment.DOVETAIL) {
            listOfResults = new ArrayList<>();
            for(int i = 0; i < querySequences.size(); i++) {
                String querySequence = querySequences.get(i);

                for(int j = 0; j < dbSequences.size(); j++) {
                    String dbSequence = dbSequences.get(j);
                    long startTime = System.currentTimeMillis();
                    DovetailAlignment dovetailAlignment = new DovetailAlignment(querySequence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = dovetailAlignment.generateMatrix(alphabet, scoringMatrix);
                    String[] sequenceAlignment = dovetailAlignment.getAlignment(distanceMatrix, scoringMatrix, alphabet);
                    long timeTaken = System.currentTimeMillis() - startTime;

                    listOfResults.add(new ResultObject(dovetailAlignment.getScore(distanceMatrix),
                            dovetailAlignment.getQuerySequenceAlignmentBeginning(),
                            dovetailAlignment.getDbSequenceAlignmentBeginning(),
                            sequenceAlignment[0], sequenceAlignment[1], queryIds.get(i), dbIds.get(j), timeTaken));

                    System.out.println(count);
                    count++;
                }
            }
        }

        printTopK(k, listOfResults);
    }

    public static void printTopK(int k, List<ResultObject> listOfResults) {
        Collections.sort(listOfResults, new SortByScore());

        for (int i = 0; i < k; i++) {
            ResultObject r = (ResultObject) listOfResults.get(i);
            System.out.println("\n\n" + (i + 1) + ") Score = " + r.getScore());
            System.out.println(r.getQueryId() + "\t" + r.getQuerySequenceStart() + "\t" + r.getQuerySequence());
            System.out.println(r.getDbId() + "\t" + r.getDbSequenceStart() + "\t" + r.getDbSequence());
            System.out.print("\n--------------------------XXX--------------------------");
        }
    }

    static class SortByScore implements Comparator<ResultObject> {
        public int compare(ResultObject a, ResultObject b) {
            return b.getScore() - a.getScore();
        }
    }
}
