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

	@Test
	public void testGreediness() {
		testSearch {
			terms << new SearchTermUsingIds(wordTypeIds: [ types.verb ])
			terms << new SearchTermUsingIds(wordTypeIds: [ types.verb ])
			terms << new SearchTermUsingIds(wordTypeIds: types.values().findAll { it != types.verb }, maximumDistanceFromLastMatch: 1)
			terms << new SearchTermUsingIds(wordTypeIds: [ types.verb ], maximumDistanceFromLastMatch: 1, lastInSentence: true)

			sentence(
				true,
				word("Maar"),
				word("we"),
				word("zijn",types.verb),
				word("ook"),
				word("één"),
				word("in"),
				word("onze"),
				word("overtuiging"),
				word("dat"),
				word("we"),
				word("meer"),
				word("kunnen", types.verb),
				word("doen", types.verb),
				word("dan"),
				word("meeleven", types.verb)
			)
		}
	}
}