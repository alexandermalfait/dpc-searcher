package be.alex.dpc.tests;

import be.alex.dpc.Database;
import be.alex.dpc.Search;
import be.alex.dpc.SearchResult;
import be.alex.dpc.SearchService;
import be.alex.dpc.SearchTerm;

import java.io.IOException;

public class SearchTest {
    public static void main(String[] args) throws IOException {
        SearchService searcher = new SearchService();

        searcher.readData("data.txt");

        Search search = new Search();

        {
            SearchTerm term = new SearchTerm();

            term.setWord("hond");

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
