package be.alex.dpc;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class SearchService {

    private Logger logger = Logger.getLogger("SearchService");

    protected String data;

    public SearchResult runSearch(Search search) {
        RegexSearchExecutor executor = new RegexSearchExecutor(data, search, new Database());

        return executor.getSearchResult();
    }

    public void readData(String fileName) throws IOException {
        data = Files.toString(new File(fileName), Charset.defaultCharset());
        
        logger.info("Finished reading data, " + data.length() + " characters, used memory: " + Util.getUsedMemoryFormatted());
    }
}

