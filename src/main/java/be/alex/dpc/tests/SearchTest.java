package be.alex.dpc.tests;

import be.alex.dpc.Search;
import be.alex.dpc.SearchResult;
import be.alex.dpc.SearchService;
import be.alex.dpc.SearchTerm;

import java.io.IOException;

public class SearchTest {
    public static void main(String[] args) throws IOException {
        SearchService searcher = new SearchService();

        searcher.readData("data.txt");
        searcher.setProgressFileLocation("progress.txt");

        Search search = new Search();

        {
            SearchTerm term = new SearchTerm();
            term.setWord("hebben");
            term.setFlags(new String[]{"pv"});
            search.addTerm(term);
        }

        {
            SearchTerm term = new SearchTerm();
            term.setWordTypes(new String[] { "ww"});
            term.setFlags(new String[]{"inf","pv"});
            term.setMaximumDistanceFromLastMatch(2);
            search.addTerm(term);
        }

        {
            SearchTerm term = new SearchTerm();
            term.setWordTypes(new String[] { "ww"});
            term.setLastInSentence(true);
            search.addTerm(term);
        }

        SearchResult searchResult = searcher.runSearch(search);

        System.out.println(searchResult.getSentenceIds().size());

        for(Long sentenceId : searchResult.getSentenceIds()) {
            //System.out.println(reader.getSentenceContents(sentenceId));
            //System.out.println(searchResult.getWordIndexesForSentenceId(sentenceId));
        }
    }
}
