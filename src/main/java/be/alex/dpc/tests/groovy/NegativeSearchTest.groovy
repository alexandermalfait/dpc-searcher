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
    public void testSimpleExclusionAgain() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("vraag")])
            terms << new SearchTermUsingIds(wordTypeIds: [ getWordTypeId("WW") ], excludeTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("dat")])

            sentence(
                true,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("vraag WW(pv,tgw,ev;vragen)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("aan VZ(init;aan)"),
                buildWord("de LID(bep,stan,rest;de)"),
                buildWord("Raadsvoorzitter N(eigen,ev,basis,zijd,stan;Raadsvoorzitter)"),
                buildWord("als VG(onder;als)"),
                buildWord("ze VNW(pers,pron,stan,red,3,mv;ze)"),
                buildWord("meeluistert WW(pv,tgw,met-t;meeluisteren)"),
                buildWord(". LET(;.)")
            )

            sentence(
                false,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("vraag WW(pv,tgw,ev;vragen)"),
                buildWord("lachend WW(inf;lachen)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("aan VZ(init;aan)"),
                buildWord("de LID(bep,stan,rest;de)"),
                buildWord("Raadsvoorzitter N(eigen,ev,basis,zijd,stan;Raadsvoorzitter)"),
                buildWord("als VG(onder;als)"),
                buildWord("ze VNW(pers,pron,stan,red,3,mv;ze)"),
                buildWord("meeluistert WW(pv,tgw,met-t;meeluisteren)"),
                buildWord(". LET(;.)")
            )

            sentence(
                true,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("vraag WW(pv,tgw,ev;vragen)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("aan VZ(init;aan)"),
                buildWord("de LID(bep,stan,rest;de)"),
                buildWord("Raadsvoorzitter N(eigen,ev,basis,zijd,stan;Raadsvoorzitter)"),
                buildWord("als VG(onder;als)"),
                buildWord("ze VNW(pers,pron,stan,red,3,mv;ze)"),
                buildWord("meeluistert WW(pv,tgw,met-t;meeluisteren)"),
                buildWord("en VG(en;en)"),
                buildWord("ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("vraag WW(pv,tgw,ev;vragen)"),
                buildWord("lachend WW(inf;lachen)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("ze VNW(pers,pron,stan,red,3,mv;ze)"),
                buildWord("meeluistert WW(pv,tgw,met-t;meeluisteren)"),
                buildWord(". LET(;.)")
            )
        }
    }
    
    @Test
    public void testExclusionMatchIndexes() {
        long sentenceId = 0

        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("vraag")])
            terms << new SearchTermUsingIds(wordTypeIds: [ getWordTypeId("WW") ], excludeTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("dat")])

            sentenceId = sentence(
                true,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("vraag WW(pv,tgw,ev;vragen)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("aan VZ(init;aan)"),
                buildWord("de LID(bep,stan,rest;de)"),
                buildWord("Raadsvoorzitter N(eigen,ev,basis,zijd,stan;Raadsvoorzitter)"),
                buildWord("als VG(onder;als)"),
                buildWord("ze VNW(pers,pron,stan,red,3,mv;ze)"),
                buildWord("meeluistert WW(pv,tgw,met-t;meeluisteren)"),
                buildWord(". LET(;.)")
            )
        }
        
        assert lastResult.getWordIndexesForSentenceId(sentenceId).size() == 2
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
    public void testExclusionWithDistance() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("kat")])
            terms << new SearchTermUsingIds(wordIds: [getWordId("krabt")], excludeTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], maximumDistanceFromLastMatch: 3)

            sentence(
                false, "de kat krabt de krollen van de trap"
            )

            sentence(
                false, "de kat doet van krabt trap"
            )

            sentence(
                true, "de kat kruipt op de trap"
            )

            sentence(
                false, "de kat doet eigenlijk helemaal niets"
            )
        }
    }


    @Test
    public void testExclusionWithDistanceOnRealData() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("willen")])
            terms << new SearchTermUsingIds(wordTypeIds: [getWordTypeId("WW")], excludeTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("dat")], maximumDistanceFromLastMatch: 3)

            sentence(
                false,
                buildWord("Wij VNW(pers,pron,nomin,vol,1,mv;wij)"),
                buildWord("willen WW(pv,tgw,mv;willen)"),
                buildWord("zeker ADJ(vrij,basis,zonder;zeker)"),
                buildWord("weten WW(inf,vrij,zonder;weten)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("dit VNW(aanw,pron,stan,vol,3o,ev;dit)"),
                buildWord("ook BW(;ook)"),
                buildWord("gebeurt WW(pv,tgw,met-t;gebeuren)"),
                buildWord(". LET(;.)")
            )

            sentence(
                true,
                buildWord("Wij VNW(pers,pron,nomin,vol,1,mv;wij)"),
                buildWord("willen WW(pv,tgw,mv;willen)"),
                buildWord("zeker ADJ(vrij,basis,zonder;zeker)"),
                buildWord("zeker ADJ(vrij,basis,zonder;zeker)"),
                buildWord("dat VG(onder;dat)"),
                buildWord("dit VNW(aanw,pron,stan,vol,3o,ev;dit)"),
                buildWord("ook BW(;ook)"),
                buildWord("gebeurt WW(pv,tgw,met-t;gebeuren)"),
                buildWord(". LET(;.)")
            )
        }
    }

}
