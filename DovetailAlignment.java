public class DovetailAlignment {
    private String querySequence;
    private String dbSequence;
    private int inDelPenalty;
    private int querySequenceAlignmentBeginning;
    private int querySequenceAlignmentEnd;
    private int dbSequenceAlignmentBeginning;
    private int dbSequenceAlignmentEnd;
    private int maxValueRowIndex;
    private int maxValueColIndex;

    public int getQuerySequenceAlignmentBeginning() {
        return querySequenceAlignmentBeginning;
    }

    public void setQuerySequenceAlignmentBeginning(int querySequenceAlignmentBeginning) {
        this.querySequenceAlignmentBeginning = querySequenceAlignmentBeginning;
    }

    public int getQuerySequenceAlignmentEnd() {
        return querySequenceAlignmentEnd;
    }

    public void setQuerySequenceAlignmentEnd(int querySequenceAlignmentEnd) {
        this.querySequenceAlignmentEnd = querySequenceAlignmentEnd;
    }

    public int getDbSequenceAlignmentBeginning() {
        return dbSequenceAlignmentBeginning;
    }

    public void setDbSequenceAlignmentBeginning(int dbSequenceAlignmentBeginning) {
        this.dbSequenceAlignmentBeginning = dbSequenceAlignmentBeginning;
    }

    public int getDbSequenceAlignmentEnd() {
        return dbSequenceAlignmentEnd;
    }

    public void setDbSequenceAlignmentEnd(int dbSequenceAlignmentEnd) {
        this.dbSequenceAlignmentEnd = dbSequenceAlignmentEnd;
    }

    public int getMaxValueRowIndex() {
        return maxValueRowIndex;
    }

    public void setMaxValueRowIndex(int maxValueRowIndex) {
        this.maxValueRowIndex = maxValueRowIndex;
    }

    public int getMaxValueColIndex() {
        return maxValueColIndex;
    }

    public void setMaxValueColIndex(int maxValueColIndex) {
        this.maxValueColIndex = maxValueColIndex;
    }

    public DovetailAlignment(String querySequence, String dbSequence, int inDelPenalty) {
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.inDelPenalty = inDelPenalty;
    }

    public int[][] generateMatrix(String alphabet, int[][] scoringMatrix) {
        int[][] distanceMatrix = new int[querySequence.length() + 1][dbSequence.length() + 1];

        for(int i = 0; i < distanceMatrix.length; i++)
            distanceMatrix[i][0] = 0;

        for(int i = 0; i < distanceMatrix[0].length; i++)
            distanceMatrix[0][i] = 0;

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
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < distanceMatrix.length; i++) {
            if(distanceMatrix[i][distanceMatrix[0].length - 1] > max) {
                max = distanceMatrix[i][distanceMatrix[0].length - 1];
                setMaxValueRowIndex(i);
                setMaxValueColIndex(distanceMatrix[0].length - 1);
            }
        }

        for (int i = 0; i < distanceMatrix[0].length; i++) {
            if (distanceMatrix[distanceMatrix.length - 1][i] > max) {
                max = distanceMatrix[distanceMatrix.length - 1][i];
                setMaxValueRowIndex(distanceMatrix.length - 1);
                setMaxValueColIndex(i);
            }
        }

        setQuerySequenceAlignmentEnd(getMaxValueRowIndex() - 1);
        setDbSequenceAlignmentEnd(getMaxValueColIndex() - 1);
        return max;
    }

    public String getAlignment(int[][] distanceMatrix, int[][] scoringMatrix, String alphabet) {
        StringBuilder alignment = new StringBuilder();
        int i = getMaxValueRowIndex();
        int j = getMaxValueColIndex();

        while(i != 0 || j != 0) {
            int querySequenceAlphabetIndex = alphabet.indexOf(querySequence.charAt(i - 1));
            int dbSequenceAlphabetIndex = alphabet.indexOf(dbSequence.charAt(j - 1));
            int scoringMatrixValue = scoringMatrix[querySequenceAlphabetIndex][dbSequenceAlphabetIndex];
            int topValue = distanceMatrix[i - 1][j] + inDelPenalty;
            int leftValue = distanceMatrix[i][j - 1] + inDelPenalty;
            int diagonalValue = distanceMatrix[i - 1][j - 1] + scoringMatrixValue;

            if (diagonalValue == distanceMatrix[i][j]) {
                if (querySequence.charAt(i - 1) == dbSequence.charAt(j - 1))
                    alignment.append("|");
                else
                    alignment.append("r");

                i--;
                j--;
            } else if (topValue == distanceMatrix[i][j]) {
                alignment.append("d");
                j--;
            } else if (leftValue == distanceMatrix[i][j]) {
                alignment.append("i");
                i--;
            } else {
                System.out.println("No match!");
            }

            if (i == 0 || j == 0) {
                setQuerySequenceAlignmentBeginning(i);
                setDbSequenceAlignmentBeginning(j);
                break;
            }
        }

        return alignment.reverse().toString();
    }
}
