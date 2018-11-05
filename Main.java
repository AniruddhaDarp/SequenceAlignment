import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String args[]) {
        Queue<String> priorityQueue = new PriorityQueue<>();
        List<String> querySequences = new ArrayList<>();
        List<String> dbSequences = new ArrayList<>();
        int ch = 0;
        int inDelPenalty = 2;

        while(!priorityQueue.isEmpty())
            System.out.print(priorityQueue.poll());

        querySequences = getSequences("query.txt");
        dbSequences = getSequences("database.txt");

        System.out.println("What type of alignment?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            ch = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ch == 1) {
            for(String querySeqence : querySequences) {
                for(String dbSequence : dbSequences) {
                    GlobalAlignment globalAlignment = new GlobalAlignment(querySeqence, dbSequence, inDelPenalty);
                    String[] scoreAndAlignment = globalAlignment.getScoreAndAlignment();
//                    int score = globalAlignment.getScore(matrix);
//                    String alignment = globalAlignment.generateAlignment(matrix, querySeqence, dbSequence);
                    StringBuilder sb = new StringBuilder();
                    sb.append(scoreAndAlignment[0]);
                    sb.append(";" + querySeqence);
                    sb.append(";" + dbSequence);
                    sb.append(";" + scoreAndAlignment[1]);
                    priorityQueue.add(sb.toString());
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
