package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class DistanceSearchTest extends GroovySearchTest {
	@Test
	public void testMaximumDistance() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [ getWordId("de") ] )
			terms << new SearchTermUsingIds(wordIds: [ getWordId("kat") ], maximumDistanceFromLastMatch: 1 )

			sentence(
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

			sentence(
				false,
				word("de"),
				word("stoute"),
				word("kat"),
				word("piept"),
				word("er"),
				word("op"),
				word("los"),
			)
		}
	}

	@Test
	public void testMaximumDistanceWithMatchSkip() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [ getWordId("de") ] )
			terms << new SearchTermUsingIds(wordIds: [ getWordId("kat") ], maximumDistanceFromLastMatch: 1 )

			sentence(
				true,
				word("de"),
				word("stoute"),
				word("kat"),
				word("klopt"),
				word("op"),
				word("de"),
				word("kat"),
			)
		}
	}
}