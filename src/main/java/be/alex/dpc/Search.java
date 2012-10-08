package be.alex.dpc;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private List<String> languages = new ArrayList<String>();

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    private List<SearchTerm> terms = new ArrayList<SearchTerm>();

    public void addTerm(SearchTerm term) {
        terms.add(term);
    }

    public List<SearchTerm> getTerms() {
        return terms;
    }
}
