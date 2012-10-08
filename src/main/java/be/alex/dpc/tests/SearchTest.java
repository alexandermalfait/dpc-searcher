package be.alex.dpc.tests;

import be.alex.dpc.Search;
import be.alex.dpc.SearchResult;
import be.alex.dpc.SearchService;
import be.alex.dpc.SearchTerm;

import java.io.IOException;

public class SearchTest {
    public static void main(String[] args) throws IOException {
        SearchService searcher = new SearchService();

        searcher.setDataLocation("data");

        searcher.setProgressFileLocation("progress.txt");

        Search search = new Search();

        /*search.getLanguages().add("NL-NL");
        search.getLanguages().add("NL-BE");*/
        search.getLanguages().add("EN-UK");
        /*search.getLanguages().add("FR-BE");
        search.getLanguages().add("FR-FR");
        search.getLanguages().add("EN-US");
        search.getLanguages().add("NL");*/

        {
            SearchTerm term = new SearchTerm();
            search.addTerm(term);
        }

        {
            SearchTerm term = new SearchTerm();
            term.setWordTypes(new String[] {"TO"});
            search.addTerm(term);
        }

        {
            SearchTerm term = new SearchTerm();
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
