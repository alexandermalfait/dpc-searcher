package be.alex.dpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchTermConvertor {
    private final Database database;

    public SearchTermConvertor(Database database) {
        this.database = database;
    }

    public List<SearchTermUsingIds> convertSearchTermsToTermsUsingIds(List<SearchTerm> terms) {
        List<SearchTermUsingIds> convertedTerms = new ArrayList<SearchTermUsingIds>();

        for(SearchTerm term : terms) {
            SearchTermUsingIds convertedTerm = new SearchTermUsingIds();

            copyBasicData(term, convertedTerm);

            lookupWords(term, convertedTerm);

            lookupLemmas(term, convertedTerm);

            lookupFlags(term, convertedTerm);

            lookupExcludeFlags(term, convertedTerm);

            lookupWordTypes(term, convertedTerm);

            convertedTerms.add(convertedTerm);
        }

        return convertedTerms;
    }

    private void lookupWordTypes(SearchTerm term, SearchTermUsingIds convertedTerm) {
        if(term.getWordTypes() != null) {
            byte[] wordTypeIds = new byte[term.getWordTypes().length];

            for(int w = 0; w < term.getWordTypes().length; w++) {
                wordTypeIds[w] = database.getWordTypeId(term.getWordTypes()[w]);
            }

            convertedTerm.setWordTypeIds(wordTypeIds);
        }
    }

    private void lookupExcludeFlags(SearchTerm term, SearchTermUsingIds convertedTerm) {
        if(term.getExcludeFlags() != null) {
            byte[] flagIds = new byte[term.getExcludeFlags().length];

            for(int f = 0; f < term.getExcludeFlags().length; f++) {
                flagIds[f] = database.getFlagId(term.getExcludeFlags()[f]);
            }

            convertedTerm.setExcludeFlagIds(flagIds);

            convertedTerm.setExcludeFlagsOrMode(term.isExcludedFlagsOrMode());
        }
    }

    private void lookupFlags(SearchTerm term, SearchTermUsingIds convertedTerm) {
        if(term.getFlags() != null) {
            byte[] flagIds = new byte[term.getFlags().length];

            for(int f = 0; f < term.getFlags().length; f++) {
                flagIds[f] = database.getFlagId(term.getFlags()[f]);
            }

            Arrays.sort(flagIds);

            convertedTerm.setFlagIds(flagIds);
        }
    }

    private void lookupLemmas(SearchTerm term, SearchTermUsingIds convertedTerm) {
        if(term.getLemmas() != null) {
            int[] lemmaIds = new int[term.getLemmas().length];

            for(int i = 0; i < term.getLemmas().length; i++) {
                String lemma = term.getLemmas()[i];

                lemmaIds[i] = database.getLemmaId(lemma);
            }

            convertedTerm.setLemmaIds(lemmaIds);
        }
    }

    private void lookupWords(SearchTerm term, SearchTermUsingIds convertedTerm) {
        if(term.getWord() != null) {
            if(term.isWordRegex()) {
                convertedTerm.setWordIds(database.getWordIdsUsingRegex(term.getWord()));
            }
            else {
                convertedTerm.setWordIds(new int[] { database.getWordId(term.getWord()) } );
            }
        }
    }

    private void copyBasicData(SearchTerm term, SearchTermUsingIds convertedTerm) {
        convertedTerm.setFirstInSentence(term.isFirstInSentence());
        convertedTerm.setLastInSentence(term.isLastInSentence());
        convertedTerm.setMaximumDistanceFromLastMatch(term.getMaximumDistanceFromLastMatch());
        convertedTerm.setFlagsOrMode(term.isFlagsOrMode());
        convertedTerm.setExcludeTerm(term.isExcludeTerm());
    }

}
