package be.alex.dpc;

import jregex.Matcher;
import jregex.Pattern;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class RegexSearchExecutor {

    private final List<String> lines;

    private final Search search;

    private final Database database;

    private Logger logger = Logger.getLogger("SearchExecutor");

    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
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

        logger.info("Found " + result.getSentenceIds().size() + " search results, used memory " + Util.getUsedMemoryFormatted());

        return result;
    }

    public SearchResult getSearchResult(List<SearchTermUsingIds> convertedSearchTerms) {
        SearchResult result = new SearchResult();

        String regex = new RegexSearchPatternBuilder().buildPattern(convertedSearchTerms);

        Pattern pattern = new Pattern(regex);

        logger.info("Searching using pattern: " + pattern);

        int offset = 0;

        for(String line : lines) {
            Matcher lineMatcher = pattern.matcher(line);

            if(lineMatcher.find()) {
                Long sentenceId = Long.valueOf(lineMatcher.group(1));

                List<Integer> wordIndexes = new ArrayList<Integer>();

                for(int groupIndex = 2; groupIndex < lineMatcher.groupCount(); groupIndex++) {
                    String groupValue = lineMatcher.group(groupIndex);

                    if(groupValue != null && groupValue.endsWith(RegexSearchPatternBuilder.INDEX_DELIMITER)) {
                        int wordIndex = Integer.parseInt(groupValue.replaceAll(RegexSearchPatternBuilder.INDEX_DELIMITER, ""));

                        wordIndexes.add(wordIndex);
                    }
                }
                
                result.addResult(sentenceId, wordIndexes);
            }

            offset++;

            if(offset % 1000 == 0) {
                reportProgress(offset);
            }
        }

        return result;
    }

    private void reportProgress(int offset) {
        logger.info("Checked line " + offset + " of " + lines.size());
        changeSupport.firePropertyChange("progress", null, offset);
    }
    
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }
}
