package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchExecutor
import be.alex.dpc.SearchResult
import be.alex.dpc.SearchTermUsingIds
import be.alex.dpc.Word

abstract class GroovySearchTest extends GroovyTestCase {

	byte nounType
	byte verbType

	List<SearchTermUsingIds> terms

	private long currentSentenceId
	private int currentWordStringId

	private List<Long> expectedMatches

	Map wordsPerSentence

	Map<String, Integer> wordStrings

	Map<String, Byte> types = ["undefined" : (byte) 0, "noun" : (byte) 1, "verb": (byte) 2, "article": (byte) 3, "article": (byte) 3]

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

		SearchExecutor executor = new SearchExecutor(wordsPerSentence, null, null)

		SearchResult result = executor.getSearchResult(terms)

		if(result.getSentenceIds() != expectedMatches) {
			failWithUnexpectedSearchResult(result)
		}
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
		} else {
			message += "NONE"
		}

		message += "\n\n"

		message += "Found:\n"

		if(result.getSentenceIds()) {
			result.getSentenceIds().each { sentenceId ->
				message += "- ${formatSentence(sentenceId)}\n"
			}
		} else {
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
			parts << "type = " + term.wordTypeIds.collect { formatType((byte)it) }.join('|')
		}

		if(term.excludeTerm) {
			parts << "!excluded!"
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

	String formatType(byte wordTypeId) {
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

	Word word(String word, byte type = 0) {
		return new Word(wordId: getWordId(word), wordTypeId: type)
	}

	def getWordId(String word) {
		if(!wordStrings[word]) {
			wordStrings[word] = (wordStrings.values().max() ?: 0) + 1
		}

		wordStrings[word]
	}
}
