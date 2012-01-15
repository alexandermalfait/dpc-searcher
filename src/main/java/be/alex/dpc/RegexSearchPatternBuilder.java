package be.alex.dpc;

import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class RegexSearchPatternBuilder {
    public static final String SENTENCE_DELIMITER = "S";

    public static final String WORD_DELIMITER = "W";
    
    public static final String INDEX_DELIMITER = "I";

    public static final String FIELD_DELIMITER = "F";

    public static final String FIELD_SUB_DELIMITER = "D";

    Pattern buildPattern(List<SearchTermUsingIds> terms) {
        String regex = "^(\\d+)" + SENTENCE_DELIMITER;

        for(SearchTermUsingIds term : terms) {
            if(term.isExcludeTerm()) {
                regex += "(?!.*";
            }
            else {
                if(term.getMaximumDistanceFromLastMatch() != null) {
                    for(int i = 0; i < term.getMaximumDistanceFromLastMatch() - 1; i++) {
                        regex += "(W[" + INDEX_DELIMITER + FIELD_DELIMITER + FIELD_SUB_DELIMITER + "\\d]+W)?";
                    }
                }
                else {
                    if(!term.isFirstInSentence()) {
                        regex += ".*";
                    }
                }
            }

            regex += buildWordMatch(term);

            if(term.isExcludeTerm()) {
                if(term.isLastInSentence()) {
                    regex += "$";
                }

                regex += ")";
            }
            else {
                if(term.isLastInSentence()) {
                    SearchTermUsingIds dotTerm = new SearchTermUsingIds();
                    
                    dotTerm.setWordTypeIds(new byte[] { 6 });

                    regex += "(" + buildWordMatch(dotTerm) + ")?";
                    
                    regex += "$";
                }
            }
        }

        return Pattern.compile(regex);
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    private String buildWordMatch(SearchTermUsingIds term) {
        String regex = "";
        
        regex += WORD_DELIMITER;
        
        regex += "(\\d+" + INDEX_DELIMITER + ")";
        
        if(term.getWordIds() != null) {
            regex += "(" + Joiner.on('|').join(appendToAll(Ints.asList(term.getWordIds()), FIELD_DELIMITER)) + ")";
        }
        else {
            regex += "\\d+" + FIELD_DELIMITER;
        }

        if(term.getLemmaIds() != null) {
            regex += "(" + Joiner.on('|').join(appendToAll(Ints.asList(term.getLemmaIds()), FIELD_DELIMITER)) + ")";
        }
        else {
            regex += "\\d+" + FIELD_DELIMITER;
        }

        if(term.getWordTypeIds() != null) {
            regex += "(" + Joiner.on('|').join(appendToAll(Bytes.asList(term.getWordTypeIds()), FIELD_DELIMITER)) + ")";
        }
        else {
            regex += "\\d+" + FIELD_DELIMITER;
        }

        if(term.getExcludeFlagIds() != null) {
            regex += "(?!(" + Joiner.on('|').join(appendToAll(Bytes.asList(term.getExcludeFlagIds()), FIELD_SUB_DELIMITER)) + "))";
        }
        
        if(term.getFlagIds() != null) {
            List<Byte> flagList = Bytes.asList(term.getFlagIds());

            Collections.sort(flagList);
            
            if(term.isFlagsOrMode()) {
                regex += "(" + Joiner.on('|').join(appendToAll(flagList, FIELD_SUB_DELIMITER)) + ")";
            }
            else {
                for(byte flagId : term.getFlagIds()) {
                    regex += flagId + FIELD_SUB_DELIMITER + ".*?";
                }
            }
        }
        regex += "[\\d"+ FIELD_SUB_DELIMITER + "]*";

        regex += WORD_DELIMITER;

        return regex;
    }

    List<String> appendToAll(List<?> objects, String append) {
        List<String> result = new ArrayList<String>();

        for(Object object : objects) {
            result.add(object.toString() + append);
        }

        return result;
    }
}