package be.alex.dpc;

public class MemoryWordMatcher {
    private final Word word;

    private final SearchTermUsingIds searchTerm;

    public MemoryWordMatcher(Word word, SearchTermUsingIds searchTerm) {
        this.word = word;
        this.searchTerm = searchTerm;
    }

    public boolean matches() {
        if(searchTerm.getWordIds() != null) {
            if(! IntArrays.contains(searchTerm.getWordIds(), word.getWordId())) {
                return false;
            }
        }

        if(searchTerm.getLemmaIds() != null) {
            if(! IntArrays.contains(searchTerm.getLemmaIds(), word.getLemmaId())) {
                return false;
            }
        }

        if(searchTerm.getWordTypeIds() != null) {
            boolean wordTypeMatches = false;

            for(byte wordTypeId : searchTerm.getWordTypeIds()) {
                if(word.getWordTypeId() == wordTypeId) {
                    wordTypeMatches = true;
                    break;
                }
            }

            if(!wordTypeMatches) {
                return false;
            }
        }

        if(searchTerm.getFlagIds() != null) {
            if(searchTerm.isFlagsOrMode()) {
                boolean anyFlagMatch = false;

                for(byte searchFlag : searchTerm.getFlagIds()) {
                    for(byte wordFlag : word.getFlags()) {
                        if(searchFlag == wordFlag) {
                            anyFlagMatch = true;
                            break;
                        }
                    }
                }

                if(! anyFlagMatch) {
                    return false;
                }
            }
            else {
                for(byte searchFlag : searchTerm.getFlagIds()) {
                    boolean anyFlagMatch = false;

                    for(byte wordFlag : word.getFlags()) {
                        if(searchFlag == wordFlag) {
                            anyFlagMatch = true;
                        }
                    }

                    if(!anyFlagMatch) {
                        return false;
                    }
                }
            }
        }

        if(searchTerm.getExcludeFlagIds() != null) {
            for(byte searchFlag : searchTerm.getExcludeFlagIds()) {
                for(byte wordFlag : word.getFlags()) {
                    if(searchFlag == wordFlag) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
