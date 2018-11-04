import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) {
        List<String> querySequences = new ArrayList<>();
        List<String> dbSequences = new ArrayList<>();
        int ch = 0;

        querySequences = getSequences(1);
        dbSequences = getSequences(2);

        System.out.println("What type of alignment?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            ch = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ch == 1) {
            GlobalAlignment ga = new GlobalAlignment();
        }

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

    public static List<String> getSequences(int num) {
        List<String> sequences = new ArrayList<>();
        String fileName = "";
        if (num == 1)
            fileName = "query.txt";
        else
            fileName = "database.txt";
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
