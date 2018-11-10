public class GlobalAlignment {
    private String querySequence;
    private String dbSequence;
    private int inDelPenalty;

    public GlobalAlignment(String querySequence, String dbSequence, int inDelPenalty) {
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.inDelPenalty = inDelPenalty;
    }

    public int[][] generateMatrix(String alphabet, int[][] scoringMatrix) {
        int[][] distanceMatrix = new int[querySequence.length() + 1][dbSequence.length() + 1];

        for(int i = 0; i < distanceMatrix.length; i++)
            distanceMatrix[i][0] = i * this.inDelPenalty;

        for(int i = 0; i < distanceMatrix[0].length; i++)
            distanceMatrix[0][i] = i * this.inDelPenalty;

        for (int i = 1; i < distanceMatrix.length; i++) {
            for (int j = 1; j < distanceMatrix[0].length; j++) {
                int querySequenceAlphabetIndex = alphabet.indexOf(querySequence.charAt(i - 1));
                int dbSequenceAlphabetIndex = alphabet.indexOf(dbSequence.charAt(j - 1));
                int scoringMatrixValue = scoringMatrix[querySequenceAlphabetIndex][dbSequenceAlphabetIndex];

                distanceMatrix[i][j] = Math.max(distanceMatrix[i - 1][j] + inDelPenalty,
                        distanceMatrix[i][j - 1] + this.inDelPenalty);
                distanceMatrix[i][j] = Math.max(distanceMatrix[i][j],
                        distanceMatrix[i - 1][j - 1] + scoringMatrixValue);
            }
        }
        return distanceMatrix;
    }

    public int getScore(int[][] distanceMatrix) {
        return distanceMatrix[distanceMatrix.length - 1][distanceMatrix[0].length - 1];
    }

    public String[] getAlignment(int[][] distanceMatrix, int[][] scoringMatrix, String alphabet) {
        StringBuilder alignment = new StringBuilder();
        StringBuilder qsAlignment = new StringBuilder();
        StringBuilder dsAlignment = new StringBuilder();
        String[] sequencesAndAlignment = new String[3];
        int i = distanceMatrix.length - 1, j = distanceMatrix[0].length - 1;

        while(true) {
            if(i == 0) {
                while(j != 0) {
                    alignment.append("i");
                    qsAlignment.append('-');
                    dsAlignment.append(dbSequence.charAt(j - 1));
                    j--;
                }
                break;
            } else if (j == 0) {
                while(i != 0) {
                    alignment.append("d");
                    qsAlignment.append(querySequence.charAt(i - 1));
                    dsAlignment.append('-');
                    i--;
                }
                break;
            } else {
                int querySequenceAlphabetIndex = alphabet.indexOf(querySequence.charAt(i - 1));
                int dbSequenceAlphabetIndex = alphabet.indexOf(dbSequence.charAt(j - 1));
                int scoringMatrixValue = scoringMatrix[querySequenceAlphabetIndex][dbSequenceAlphabetIndex];
                int topValue = distanceMatrix[i - 1][j] + inDelPenalty;
                int leftValue = distanceMatrix[i][j - 1] + inDelPenalty;
                int diagonalValue = distanceMatrix[i - 1][j - 1] + scoringMatrixValue;

                if (diagonalValue == distanceMatrix[i][j]) {
                    if (querySequence.charAt(i - 1) == dbSequence.charAt(j - 1)) {
                        alignment.append("|");
                    } else {
                        alignment.append("r");
                    }

                    qsAlignment.append(querySequence.charAt(i - 1));
                    dsAlignment.append(dbSequence.charAt(j - 1));
                    i--;
                    j--;
                } else if (topValue == distanceMatrix[i][j]) {
                    alignment.append("d");
                    qsAlignment.append(querySequence.charAt(i - 1));
                    dsAlignment.append('-');
                    i--;
                } else if (leftValue == distanceMatrix[i][j]) {
                    alignment.append("i");
                    qsAlignment.append('-');
                    dsAlignment.append(dbSequence.charAt(j - 1));
                    j--;
                }
//                else {
//                    System.out.println("No match!");
//                }
            }
        }

        sequencesAndAlignment[2] = alignment.reverse().toString();
        sequencesAndAlignment[0] = qsAlignment.reverse().toString();
        sequencesAndAlignment[1] = dsAlignment.reverse().toString();

        if(sequencesAndAlignment[2].length() != sequencesAndAlignment[0].length())
            System.out.println("Query and alignment have different lengths!!");

        if(sequencesAndAlignment[1].length() != sequencesAndAlignment[0].length())
            System.out.println("DB and alignment have different lengths!!");

        return sequencesAndAlignment;
    }
}
