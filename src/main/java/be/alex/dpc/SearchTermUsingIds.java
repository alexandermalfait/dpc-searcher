package be.alex.dpc;

public class SearchTermUsingIds {
    private int[] wordIds;

    private int[] lemmaIds;

    private int[] wordTypeIds;

    private int[] flagIds;

    private int[] excludeFlagIds;

    private boolean excludeFlagsOrMode = false;

    private boolean flagsOrMode;

    private boolean invertTerm = false;

    private int minOccurrences;

    private Integer maxOccurrences;

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

    public int[] getWordTypeIds() {
        return wordTypeIds;
    }

    public void setWordTypeIds(int[] wordTypeIds) {
        this.wordTypeIds = wordTypeIds;
    }

    public int[] getFlagIds() {
        return flagIds;
    }

    public void setFlagIds(int[] flagIds) {
        this.flagIds = flagIds;
    }

    public boolean isFlagsOrMode() {
        return flagsOrMode;
    }

    public void setFlagsOrMode(boolean flagsOrMode) {
        this.flagsOrMode = flagsOrMode;
    }

    public int[] getExcludeFlagIds() {
        return excludeFlagIds;
    }

    public void setExcludeFlagIds(int[] excludeFlagIds) {
        this.excludeFlagIds = excludeFlagIds;
    }

    public boolean isExcludeFlagsOrMode() {
        return excludeFlagsOrMode;
    }

    public void setExcludeFlagsOrMode(boolean excludeFlagsOrMode) {
        this.excludeFlagsOrMode = excludeFlagsOrMode;
    }

    public int getMinOccurrences() {
        return minOccurrences;
    }

    public void setMinOccurrences(int minOccurrences) {
        this.minOccurrences = minOccurrences;
    }

    public Integer getMaxOccurrences() {
        return maxOccurrences;
    }

    public void setMaxOccurrences(Integer maxOccurrences) {
        this.maxOccurrences = maxOccurrences;
    }

    public boolean isInvertTerm() {
        return invertTerm;
    }

    public void setInvertTerm(boolean invertTerm) {
        this.invertTerm = invertTerm;
    }
}
