package be.alex.dpc.tests;

import be.alex.dpc.Database;
import be.alex.dpc.Search;
import be.alex.dpc.SearchResult;
import be.alex.dpc.SearchService;
import be.alex.dpc.SearchTerm;

public class SearchTest {
    public static void main(String[] args) {
        SearchService searcher = new SearchService();

        searcher.readMemoryDump("memory.dump");

        Search search = new Search();

        /*{
            SearchTerm term = new SearchTerm();

            term.setWordTypes(new String[]{ "VG" });

            search.addTerm(term);
        }*/

        {
            SearchTerm term = new SearchTerm();

            term.setLemmas(new String[]{ "hond", "kat"});

            search.addTerm(term);
        }

        Database reader = new Database();

        SearchResult searchResult = searcher.runSearch(search);

        System.out.println(searchResult.getSentenceIds().size());

        for(Long sentenceId : searchResult.getSentenceIds()) {
            System.out.println(reader.getSentenceContents(sentenceId));
            //System.out.println(searchResult.getWordIndexesForSentenceId(sentenceId));
        }
    }
}
