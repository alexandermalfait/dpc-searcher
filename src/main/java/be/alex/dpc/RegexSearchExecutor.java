package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSearchExecutor {

    private final List<String> lines;

    private final Search search;

    private final Database database;

    private Logger logger = Logger.getLogger("SearchExecutor");

    public RegexSearchExecutor(List<String> lines, Search search, Database database) {
        this.lines = lines;
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

        Pattern pattern = new RegexSearchPatternBuilder().buildPattern(convertedSearchTerms);

        logger.info("Searching using pattern: " + pattern);

        for(String line : lines) {
            Matcher lineMatcher = pattern.matcher(line);
            if(lineMatcher.lookingAt()) {
                Long sentenceId = Long.valueOf(lineMatcher.group(1));

                List<Integer> wordIndexes = new ArrayList<Integer>();

                for(int groupIndex = 2; groupIndex < lineMatcher.groupCount(); groupIndex++) {
                    String groupValue = lineMatcher.group(groupIndex);

                    if(groupValue != null && groupValue.endsWith(RegexSearchPatternBuilder.INDEX_DELIMITER)) {
                        wordIndexes.add(Integer.parseInt(groupValue.replaceAll(RegexSearchPatternBuilder.INDEX_DELIMITER, "")));
                    }
                }
                
                result.addResult(sentenceId, wordIndexes);
            }
        }

        return result;
    }
}
