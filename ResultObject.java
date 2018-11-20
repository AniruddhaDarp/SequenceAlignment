public class ResultObject {
    private int score;
    private int querySequenceStart;
    private int dbSequenceStart;
    private String querySequence;
    private String dbSequence;
    private String queryId;
    private String dbId;

    public ResultObject(int score, int querySequenceStart, int dbSequenceStart, String querySequence,
                        String dbSequence, String queryId, String dbId) {
        this.score = score;
        this.querySequenceStart = querySequenceStart;
        this.dbSequenceStart = dbSequenceStart;
        this.querySequence = querySequence;
        this.dbSequence = dbSequence;
        this.queryId = queryId;
        this.dbId = dbId;
    }

    public int getScore() {
        return score;
    }

    public int getQuerySequenceStart() {
        return querySequenceStart;
    }

    public int getDbSequenceStart() {
        return dbSequenceStart;
    }

    public String getQuerySequence() {
        return querySequence;
    }

    public String getDbSequence() {
        return dbSequence;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getDbId() {
        return dbId;
    }
}
