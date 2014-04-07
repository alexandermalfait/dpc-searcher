package be.alex.dpc.tools;

import be.alex.dpc.Database;
import be.alex.dpc.SentenceDumper;
import be.alex.dpc.Word;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FullSentenceDumper {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    private Database database;

    private File targetFolder;

    public FullSentenceDumper(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    public static void main(String[] args) throws Exception {
        FullSentenceDumper dumper = new FullSentenceDumper(new File("data"));

        dumper.dumpSentences();
    }

    private void dumpSentences() throws IOException {
        database = new Database(null);

        List<String> languages = database.getAllDocumentLanguages();

        for (String language : languages) {
            File targetFile = new File(targetFolder, language + ".txt");

            logger.info("Dumping to " + targetFile.getName());

            Map<Long, Word[]> sentences = database.readDatabase(0, language);

            SentenceDumper sentenceDumper = new SentenceDumper();

            FileWriter fileWriter = new FileWriter(targetFile);

            int numDumped = 0;

            for (Map.Entry<Long, Word[]> entry : sentences.entrySet()) {
                sentenceDumper.dumpSentence(entry.getKey(), entry.getValue(), fileWriter);

                numDumped++;

                if (numDumped % 1000 == 0) {
                    logger.info("Dumped sentence " + numDumped + " of " + sentences.size());
                }
            }

            fileWriter.close();
        }
    }


}
