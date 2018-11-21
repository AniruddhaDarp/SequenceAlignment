public class LocalAlignment {
    private String querySequence;
    private String dbSequence;
    private int inDelPenalty;
    private int querySequenceAlignmentBeginning;
    private int querySequenceAlignmentEnd;
    private int dbSequenceAlignmentBeginning;
    private int dbSequenceAlignmentEnd;
    private int maxValueRowIndex;
    private int maxValueColIndex;

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

    public int getDbSequenceAlignmentBeginning() {
        return dbSequenceAlignmentBeginning;
    }

    public void setDbSequenceAlignmentBeginning(int dbSequenceAlignmentBeginning) {
        this.dbSequenceAlignmentBeginning = dbSequenceAlignmentBeginning;
    }

    public void setDbSequenceAlignmentEnd(int dbSequenceAlignmentEnd) {
        this.dbSequenceAlignmentEnd = dbSequenceAlignmentEnd;
    }

    public int getQuerySequenceAlignmentBeginning() {
        return querySequenceAlignmentBeginning;
    }

    public void setQuerySequenceAlignmentBeginning(int querySequenceAlignmentBeginning) {
        this.querySequenceAlignmentBeginning = querySequenceAlignmentBeginning;
    }

    public void setQuerySequenceAlignmentEnd(int querySequenceAlignmentEnd) {
        this.querySequenceAlignmentEnd = querySequenceAlignmentEnd;
    }

    public LocalAlignment(String querySequence, String dbSequence, int inDelPenalty) {
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.inDelPenalty = inDelPenalty;
    }

    public int[][] generateMatrix(String alphabet, int[][] scoringMatrix) {
        int[][] distanceMatrix = new int[querySequence.length() + 1][dbSequence.length() + 1];

        for(int i = 0; i < distanceMatrix.length; i++) {
            distanceMatrix[i][0] = 0;
        }
        for(int i = 0; i < distanceMatrix[0].length; i++) {
            distanceMatrix[0][i] = 0;
        }

        for (int i = 1; i < distanceMatrix.length; i++) {
            for (int j = 1; j < distanceMatrix[0].length; j++) {
                int querySequenceAlphabetIndex = alphabet.indexOf(querySequence.charAt(i - 1));
                int dbSequenceAlphabetIndex = alphabet.indexOf(dbSequence.charAt(j - 1));
                int scoringMatrixValue = scoringMatrix[querySequenceAlphabetIndex][dbSequenceAlphabetIndex];

                distanceMatrix[i][j] = Math.max(distanceMatrix[i - 1][j] + inDelPenalty,
                        distanceMatrix[i][j - 1] + this.inDelPenalty);
                distanceMatrix[i][j] = Math.max(distanceMatrix[i][j],
                        distanceMatrix[i - 1][j - 1] + scoringMatrixValue);
                distanceMatrix[i][j] = Math.max(distanceMatrix[i][j], 0);
            }
        }
        return distanceMatrix;
    }

    public int getScore(int[][] distanceMatrix) {
        int max = Integer.MIN_VALUE;

        for (int i = 1; i < distanceMatrix.length; i++) {
            for (int j = 1; j < distanceMatrix[0].length; j++) {
                if (distanceMatrix[i][j] > max) {
                    max = distanceMatrix[i][j];
                    setMaxValueRowIndex(i);
                    setMaxValueColIndex(j);
                }
            }
        }

        setQuerySequenceAlignmentEnd(getMaxValueRowIndex() - 1);
        setDbSequenceAlignmentEnd(getMaxValueColIndex() - 1);
        return max;
    }

    public String[] getAlignment(int[][] distanceMatrix, int[][] scoringMatrix, String alphabet) {
        StringBuilder qsAlignment = new StringBuilder();
        StringBuilder dsAlignment = new StringBuilder();
        String[] sequencesWithAlignment = new String[2];
        int i = getMaxValueRowIndex();
        int j = getMaxValueColIndex();

        while(i > 0 && j > 0) {
            int querySequenceAlphabetIndex = alphabet.indexOf(querySequence.charAt(i - 1));
            int dbSequenceAlphabetIndex = alphabet.indexOf(dbSequence.charAt(j - 1));
            int scoringMatrixValue = scoringMatrix[querySequenceAlphabetIndex][dbSequenceAlphabetIndex];
            int topValue = distanceMatrix[i][j] - inDelPenalty;
            int leftValue = distanceMatrix[i][j] - inDelPenalty;
            int diagonalValue = distanceMatrix[i][j] - scoringMatrixValue;

            if (diagonalValue == distanceMatrix[i - 1][j - 1]) {
                qsAlignment.append(querySequence.charAt(i - 1));
                dsAlignment.append(dbSequence.charAt(j - 1));
                i--;
                j--;
            } else if (topValue == distanceMatrix[i - 1][j]) {
                qsAlignment.append(querySequence.charAt(i - 1));
                dsAlignment.append('-');
                i--;
            } else if (leftValue == distanceMatrix[i][j - 1]) {
                qsAlignment.append('-');
                dsAlignment.append(dbSequence.charAt(j - 1));
                j--;
            }

            if (distanceMatrix[i][j] == 0) {
                setQuerySequenceAlignmentBeginning(i);
                setDbSequenceAlignmentBeginning(j);
                break;
            }
        }

        sequencesWithAlignment[0] = qsAlignment.reverse().toString();
        sequencesWithAlignment[1] = dsAlignment.reverse().toString();

        return sequencesWithAlignment;
    }
}
