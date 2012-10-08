package be.alex.dpc;

import com.google.common.io.Files;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchService {

    private final Logger logger = Logger.getLogger("SearchService");
    
    private String progressFileLocation;

    private String dataLocation;

    public SearchResult runSearch(Search search) throws IOException {
        final List<String> lines = new ArrayList<String>();

        for (final String language : search.getLanguages()) {
            String fileName = dataLocation + "/" + language + ".txt";

            writeProgress("Reading " + language);

            lines.addAll(readData(fileName));
        }

        RegexSearchExecutor executor = new RegexSearchExecutor(lines, search, new Database());

        if (progressFileLocation != null) {
            executor.addPropertyChangeListener("progress", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    writeProgress((Integer) evt.getNewValue(), lines.size());
                }
            });
        }

        return executor.getSearchResult();
    }

    @SuppressWarnings("NestedAssignment")
    private List<String> readData(String fileName) throws IOException {
        logger.info("Reading data, used memory: " + Util.getUsedMemoryFormatted());

        List<String> lines = new ArrayList<String>();

        FileReader reader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }

        logger.info("Finished reading data, found " + lines.size() + " sentences, used memory: " + Util.getUsedMemoryFormatted());

        return lines;
    }

    private void writeProgress(int progress, int size) {
        writeProgress(progress + "/" + size);
    }

    private void writeProgress(String message) {
        try {
            Files.write(message, new File(progressFileLocation), Charset.defaultCharset());
        }
        catch(IOException e) {
            logger.log(Level.WARNING, "Couldn't write progress", e);
        }
    }

    public void setProgressFileLocation(String progressFileLocation) {
        this.progressFileLocation = progressFileLocation;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }
}

