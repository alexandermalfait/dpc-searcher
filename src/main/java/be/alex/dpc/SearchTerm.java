package be.alex.dpc;

public class SearchTerm {

    private String word;

    private boolean wordRegex = false;

    private String lemma;

    private boolean firstInSentence = false;

    private boolean lastInSentence = false;

    private Integer maximumDistanceFromLastMatch;

    private String[] wordTypes;

    private String[] flags;

    private boolean flagsOrMode;

    private String[] excludeFlags;

    private boolean excludeTerm = false;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
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

    public boolean isExcludeTerm() {
        return excludeTerm;
    }

    public void setExcludeTerm(boolean excludeTerm) {
        this.excludeTerm = excludeTerm;
    }
}
