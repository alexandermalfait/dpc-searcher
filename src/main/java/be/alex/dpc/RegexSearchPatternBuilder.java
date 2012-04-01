package be.alex.dpc;

import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;

public class RegexSearchPatternBuilder {

    String buildPattern(List<SearchTermUsingIds> terms) {
        String regex = "^(\\d+)S";
        
        for(SearchTermUsingIds term : terms) {
            boolean collectMatchIndex = term.getMinOccurrences() == 1 && term.getMaxOccurrences() != null && term.getMaxOccurrences() == 1;

            regex += "(" + buildWordMatch(term, collectMatchIndex) + ")";

            regex += "{" + term.getMinOccurrences() + ",";

            if (term.getMaxOccurrences() != null) {
                regex += term.getMaxOccurrences();
            }

            regex += "}";
        }

        regex += "$";

        return regex;
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    private String buildWordMatch(SearchTermUsingIds term, boolean collectMatchIndex) {
        String regex = "";
        
        regex += "W";

        if (collectMatchIndex) {
            regex += "(\\d+I)";
        }
        else {
            regex += "\\d+I";
        }

        if(term.getWordIds() != null) {
            List<String> wordsWithDelimiter = appendToAll(Ints.asList(term.getWordIds()), "F");

            if (term.isInvertTerm()) {
                regex += "(?!" + Joiner.on('|').join(wordsWithDelimiter) + ")";

                regex += "\\d+F";
            }
            else {
                regex += "(" + Joiner.on('|').join(wordsWithDelimiter) + ")";
            }
        }
        else {
            regex += "\\d+F";
        }

        if(term.getLemmaIds() != null) {
            List<String> lemmasWithDelimiter = appendToAll(Ints.asList(term.getLemmaIds()), "F");

            if (term.isInvertTerm()) {
                regex += "(?!" + Joiner.on('|').join(lemmasWithDelimiter) + ")";

                regex += "\\d+F";
            }
            else {
                regex += "(" + Joiner.on('|').join(lemmasWithDelimiter) + ")";
            }
        }
        else {
            regex += "\\d+F";
        }

        if(term.getWordTypeIds() != null) {
            if (term.isInvertTerm()) {
                regex += "(?!" + Joiner.on('|').join(Bytes.asList(term.getWordTypeIds())) + ")";

                regex += "\\d+F";
            }
            else {
                regex += "(" + Joiner.on('|').join(Bytes.asList(term.getWordTypeIds())) + ")" + "F";
            }

        }
        else {
            regex += "\\d+F";
        }

        if(term.getExcludeFlagIds() != null) {
            regex += buildFlagSearch(term.getExcludeFlagIds(), term.isExcludeFlagsOrMode(), ! term.isInvertTerm());
        }
        
        if(term.getFlagIds() != null) {
            regex += buildFlagSearch(term.getFlagIds(), term.isFlagsOrMode(), term.isInvertTerm());
        }

        if(term.getExcludeFlagIds() == null && term.getFlagIds() == null) {
            regex += "(?:\\d+D)*";
        }

        regex += "W";

        return regex;
    }

    private String buildFlagSearch(byte[] flagIds, boolean orMode, boolean inverted) {
        List<String> flagsWithDelimiter = appendToAll(Bytes.asList(flagIds), "D");

        String regex = "";

        if (inverted) {
            regex += "(?!";
        }
        else {
            regex += "(?:";
        }

        regex += "(\\d+D)*";

        if(orMode) {
            regex += Joiner.on('|').join(flagsWithDelimiter);
        }
        else {
            regex += Joiner.on("(\\d+D)*").join(flagsWithDelimiter);
        }

        regex += "(\\d+D)*";

        regex += ")[\\dD]*";

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