package be.alex.dpc.tests.groovy

import org.junit.Test
import be.alex.dpc.SearchTermUsingIds

class OccurencesTest extends GroovySearchTest {

    @Test
    public void testMinMaxOccurences() {
        testSearch {
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [getWordId("dum")], minOccurences: 3, maxOccurences: 5)
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
            terms << new SearchTermUsingIds(wordIds: [ getWordId("hello")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(maxOccurences: 3)
            terms << new SearchTermUsingIds(wordIds: [ getWordId("world")], minOccurences: 1, maxOccurences: 1)

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
