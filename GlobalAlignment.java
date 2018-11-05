public class GlobalAlignment {
    private String querySequence = new String();
    private String dbSequence = new String();
    private int inDelPenalty;

    public GlobalAlignment(String querySequence, String dbSequence, int inDelPenalty) {
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.inDelPenalty = inDelPenalty;
    }

    public String[] getScoreAndAlignment() {
        int[][] matrix = generateMatrix(this.inDelPenalty);
        String score = getScore(matrix);
        String alignment = generateAlignment(matrix, this.querySequence, this.dbSequence);

        return new String[] {score, this.querySequence, this.dbSequence, alignment};
    }

    private int[][] generateMatrix(int inDelPenalty) {
        int[][] matrix = new int[this.querySequence.length() + 1][this.dbSequence.length() + 1];
        
        return matrix;
    }

    private String getScore(int[][] matrix) {
        return "";
    }

    public String generateAlignment(int[][] matrix, String querySequence, String dbSequence) {
        return "";
    }
}
