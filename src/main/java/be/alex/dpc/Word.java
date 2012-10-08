package be.alex.dpc;

import java.io.Serializable;

public class Word implements Serializable {

    private int wordId;

    private int lemmaId;

    private int wordTypeId;

    private int[] flags;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getLemmaId() {
        return lemmaId;
    }

    public void setLemmaId(int lemmaId) {
        this.lemmaId = lemmaId;
    }

    public int getWordTypeId() {
        return wordTypeId;
    }

    public void setWordTypeId(int wordTypeId) {
        this.wordTypeId = wordTypeId;
    }

    public int[] getFlags() {
        return flags;
    }

    public void setFlags(int[] flags) {
        this.flags = flags;
    }
}
