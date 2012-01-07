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

    protected List<SearchTermUsingIds> excludedTermsSinceLastMatch = new ArrayList<SearchTermUsingIds>(1);

    public SentenceMatcher(Word[] words, List<SearchTermUsingIds> terms) {
        this.words = words;
        this.terms = terms;
    }

    public List<Integer> matchSentence() {
        lastMatchIndex = -1;
        continueFromWordIndex = 0;

        int numNonExcludedTerms = 0;

        for(SearchTermUsingIds term : terms) {
            if(!term.isExcludeTerm()) {
                numNonExcludedTerms++;
            }
        }

        for(termIndex = 0; termIndex < terms.size(); termIndex++) {
            SearchTermUsingIds searchTerm = terms.get(termIndex);

            if(searchTerm.isExcludeTerm()) {
                excludedTermsSinceLastMatch.add(searchTerm);
                continue;
            }
            
            for(wordIndex = continueFromWordIndex; wordIndex < words.length; wordIndex++) {
                Word word = words[wordIndex];

                if(searchTerm.isFirstInSentence() && wordIndex > 0) {
                    return null;
                }

                if(searchTerm.isLastInSentence()) {
                    if(!isLastWord(wordIndex)) {
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
                    if(haveExcludedMatchSinceLastMatch(wordIndex - 1)) {
                        return null;
                    }
                    else {
                        currentWordMatches();
                        break;
                    }
                }
            }
        }

        if(matchingWordIndexes.size() == numNonExcludedTerms) {
            if(haveExcludedMatchSinceLastMatch(words.length - 1)) {
               return null;
            }
            else {
                return matchingWordIndexes;
            }
        }
        else {
            return null;
        }
    }

    private boolean haveExcludedMatchSinceLastMatch(int untilWordIndex) {
        if(excludedTermsSinceLastMatch.isEmpty()) {
            return false;
        }

        for(SearchTermUsingIds excludedTerm : excludedTermsSinceLastMatch) {
            for(int previousWordIndex = lastMatchIndex + 1; previousWordIndex <= untilWordIndex; previousWordIndex++) {
                if(wordMatches(excludedTerm, words[previousWordIndex])) {
                    if(excludedTerm.isLastInSentence()) {
                        if(isLastWord(previousWordIndex)) {
                            return true;
                        }
                    }
                    else if(excludedTerm.getMaximumDistanceFromLastMatch() != null) {
                        if(excludedTerm.getMaximumDistanceFromLastMatch() >= previousWordIndex - lastMatchIndex) {
                            return true;
                        }
                    }
                    else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean nextWordMatchesNextTerm() {
        if(wordIndex < words.length - 1 && termIndex < terms.size() - 1) {
            Word nextWord = words[wordIndex + 1];
            SearchTermUsingIds nextTerm = terms.get(termIndex + 1);

            return wordMatches(nextTerm, nextWord);
        }

        return false;
    }

    private boolean currentIsLastTerm() {
        return termIndex == terms.size() - 1;
    }

    private boolean isLastWord(int wordIndex) {
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
        excludedTermsSinceLastMatch.clear();
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
