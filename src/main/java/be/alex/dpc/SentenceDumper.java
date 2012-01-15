package be.alex.dpc;

import com.google.common.primitives.Bytes;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import static be.alex.dpc.RegexSearchPatternBuilder.*;

public class SentenceDumper {

    public void dumpSentence(Long sentenceId, Word[] words, Writer writer) throws IOException {
        writer.write(sentenceId.toString());

        writer.write(SENTENCE_DELIMITER);

        int wordIndex = 0;

        for(Word word : words) {
            writer.write(WORD_DELIMITER);
            writer.write(String.valueOf(wordIndex));
            writer.write(INDEX_DELIMITER);
            writer.write(String.valueOf(word.getWordId()));
            writer.write(FIELD_DELIMITER);
            writer.write(String.valueOf(word.getLemmaId()));
            writer.write(FIELD_DELIMITER);
            writer.write(String.valueOf((int) word.getWordTypeId()));
            writer.write(FIELD_DELIMITER);

            List<Byte> flagList = Bytes.asList(word.getFlags());

            Collections.sort(flagList);
            
            for(byte flag : flagList) {
                writer.write(String.valueOf((int) flag));
                writer.write(FIELD_SUB_DELIMITER);
            }

            writer.write(WORD_DELIMITER);

            wordIndex++;
        }

        writer.write('\n');
    }

}
