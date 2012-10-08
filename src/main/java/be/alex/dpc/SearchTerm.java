package be.alex.dpc;

public class SearchTerm {

    private String word;

    private boolean wordRegex = false;

    private String[] lemmas;

    private String[] wordTypes;

    private int[] wordTypeIds;

    private String[] flags;

    private boolean flagsOrMode;

    private String[] excludeFlags;

    private boolean invertTerm = false;

    private boolean excludedFlagsOrMode;

    private int minOccurrences;

    private Integer maxOccurrences;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getLemmas() {
        return lemmas;
    }

    public void setLemmas(String[] lemmas) {
        this.lemmas = lemmas;
    }

    public String[] getWordTypes() {
        return wordTypes;
    }

    public void setWordTypes(String[] wordTypes) {
        this.wordTypes = wordTypes;
    }

    public String[] getFlags() {
        return flags;
    }

    public void setFlags(String[] flags) {
        this.flags = flags;
    }

    public boolean isFlagsOrMode() {
        return flagsOrMode;
    }

    public void setFlagsOrMode(boolean flagsOrMode) {
        this.flagsOrMode = flagsOrMode;
    }

    public boolean isWordRegex() {
        return wordRegex;
    }

    public void setWordRegex(boolean wordRegex) {
        this.wordRegex = wordRegex;
    }

    public String[] getExcludeFlags() {
        return excludeFlags;
    }

    public void setExcludeFlags(String[] excludeFlags) {
        this.excludeFlags = excludeFlags;
    }

    public boolean isExcludedFlagsOrMode() {
        return excludedFlagsOrMode;
    }

    public void setExcludedFlagsOrMode(boolean excludedFlagsOrMode) {
        this.excludedFlagsOrMode = excludedFlagsOrMode;
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

    public int[] getWordTypeIds() {
        return wordTypeIds;
    }

    public void setWordTypeIds(int[] wordTypeIds) {
        this.wordTypeIds = wordTypeIds;
    }
}
