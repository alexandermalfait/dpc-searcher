package be.alex.dpc;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MemorySearchExecutor {
    private final Map<Long, Word[]> wordsPerSentence;

    private final Search search;

    private final Database database;

    private Logger logger = Logger.getLogger("SearchExecutor");

    public MemorySearchExecutor(Map<Long, Word[]> wordsPerSentence, Search search, Database database) {
        this.wordsPerSentence = wordsPerSentence;
        this.search = search;
        this.database = database;
    }

    public SearchResult getSearchResult() {
        if(search.getTerms().isEmpty()) {
            throw new IllegalStateException("Empty search supplied");
        }

        logger.info("Executing search with " + search.getTerms().size() + " terms");

        SearchTermConvertor searchTermConvertor = new SearchTermConvertor(database);

        List<SearchTermUsingIds> convertedSearchTerms = searchTermConvertor.convertSearchTermsToTermsUsingIds(search.getTerms());

        SearchResult result = getSearchResult(convertedSearchTerms);

        logger.info("Found " + result.getSentenceIds().size() + " search results");

        return result;
    }

    public SearchResult getSearchResult(List<SearchTermUsingIds> convertedSearchTerms) {
        SearchResult result = new SearchResult();

        for(Long sentenceId : wordsPerSentence.keySet()) {
            Word[] words = wordsPerSentence.get(sentenceId);

            SentenceMatcher sentenceMatcher = new SentenceMatcher(words, convertedSearchTerms);

            List<Integer> matchingWordIndexes = sentenceMatcher.matchSentence();

            if(matchingWordIndexes != null) {
                result.addResult(sentenceId, matchingWordIndexes);
            }
        }

        return result;
    }

}
