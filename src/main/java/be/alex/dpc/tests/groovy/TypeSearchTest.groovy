package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class TypeSearchTest extends GroovySearchTest {

	@Test
	public void testBasicSearch() {
		testSearch {
			terms << new SearchTermUsingIds(wordTypeIds : [ types.verb ] )

			sentence(true, word("hallo", types.noun), word("hoe", types.noun), word("gaat", types.verb), word("het", types.noun))
			sentence(false, word("hallo", types.noun))
		}
	}
}
