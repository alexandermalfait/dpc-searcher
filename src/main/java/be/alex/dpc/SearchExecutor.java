package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SearchExecutor {
    private final Map<Long, Word[]> wordsPerSentence;

    private final Search search;

    private final Database database;

    private Logger logger = Logger.getLogger("SearchExecutor");

    private List<SearchTermUsingIds> convertedSearchTerms;

    public SearchExecutor(Map<Long, Word[]> wordsPerSentence, Search search, Database database) {
        this.wordsPerSentence = wordsPerSentence;
        this.search = search;
        this.database = database;
    }

    public SearchResult getSearchResult() {
        if(search.getTerms().isEmpty()) {
            throw new IllegalStateException("Empty search supplied");
        }

        logger.info("Executing search with " + search.getTerms().size() + " terms");

        convertedSearchTerms = convertSearchTermsToTermsUsingIds(search.getTerms());

        SearchResult result = new SearchResult();

        int index = 0;

        for(Long sentenceId : wordsPerSentence.keySet()) {
            Word[] words = wordsPerSentence.get(sentenceId);

            List<Integer> matchingWordIndexes = matchSentence(words);

            if(matchingWordIndexes != null) {
                result.addResult(sentenceId, matchingWordIndexes);
            }

            index++;

            if(index % 1000 == 0) {
                logger.info("Checked sentence " + index);
            }
        }

        logger.info("Found " + result.getSentenceIds().size() + " search results");

        return result;
    }


    @SuppressWarnings({"AssignmentToForLoopParameter"})
    private List<Integer> matchSentence(Word[] words) {
        int lastMatchIndex = -1;
        int continueFromWordIndex = 0;

        List<Integer> matchingWordIndexes = null;

        for(int termIndex = 0; termIndex < convertedSearchTerms.size(); termIndex++) {
            SearchTermUsingIds searchTerm = convertedSearchTerms.get(termIndex);

            boolean foundMatchingWord = false;

            for(int wordIndex = continueFromWordIndex; wordIndex < words.length; wordIndex++) {
                Word word = words[wordIndex];

                if(searchTerm.isFirstInSentence() && wordIndex > 0) {
                    return null;
                }

                if(searchTerm.isLastInSentence()) {
                    // if the sentence ends with a dot (LET), allow the second-to-last word to be handled as the last word
                    if(words[words.length - 1].getWordTypeId() == 6) {
                        if(wordIndex < words.length - 2) {
                            matchingWordIndexes = null;
                            termIndex = -1;
                            continueFromWordIndex = wordIndex + 1;
                            break;
                        }
                    }
                    else {
                        if(wordIndex < words.length - 1) {
                            matchingWordIndexes = null;
                            termIndex = -1;
                            continueFromWordIndex = wordIndex + 1;
                            break;
                        }
                    }
                }

                if(searchTerm.getMaximumDistanceFromLastMatch() != null) {
                    if(lastMatchIndex == -1 || wordIndex - lastMatchIndex > searchTerm.getMaximumDistanceFromLastMatch()) {
                        matchingWordIndexes = null;
                        termIndex = -1;
                        continueFromWordIndex = wordIndex + 1;
                        break;
                    }
                }

                if(wordMatches(searchTerm, word)) {
                    if(searchTerm.isExcludeTerm()) {
                        return null;
                    }

                    if(matchingWordIndexes == null) {
                        matchingWordIndexes = new ArrayList<Integer>();
                    }

                    matchingWordIndexes.add(wordIndex);

                    lastMatchIndex = wordIndex;
                    continueFromWordIndex = wordIndex + 1;
                    foundMatchingWord = true;
                    break;
                }
                else {
                    // if we need to exclude this term, and it is the maximum distance from the previous hit,
                    // this term is "satisfied" by not being found within the given reach, and we can advance
                    // to the next search term (by breaking from the loop)
                    if(searchTerm.isExcludeTerm()) {
                        if(searchTerm.getMaximumDistanceFromLastMatch() != null) {
                            if(wordIndex - lastMatchIndex == searchTerm.getMaximumDistanceFromLastMatch()) {
                                break;
                            }
                        }
                    }
                }
            }

            /*if(!foundMatchingWord && !searchTerm.isExcludeTerm()) {
                return null;
            }*/
        }

        if(matchingWordIndexes != null && matchingWordIndexes.size() == convertedSearchTerms.size()) {
            return matchingWordIndexes;
        }
        else {
            return null;
        }
    }

    private boolean wordMatches(SearchTermUsingIds searchTerm, Word word) {
        if(searchTerm.getWordIds() != null) {
            boolean matchingWord = false;

            for(int wordId : searchTerm.getWordIds()) {
                if(word.getWordId() == wordId) {
                    matchingWord = true;
                    break;
                }
            }

            if(!matchingWord) {
                return false;
            }
        }

        if(searchTerm.getLemmaId() != 0) {
            if(word.getLemmaId() != searchTerm.getLemmaId()) {
                return false;
            }
        }

        if(searchTerm.getWordTypeIds() != null) {
            boolean wordTypeMatches = false;

            for(byte wordTypeId : searchTerm.getWordTypeIds()) {
                if(word.getWordTypeId() == wordTypeId) {
                    wordTypeMatches = true;
                    break;
                }
            }

            if(!wordTypeMatches) {
                return false;
            }
        }

        if(searchTerm.getFlagIds() != null) {
            if(searchTerm.isFlagsOrMode()) {
                boolean anyFlagMatch = false;

                for(byte searchFlag : searchTerm.getFlagIds()) {
                    for(byte wordFlag : word.getFlags()) {
                        if(searchFlag == wordFlag) {
                            anyFlagMatch = true;
                            break;
                        }
                    }
                }

                if(! anyFlagMatch) {
                    return false;
                }
            }
            else {
                for(byte searchFlag : searchTerm.getFlagIds()) {
                    boolean anyFlagMatch = false;

                    for(byte wordFlag : word.getFlags()) {
                        if(searchFlag == wordFlag) {
                            anyFlagMatch = true;
                        }
                    }

                    if(!anyFlagMatch) {
                        return false;
                    }
                }
            }
        }

        if(searchTerm.getExcludeFlagIds() != null) {
            for(byte searchFlag : searchTerm.getExcludeFlagIds()) {
                for(byte wordFlag : word.getFlags()) {
                    if(searchFlag == wordFlag) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    private List<SearchTermUsingIds> convertSearchTermsToTermsUsingIds(List<SearchTerm> terms) {
        List<SearchTermUsingIds> convertedTerms = new ArrayList<SearchTermUsingIds>();

        for(SearchTerm term : terms) {
            SearchTermUsingIds convertedTerm = new SearchTermUsingIds();

            convertedTerm.setFirstInSentence(term.isFirstInSentence());
            convertedTerm.setLastInSentence(term.isLastInSentence());
            convertedTerm.setMaximumDistanceFromLastMatch(term.getMaximumDistanceFromLastMatch());
            convertedTerm.setFlagsOrMode(term.isFlagsOrMode());
            convertedTerm.setExcludeTerm(term.isExcludeTerm());

            if(term.getWord() != null) {
                if(term.isWordRegex()) {
                    convertedTerm.setWordIds(database.getWordIdsUsingRegex(term.getWord()));
                }
                else {
                    convertedTerm.setWordIds(new int[] { database.getWordId(term.getWord()) } );
                }
            }

            if(term.getLemma() != null) {
                convertedTerm.setLemmaId(database.getLemmaId(term.getLemma()));
            }

            if(term.getFlags() != null) {
                byte[] flagIds = new byte[term.getFlags().length];

                for(int f = 0; f < term.getFlags().length; f++) {
                    flagIds[f] = database.getFlagId(term.getFlags()[f]);
                }

                convertedTerm.setFlagIds(flagIds);
            }

            if(term.getExcludeFlags() != null) {
                byte[] flagIds = new byte[term.getExcludeFlags().length];

                for(int f = 0; f < term.getExcludeFlags().length; f++) {
                    flagIds[f] = database.getFlagId(term.getExcludeFlags()[f]);
                }

                convertedTerm.setExcludeFlagIds(flagIds);
            }

            if(term.getWordTypes() != null) {
                byte[] wordTypeIds = new byte[term.getWordTypes().length];

                for(int w = 0; w < term.getWordTypes().length; w++) {
                    wordTypeIds[w] = database.getWordTypeId(term.getWordTypes()[w]);
                }

                convertedTerm.setWordTypeIds(wordTypeIds);
            }

            convertedTerms.add(convertedTerm);
        }

        return convertedTerms;
    }
}
