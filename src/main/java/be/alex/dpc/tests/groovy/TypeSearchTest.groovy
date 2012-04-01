package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class TypeSearchTest extends GroovySearchTest {

	@Test
	public void testBasicSearch() {
		testSearch {
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(wordTypeIds: [types.verb], minOccurrences: 1, maxOccurrences: 1)
            terms << new SearchTermUsingIds()

			sentence(true,
                word("hallo", types.noun),
                word("hoe", types.noun),
                word("gaat", types.verb),
                word("het", types.noun)
            )

			sentence(false,
                word("hallo", types.noun)
            )
		}
	}

	@Test
	public void testOrSearch() {
		testSearch {
			terms << new SearchTermUsingIds(wordTypeIds: [types.verb, types.noun ], minOccurrences: 1, maxOccurrences: 1)

			sentence(true, word("hallo", types.noun))

			sentence(true, word("hallo", types.verb))

			sentence(false, word("het", types.article))
		}
	}
}
