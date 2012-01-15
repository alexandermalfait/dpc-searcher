package be.alex.dpc.tests;

import be.alex.dpc.MemorySearchService;
import be.alex.dpc.SentenceDumper;
import be.alex.dpc.Word;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class DumpMemoryToFileTest {

    public static void main(String[] args) throws IOException {
        MemorySearchService searcher = new MemorySearchService();

        searcher.readMemoryDump("memory.dump");

        FileWriter writer = new FileWriter("data.txt");

        Map<Long,Word[]> wordsPerSentence = searcher.getWordsPerSentence();

        int offset = 0;

        for(Long sentenceId : wordsPerSentence.keySet()) {
            SentenceDumper dumper = new SentenceDumper();

            dumper.dumpSentence(sentenceId, wordsPerSentence.get(sentenceId), writer);

            offset++;

            if(offset % 5000 == 0) {
                System.out.println("Dumped " + offset);
            }
        }
    }
}
