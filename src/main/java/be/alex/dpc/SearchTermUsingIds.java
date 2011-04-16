package be.alex.dpc;

public class SearchTermUsingIds {
    private int[] wordIds;

    private int lemmaId;

    private byte[] wordTypeIds;

    private byte[] flagIds;

    private byte[] excludeFlagIds;

    private boolean firstInSentence = false;

    private boolean lastInSentence = false;

    private Integer maximumDistanceFromLastMatch;

    private boolean flagsOrMode;

    private boolean excludeTerm = false;

    public int[] getWordIds() {
        return wordIds;
    }

    public void setWordIds(int[] wordIds) {
        this.wordIds = wordIds;
    }

    public int getLemmaId() {
        return lemmaId;
    }

    public void setLemmaId(int lemmaId) {
        this.lemmaId = lemmaId;
    }

    public boolean isFirstInSentence() {
        return firstInSentence;
    }

    public void setFirstInSentence(boolean firstInSentence) {
        this.firstInSentence = firstInSentence;
    }

    public boolean isLastInSentence() {
        return lastInSentence;
    }

    public void setLastInSentence(boolean lastInSentence) {
        this.lastInSentence = lastInSentence;
    }

    public Integer getMaximumDistanceFromLastMatch() {
        return maximumDistanceFromLastMatch;
    }

    public void setMaximumDistanceFromLastMatch(Integer maximumDistanceFromLastMatch) {
        this.maximumDistanceFromLastMatch = maximumDistanceFromLastMatch;
    }

    public byte[] getWordTypeIds() {
        return wordTypeIds;
    }

    public void setWordTypeIds(byte[] wordTypeIds) {
        this.wordTypeIds = wordTypeIds;
    }

    public byte[] getFlagIds() {
        return flagIds;
    }

    public void setFlagIds(byte[] flagIds) {
        this.flagIds = flagIds;
    }

    public boolean isFlagsOrMode() {
        return flagsOrMode;
    }

    public void setFlagsOrMode(boolean flagsOrMode) {
        this.flagsOrMode = flagsOrMode;
    }

    public byte[] getExcludeFlagIds() {
        return excludeFlagIds;
    }

    public void setExcludeFlagIds(byte[] excludeFlagIds) {
        this.excludeFlagIds = excludeFlagIds;
    }

    public boolean isExcludeTerm() {
        return excludeTerm;
    }

    public void setExcludeTerm(boolean excludeTerm) {
        this.excludeTerm = excludeTerm;
    }
}
