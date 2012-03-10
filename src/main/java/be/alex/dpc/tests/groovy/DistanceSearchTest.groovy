package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class DistanceSearchTest extends GroovySearchTest {
    @Test
    public void testMaximumDistance() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("de")])
            terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], maximumDistanceFromLastMatch: 1)

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
            terms << new SearchTermUsingIds(wordIds: [getWordId("de")])
            terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], maximumDistanceFromLastMatch: 1)

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
            terms << new SearchTermUsingIds(wordTypeIds: [types.verb])
            terms << new SearchTermUsingIds(wordTypeIds: [types.verb])
            terms << new SearchTermUsingIds(wordTypeIds: types.values().findAll { it != types.verb }, maximumDistanceFromLastMatch: 1)
            terms << new SearchTermUsingIds(wordTypeIds: [types.verb], maximumDistanceFromLastMatch: 1, lastInSentence: true)

            sentence(
                true,
                word("Maar"),
                word("we"),
                word("zijn", types.verb),
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

    @Test
    public void testLastInSentenceSkipsDot() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("Ziggy")])
            terms << new SearchTermUsingIds(wordIds: [getWordId("kater")], lastInSentence: true)

            sentence(
                true,
                word("Ziggy"), word("is"), word("een"), word("gekke"), word("kater")
            )

            sentence(
                true,
                word("Ziggy"), word("is"), word("een"), word("gekke"), word("kater"), word(".", types.dot)
            )

            sentence(
                false,
                word("Ziggy"), word("is"), word("een"), word("gekke"), word("olifant")
            )

            sentence(
                false,
                word("Ziggy"), word("is"), word("een"), word("gekke"), word("olifant"), word(".", types.dot)
            )
        }
    }

    @Test
    public void test2wordsMaximumDistance() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("hebben")])
            terms << new SearchTermUsingIds(wordTypeIds: [ types.verb ], flagIds: [ flags.pv ], maximumDistanceFromLastMatch: 2)
            terms << new SearchTermUsingIds(wordTypeIds: [ types.verb ], lastInSentence: true)

            //criteria N(soort,mv,basis;criterium) , LET(;,) maar VG(neven;maar) die VNW(aanw,pron,stan,vol,3,getal;die)
            // worden WW(pv,tgw,mv;worden) in VZ(init;in) hun VNW(bez,det,stan,vol,3,mv,prenom,zonder,agr;hun) ogen N(soort,mv,basis;oog) niet BW(;niet)
            // strikt ADJ(vrij,basis,zonder;strikt) nageleefd WW(vd,vrij,zonder;naleven) . LET(;.)
            sentence(
                false,
                word("We"),
                word("hebben", types.verb, flags.pv, flags.tgw, flags.mv),
                word("criteria", types.noun, flags.soort, flags.basis),
                word(";", types.dot),
                word("maar", types.vg),
                word("die", types.aanw, flags.pron,flags.stan, flags.vol),
                word("worden", types.verb, flags.pv, flags.tgw, flags.mv),
                word("in", types.vz),
                word("ogen", types.noun, flags.m, flags.soort, flags.basis),
                word("niet"),
                word("strikt"),
                word("nageleefd", types.verb),
                word(".", types.dot)
            )
        }
    }
}