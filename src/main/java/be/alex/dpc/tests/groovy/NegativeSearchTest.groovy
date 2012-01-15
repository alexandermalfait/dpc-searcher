package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class NegativeSearchTest extends GroovySearchTest {

	@Test
	public void testSimpleExclusion() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [getWordId("de")])
			terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], maximumDistanceFromLastMatch: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("krollen")], excludeTerm: true)
			terms << new SearchTermUsingIds(wordIds: [getWordId("trap")])

			sentence(
				false, "de kat krabt de krollen van de trap"
			)

			sentence(
				true, "de kat krabt de bollen van de trap"
			)
		}
	}

	@Test
	public void testExclusionWithSkip() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [getWordId("de")])
			terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], maximumDistanceFromLastMatch: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("krabt")])
			terms << new SearchTermUsingIds(wordIds: [getWordId("krollen")], excludeTerm: true)
			terms << new SearchTermUsingIds(wordIds: [getWordId("trap")])

			sentence(
				false, "de kat krabt de krollen van de trap"
			)

			sentence(
				true, "de kat krabt de bollen van de trap"
			)

			sentence(
				true, "de kat krabt de krollen van de trap en de kat krabt ook de bollen van de trap"
			)

			sentence(
				true, "de kat krabt de krollen van de trap en de stoute kat krabt ook de bollen van de trap"
			)
		}
	}

	@Test
	public void testExclusionAtEnd() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [getWordId("krabt")])
			terms << new SearchTermUsingIds(wordIds: [getWordId("krollen")])
			terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], excludeTerm: true, lastInSentence: true)

			sentence(
				false, "de kat krabt de krollen van de trap"
			)

			sentence(
				false, "de kat krabt de bollen van de zetel en de trap"
			)

			sentence(
				true, "de kat krabt de krollen van de trap en de kat krabt ook de bollen van de wereld"
			)

			sentence(
				false, "de kat krabt de krollen van de trap en de stoute kat krabt ook de bollen van de trap"
			)
		}
	}
}
