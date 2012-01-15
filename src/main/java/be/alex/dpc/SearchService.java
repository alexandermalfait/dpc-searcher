package be.alex.dpc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SearchService {

    private Logger logger = Logger.getLogger("SearchService");

    private Map<Long, Word[]> wordsPerSentence = new HashMap<Long, Word[]>();

    public SearchResult runSearch(Search search) {
        SearchExecutor executor = new SearchExecutor(wordsPerSentence, search, new Database());

        return executor.getSearchResult();
    }


    public void dumpMemory(String targetFile) {
        try {
            logger.info("Dumping memory");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(targetFile)));

            objectOutputStream.writeObject(wordsPerSentence);

            objectOutputStream.close();

            logger.info("Finished dumping memory, used memory: " + Util.getUsedMemoryFormatted());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public void readMemoryDump(String sourceFile) {
        try {
            logger.info("Reading memory dump");

            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(sourceFile))));

            wordsPerSentence = (Map<Long, Word[]>) objectInputStream.readObject();

            objectInputStream.close();

            System.gc();

            logger.info("Finished reading memory dump, found " + wordsPerSentence.size() + " sentences, used memory: " + Util.getUsedMemoryFormatted());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void readDatabase(int limit) {
        Database reader = new Database();

        wordsPerSentence = reader.readDatabase(limit);

        System.gc();

        logger.info("Finished reading " + wordsPerSentence.size() + " sentences, used memory: " + Util.getUsedMemoryFormatted());
    }

    public void readDatabase() {
        readDatabase(0);
    }

    public Map<Long, Word[]> getWordsPerSentence() {
        return wordsPerSentence;
    }
}

