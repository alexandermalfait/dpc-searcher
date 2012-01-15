package be.alex.dpc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchService {

    private Logger logger = Logger.getLogger("SearchService");

    protected List<String> lines;

    public SearchResult runSearch(Search search) {
        RegexSearchExecutor executor = new RegexSearchExecutor(lines, search, new Database());

        return executor.getSearchResult();
    }

    public void readData(String fileName) throws IOException {
        lines = new ArrayList<String>();

        FileReader reader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(reader);
        
        String line = null;
        
        while((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }

        logger.info("Finished reading data, found " + lines.size() + " sentences, used memory: " + Util.getUsedMemoryFormatted());
    }
}

