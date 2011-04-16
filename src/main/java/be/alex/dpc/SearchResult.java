package be.alex.dpc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchResult extends LinkedHashMap<Long,List<Integer>> {

    public void addResult(Long sentenceId, List<Integer> wordIndexes) {
        put(sentenceId, wordIndexes);
    }

    public List<Long> getSentenceIds() {
        return new ArrayList<Long>(keySet());
    }

    public List<Integer> getWordIndexesForSentenceId(Long sentenceId) {
        return get(sentenceId);
    }
}
