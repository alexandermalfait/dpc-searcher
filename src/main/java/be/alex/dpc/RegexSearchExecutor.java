package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexSearchExecutor {

    private final String data;

    private final Search search;

    private final Database database;

    private Logger logger = Logger.getLogger("SearchExecutor");

    public RegexSearchExecutor(String data, Search search, Database database) {
        this.data = data;
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

        Pattern pattern = Pattern.compile(new RegexSearchPatternBuilder().buildPattern(convertedSearchTerms), Pattern.MULTILINE);

        logger.info("Searching using pattern: " + pattern);

        Matcher matcher = pattern.matcher(data);

        while(matcher.find()) {
            Long sentenceId = Long.valueOf(matcher.group(1));

            List<Integer> wordIndexes = new ArrayList<Integer>();

            for(int groupIndex = 2; groupIndex < matcher.groupCount(); groupIndex++) {
                String groupValue = matcher.group(groupIndex);

                if(groupValue != null && groupValue.endsWith(RegexSearchPatternBuilder.INDEX_DELIMITER)) {
                    wordIndexes.add(Integer.parseInt(groupValue.replaceAll(RegexSearchPatternBuilder.INDEX_DELIMITER, "")));
                }
            }

            result.addResult(sentenceId, wordIndexes);
        }


        return result;
    }
}
