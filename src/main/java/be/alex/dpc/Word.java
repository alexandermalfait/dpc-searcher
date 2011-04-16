package be.alex.dpc;

import java.io.Serializable;

public class Word implements Serializable {

    private int wordId;

    private int lemmaId;

    private byte wordTypeId;

    private byte[] flags;

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

    public byte getWordTypeId() {
        return wordTypeId;
    }

    public void setWordTypeId(byte wordTypeId) {
        this.wordTypeId = wordTypeId;
    }

    public byte[] getFlags() {
        return flags;
    }

    public void setFlags(byte[] flags) {
        this.flags = flags;
    }
}
