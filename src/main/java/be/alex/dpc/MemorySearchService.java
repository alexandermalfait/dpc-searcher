package be.alex.dpc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MemorySearchService {

    private Logger logger = Logger.getLogger("SearchService");

    private Map<Long, Word[]> wordsPerSentence = new HashMap<Long, Word[]>();


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

    public Map<Long, Word[]> getWordsPerSentence() {
        return wordsPerSentence;
    }
}

