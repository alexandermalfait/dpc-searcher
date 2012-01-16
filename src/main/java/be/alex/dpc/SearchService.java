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

    private Logger logger = Logger.getLogger("SearchService");
    
    private String progressFileLocation;

    protected List<String> lines;
    
    public SearchResult runSearch(Search search) {
        RegexSearchExecutor executor = new RegexSearchExecutor(lines, search, new Database());

        if(progressFileLocation != null) {
            executor.addPropertyChangeListener("progress", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    writeProgress((Integer) evt.getNewValue());
                }
            });
        }
        
        return executor.getSearchResult();
    }

    private void writeProgress(Integer progress) {
        try {
            Files.write(progress + "/" + lines.size(), new File(progressFileLocation), Charset.defaultCharset());
        }
        catch(IOException e) {
            logger.log(Level.WARNING, "Couldn't write progress", e);
        }
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

    public void setProgressFileLocation(String progressFileLocation) {
        this.progressFileLocation = progressFileLocation;
    }
}

