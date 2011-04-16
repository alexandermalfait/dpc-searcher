package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private List<SearchTerm> terms = new ArrayList<SearchTerm>();

    public void addTerm(SearchTerm term) {
        terms.add(term);
    }

    public List<SearchTerm> getTerms() {
        return terms;
    }
}
