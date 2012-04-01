package be.alex.dpc;

public class SearchTermUsingIds {
    private int[] wordIds;

    private int[] lemmaIds;

    private byte[] wordTypeIds;

    private byte[] flagIds;

    private byte[] excludeFlagIds;

    private boolean excludeFlagsOrMode = false;

    private boolean flagsOrMode;

    private boolean invertTerm = false;

    private int minOccurences;

    private Integer maxOccurences;

    public int[] getWordIds() {
        return wordIds;
    }

    public void setWordIds(int[] wordIds) {
        this.wordIds = wordIds;
    }

    public int[] getLemmaIds() {
        return lemmaIds;
    }

    public void setLemmaIds(int[] lemmaIds) {
        this.lemmaIds = lemmaIds;
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

    public boolean isExcludeFlagsOrMode() {
        return excludeFlagsOrMode;
    }

    public void setExcludeFlagsOrMode(boolean excludeFlagsOrMode) {
        this.excludeFlagsOrMode = excludeFlagsOrMode;
    }

    public int getMinOccurences() {
        return minOccurences;
    }

    public void setMinOccurences(int minOccurences) {
        this.minOccurences = minOccurences;
    }

    public Integer getMaxOccurences() {
        return maxOccurences;
    }

    public void setMaxOccurences(Integer maxOccurences) {
        this.maxOccurences = maxOccurences;
    }

    public boolean isInvertTerm() {
        return invertTerm;
    }

    public void setInvertTerm(boolean invertTerm) {
        this.invertTerm = invertTerm;
    }
}
