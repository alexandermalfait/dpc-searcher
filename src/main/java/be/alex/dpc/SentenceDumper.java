package be.alex.dpc;

import com.google.common.primitives.Ints;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class SentenceDumper {

    public void dumpSentence(Long sentenceId, Word[] words, Writer writer) throws IOException {
        writer.write(sentenceId.toString());

        writer.write("S");

        int wordIndex = 0;

        for(Word word : words) {
            writer.write("W");
            writer.write(String.valueOf(wordIndex));
            writer.write("I");
            writer.write(String.valueOf(word.getWordId()));
            writer.write("F");
            writer.write(String.valueOf(word.getLemmaId()));
            writer.write("F");
            writer.write(String.valueOf(word.getWordTypeId()));
            writer.write("F");

            List<Integer> flagList = Ints.asList(word.getFlags());

            Collections.sort(flagList);
            
            for(int flag : flagList) {
                writer.write(String.valueOf(flag));
                writer.write("D");
            }

            writer.write("W");

            wordIndex++;
        }

        writer.write('\n');
    }

}
