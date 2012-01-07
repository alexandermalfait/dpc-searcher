package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;

public class SentenceMatcher {
    private final Word[] words;

    private final List<SearchTermUsingIds> terms;

    private int lastMatchIndex;

    private int continueFromWordIndex;

    private List<Integer> matchingWordIndexes = new ArrayList<Integer>();

    private int termIndex;

    private int wordIndex;

    public SentenceMatcher(Word[] words, List<SearchTermUsingIds> terms) {
        this.words = words;
        this.terms = terms;
    }

    public List<Integer> matchSentence() {
        lastMatchIndex = -1;
        continueFromWordIndex = 0;

        for(termIndex = 0; termIndex < terms.size(); termIndex++) {
            SearchTermUsingIds searchTerm = terms.get(termIndex);

            for(wordIndex = continueFromWordIndex; wordIndex < words.length; wordIndex++) {
                Word word = words[wordIndex];

                if(searchTerm.isFirstInSentence() && wordIndex > 0) {
                    return null;
                }

                if(searchTerm.isLastInSentence()) {
                    if(!currentIsLastWord()) {
                        continue; // go through to next word
                    }
                }

                if(searchTerm.getMaximumDistanceFromLastMatch() != null) {
                    if(! haveMatch() || getDistanceFromLastMatch() > searchTerm.getMaximumDistanceFromLastMatch()) {
                        retrySearchFromNextWord();
                        break;
                    }
                }

                if(wordMatches(searchTerm, word)) {
                    if(searchTerm.isExcludeTerm()) {
                        return null;
                    }

                    currentWordMatches();

                    break;
                }
                else {
                    // if we need to exclude this term, and it is the maximum distance from the previous hit,
                    // this term is "satisfied" by not being found within the given reach, and we can advance
                    // to the next search term (by breaking from the loop)
                    if(searchTerm.isExcludeTerm()) {
                        if(searchTerm.getMaximumDistanceFromLastMatch() != null) {
                            if(getDistanceFromLastMatch() == searchTerm.getMaximumDistanceFromLastMatch()) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(matchingWordIndexes.size() == terms.size()) {
            return matchingWordIndexes;
        }
        else {
            return null;
        }
    }

    private boolean currentIsLastWord() {
        // if the sentence ends with a dot (LET), allow the second-to-last word to be handled as the last word
        if(words[words.length - 1].getWordTypeId() == 6) {
            return wordIndex >= words.length - 2;
        }
        else {
            return wordIndex >= words.length - 1;
        }
    }

    private void currentWordMatches() {
        matchingWordIndexes.add(wordIndex);

        lastMatchIndex = wordIndex;
        continueFromWordIndex = wordIndex + 1;
    }

    private int getDistanceFromLastMatch() {
        return wordIndex - lastMatchIndex;
    }

    private boolean haveMatch() {
        return lastMatchIndex > -1;
    }

    private void retrySearchFromNextWord() {
        matchingWordIndexes.clear();
        termIndex = -1;
        continueFromWordIndex = wordIndex + 1;
    }

    private boolean wordMatches(SearchTermUsingIds searchTerm, Word word) {
        WordMatcher wordMatcher = new WordMatcher(word, searchTerm);

        return wordMatcher.matches();
    }
}
