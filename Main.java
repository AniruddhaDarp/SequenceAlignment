import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String args[]) {
        Map<Integer, List<String[]>> scoreMap = new TreeMap<>();
        List<String> querySequences = new ArrayList<>();
        List<String> dbSequences = new ArrayList<>();
        int ch = 1;
        int inDelPenalty = 2;

        querySequences = getSequences("query.txt");
        dbSequences = getSequences("database.txt");

        if(ch == 1) {
            for(String querySeqence : querySequences) {
                for(String dbSequence : dbSequences) {
                    GlobalAlignment globalAlignment = new GlobalAlignment(querySeqence, dbSequence, inDelPenalty);

                    int[][] distanceMatrix = globalAlignment.generateMatrix();
                    int score = globalAlignment.getScore(distanceMatrix);

                    String[] sequenceAlignment = new String[3];
                    sequenceAlignment[0] = querySeqence;
                    sequenceAlignment[1] = dbSequence;
                    sequenceAlignment[2] = globalAlignment.getAlignment(distanceMatrix);

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
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null) {
                if (line.charAt(0) == '>') {
                    if(sb.length() == 0)
                        continue;

                    else {
                        sequences.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                    }
                }

                else
                    sb.append(line);
            }
            sequences.add(sb.toString());

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sequences;
    }
}
