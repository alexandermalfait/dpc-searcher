package be.alex.dpc.tests.groovy

import java.util.regex.Matcher
import be.alex.dpc.*

abstract class GroovySearchTest extends GroovyTestCase {

	int nounType
	int verbType

	List<SearchTermUsingIds> terms

	private long currentSentenceId
	private int currentWordStringId

	private List<Long> expectedMatches

	Map<Long,Word[]> wordsPerSentence

	Map<String, Integer> wordStrings

	Map<String, Integer> types = [
        "undefined": (int) 0, "noun": (int) 1, "verb": (int) 11, "article": (int) 12,
        "dot": (int) 6, vg: (int) 7, aanw: (int) 8, vz: (int) 9
    ]

	Map<String, Integer> flags = [
        "properName": (int) 1, "pastParticiple": (int) 11, "flag3": (int) 12, "flag4": (int) 13,
        "pv": (int) 20, "tgw": (int) 21, "mv": (int) 22, "soort": (int) 23, "basis": (int) 24,
        pron: (int) 25, stan: (int) 26, vol: (int) 27
    ]

	SearchResult lastResult

	@Override
	protected void setUp() {
		currentSentenceId = 0

		wordsPerSentence = [:]
		terms = []
		expectedMatches = []
		wordStrings = [:]
	}

	protected void testSearch(Closure definition) {
		definition.call()

		RegexSearchExecutor executor = new RegexSearchExecutor(dumpSentences(), null, null)

		lastResult = executor.getSearchResult(terms)

		if(lastResult.getSentenceIds() != expectedMatches) {
			failWithUnexpectedSearchResult(lastResult)
		}
	}

	private List<String> dumpSentences() {
		StringWriter writer = new StringWriter()
		SentenceDumper dumper = new SentenceDumper()

		wordsPerSentence.each { long sentenceId, Word[] words ->
			dumper.dumpSentence(sentenceId, words, writer)
		}

		log.info("Dumped: " + writer.toString())

		return writer.toString().split("\n")
	}

	private def failWithUnexpectedSearchResult(SearchResult result) {
		String message = "Unexpected search result.\n\n"

		message += "Search:\n"

		terms.each {
			message += "- " + formatTerm(it) + "\n"
		}

		message += "\n"

		message += "Expected:\n"

		if(expectedMatches) {
			expectedMatches.each { sentenceId ->
				message += "- ${formatSentence(sentenceId)}\n"
			}
		}
		else {
			message += "NONE"
		}

		message += "\n\n"

		message += "Found:\n"

		if(result.getSentenceIds()) {
			result.getSentenceIds().each { sentenceId ->
				message += "- ${formatSentence(sentenceId)}\n"
			}
		}
		else {
			message += "NONE"
		}

		message += "\n\n"

		fail(message)
	}

	def formatTerm(SearchTermUsingIds term) {
		List parts = []

		if(term.wordIds) {
			parts << "word = " + term.wordIds.collect { getWordString(it) }.join('|')
		}

		if(term.wordTypeIds) {
			parts << "type = " + term.wordTypeIds.collect { formatType((int) it) }.join('|')
		}

		if(term.invertTerm) {
			parts << "!inverted!"
		}

		parts.join(" && ")
	}

	def formatSentence(long sentenceId) {
		wordsPerSentence[sentenceId].collect { Word word -> formatWord(word) }.join(" ")
	}

	def formatWord(Word word) {
		String formatted = getWordString(word.wordId)

		if(word.wordTypeId > 0) {
			formatted += " (" + formatType(word.wordTypeId) + ")"
		}

		formatted
	}

	String getWordString(int wordId) {
		return wordStrings.keySet().find { wordStrings[it] == wordId }
	}

	String formatType(int wordTypeId) {
		return types.keySet().find { types[it] == wordTypeId }
	}

	protected long sentence(boolean shouldMatch, Word... words) {
		currentSentenceId++

		wordsPerSentence[currentSentenceId] = words

		if(shouldMatch) {
			expectedMatches << currentSentenceId
		}

		return currentSentenceId
	}

	protected long sentence(boolean shouldMatch, String sentenceString) {
		return sentence(shouldMatch, sentenceString.split(" ").collect { word(it) } as Word[])
	}

	Word word(String word, int type = 0, int... flags) {
        Arrays.sort(flags, 0, flags.length)

		return new Word(wordId: getWordId(word), wordTypeId: type, flags: flags)
	}
    
    Word buildWord(String data) {
        Matcher matcher = data =~ /(.+) ([A-Z]+)\(([a-z\d,\-]*);(.+?)\)/

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Couldn't parse: $data")
        }
        
        return new Word(
            wordId: getWordId(matcher.group(1)),
            wordTypeId: getWordTypeId(matcher.group(2)),
            flags: matcher.group(3).split(",").collect { getFlagId(it) },
        )
    }

	def getWordId(String word) {
		if(!wordStrings[word]) {
			wordStrings[word] = (wordStrings.values().max() ?: 0) + 1
		}

		wordStrings[word]
	}

    int getWordTypeId(String wordType) {
        if(!types[wordType]) {
            types[wordType] = (int) ( (types.values().max() ?: 0) + 1 )
        }

        types[wordType]
    }

    int getFlagId(String flagName) {
        if(!flags[flagName]) {
            flags[flagName] = (int) (  (flags.values().max() ?: 0) + 1 )
        }
        
        flags[flagName]
    }
}
