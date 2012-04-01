package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class MatchIndexesTest extends GroovySearchTest {

	@Test
	public void testIndexes() {
		long sentenceId = 0
		
		testSearch {
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(wordIds: [getWordId("de")], minOccurrences: 1, maxOccurrences: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], minOccurrences: 1, maxOccurrences: 1)
            terms << new SearchTermUsingIds()

			sentenceId = sentence(
				true,
				word("de"),
				word("kat"),
				word("krabt"),
				word("de"),
				word("krollen"),
				word("van"),
				word("de"),
				word("trap"),
			)
		}
		
		assert lastResult.getWordIndexesForSentenceId(sentenceId) == [0, 1]
	}
}
