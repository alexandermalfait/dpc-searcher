package be.alex.dpc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Database {

    private Logger logger = Logger.getLogger("DatabaseReader");

    private Connection connection;

    public Database() {
        connect();
    }

    private void connect() {
        try {
            logger.info("Connecting to database");

            connection = DriverManager.getConnection("jdbc:postgresql:dpc", "postgres", "olifant");
            connection.setAutoCommit(false);
        }
        catch(SQLException e) {
            throw (new RuntimeException(e));
        }
    }

    @SuppressWarnings({"ConstantConditions"})
    public Map<Long, Word[]> readDatabase(int limit) {
        logger.info("Fetching data from db...");

        try {
            connection.createStatement().execute("SET enable_seqscan = off;");
            connection.createStatement().execute("SET enable_sort = off;");
            
            Statement statement = connection.createStatement();
            statement.setFetchSize(10000);

            String query =
                "SELECT " +
                    "word.sentence_id AS sentence_id, " +
                    "word.id AS word_id, word.word_id AS word_word_id, word.lemma_id AS word_lemma_id, word.word_type_id AS word_type_id, " +
                    "word_flag.flag_id AS flag_id " +
                "FROM word " +
                "LEFT JOIN word_flag ON word.id = word_flag.word_id " +
                "ORDER BY sentence_id, word.position ";

            if(limit > 0) {
                query += "LIMIT " + limit;
            }

            ResultSet rs = statement.executeQuery(query);

            Map<Long, Word[]> wordsPerSentence = new HashMap<Long, Word[]>();

            int offset = 0;

            long lastWordId = 0;

            Word word = null;

            long wordId;

            long lastSentenceId = 0;

            List<Word> words = null;

            List<Byte> flags = new ArrayList<Byte>();

            while(rs.next()) {
                long sentenceId = rs.getLong("sentence_id");

                if(sentenceId != lastSentenceId) {
                    if(words != null) {
                        Word[] wordsArray = new Word[words.size()];
                        words.toArray(wordsArray);
                        wordsPerSentence.put(lastSentenceId, wordsArray);
                    }

                    words = new ArrayList<Word>();

                    lastSentenceId = sentenceId;
                }

                wordId = rs.getLong("word_id");

                if(wordId != lastWordId) {
                    if(word != null) {
                        byte[] flagsArray = new byte[flags.size()];

                        for(int f = 0; f < flags.size(); f++) {
                            flagsArray[f] = flags.get(f);
                        }

                        word.setFlags(flagsArray);
                    }

                    word = new Word();

                    word.setWordId(rs.getInt("word_word_id"));
                    word.setLemmaId(rs.getInt("word_lemma_id"));
                    word.setWordTypeId(rs.getByte("word_type_id"));

                    words.add(word);

                    lastWordId = wordId;

                    flags = new ArrayList<Byte>();
                }

                Byte flag = rs.getByte("flag_id");


                if(flag > 0) {
                    flags.add(flag);
                }

                offset++;

                if(offset % 10000 == 0) {
                    logger.info("Read " + wordsPerSentence.size() + " sentences, used memory: " + Util.getUsedMemoryFormatted());
                }
            }

            rs.close();

            return wordsPerSentence;
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWordId(String word) {
        return (int) findIdByQuery("SELECT id FROM word_word WHERE word = ?", word.toLowerCase());
    }

    private long findIdByQuery(String query, String word) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, word.toLowerCase());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getLong(1);
            }
            else {
                return -1;
            }
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSentenceContents(long sentenceId) {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT original FROM sentence WHERE id = " + sentenceId);

            resultSet.next();

            return resultSet.getString("original");
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public int getLemmaId(String lemma) {
        return (int) findIdByQuery("SELECT id FROM lemma WHERE lemma = ?", lemma.toLowerCase());
    }

    public byte getFlagId(String flag) {
        return (byte) findIdByQuery("SELECT id FROM flag WHERE name = ?", flag.toLowerCase());
    }

    public byte getWordTypeId(String type) {
        return (byte) findIdByQuery("SELECT id FROM word_type WHERE name = ?", type.toLowerCase());
    }

    public int[] getWordIdsUsingRegex(String regex) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM word_word WHERE word ~ ?");

            statement.setString(1, regex);

            ResultSet resultSet = statement.executeQuery();

            List<Integer> ids = new ArrayList<Integer>();

            while(resultSet.next()) {
                ids.add((int) resultSet.getLong(1));
            }

            int[] idsArray = new int[ids.size()];

            for(int i = 0; i < ids.size(); i++) {
                idsArray[i] = ids.get(i);
            }

            return idsArray;
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getDocumentId(String documentName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM document WHERE filename = ?");

            statement.setString(1, documentName);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getLong("id");
            }
            else {
                return -1;
            }
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSentenceOriginal(long documentId, int position, String text) {
        try {
            logger.info("Updating " + position + " to " + text);

            PreparedStatement statement = connection.prepareStatement("UPDATE sentence SET original = ? WHERE document_id = ? AND position = ?");

            statement.setString(1, text);
            statement.setLong(2, documentId);
            statement.setInt(3, position);

            statement.executeUpdate();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitChanges() {
        try {
            connection.commit();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
