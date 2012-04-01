package be.alex.dpc.tests.groovy

import org.junit.Test
import be.alex.dpc.SearchTermUsingIds

class OccurencesTest extends GroovySearchTest {

    @Test
    public void testMinMaxOccurences() {
        testSearch {
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [getWordId("dum")], minOccurrences: 3, maxOccurrences: 5)
            terms << new SearchTermUsingIds()

            sentence(
                false, "bla bla dum dum bla dum bla dum"
            )

            sentence(
                false, "blah blah blah blah blah"
            )

            sentence(
                true, "bla bla dum dum dum"
            )

            sentence(
                true, "bla bla dum dum dum bla"
            )

            sentence(
                true, "bla bla dum dum dum dum dum dum dum dum"
            )
        }
    }

    @Test
    public void testMaxOccurences() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [ getWordId("hello")], minOccurrences: 1, maxOccurrences: 1)
            terms << new SearchTermUsingIds(maxOccurrences: 3)
            terms << new SearchTermUsingIds(wordIds: [ getWordId("world")], minOccurrences: 1, maxOccurrences: 1)

            sentence(
                true, "hello world"
            )

            sentence(
                true, "hello beautiful world"
            )

            sentence(
                false, "hello planet"
            )

            sentence(
                false, "hello beautiful pretty gorgeous fantastic world"
            )
        }
    }
}
