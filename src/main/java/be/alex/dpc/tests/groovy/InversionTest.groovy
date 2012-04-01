package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class InversionTest extends GroovySearchTest {

	@Test
	public void testWordInversion() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [getWordId("de")], minOccurences: 1, maxOccurences: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], minOccurences: 1, maxOccurences: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("krollen")], invertTerm: true)
			terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

			sentence(
				false, "de kat krabt de krollen van de trap"
			)

			sentence(
				true, "de kat krabt de bollen van de trap"
			)
		}
	}

    @Test
    public void testMultipleWordInversion() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [getWordId("de")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(wordIds: [getWordId("krollen"), getWordId("bollen")], invertTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

            sentence(
                false, "de kat krabt de krollen van de trap"
            )

            sentence(
                false, "de kat krabt de bollen van de trap"
            )

            sentence(
                true, "de kat krabt de dingetjes van de trap"
            )
        }
    }

    @Test
    public void testWordTypeInversion() {
        testSearch {
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [getWordId("vraag")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(wordTypeIds: [ getWordTypeId("WW") ], invertTerm: true)
            terms << new SearchTermUsingIds(wordIds: [getWordId("dat")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

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
	public void testExclusionWithSkip() {
		testSearch {
			terms << new SearchTermUsingIds(wordIds: [getWordId("de")], minOccurences: 1, maxOccurences: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(wordIds: [getWordId("krabt")], minOccurences: 1, maxOccurences: 1)
			terms << new SearchTermUsingIds(wordIds: [getWordId("krollen")], invertTerm: true)
			terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], minOccurences: 1, maxOccurences: 1)

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
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [getWordId("kat")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(wordIds: [getWordId("krabt")], invertTerm: true, minOccurences: 1, maxOccurences: 3)
            terms << new SearchTermUsingIds(wordIds: [getWordId("trap")], minOccurences: 1, maxOccurences: 1)

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
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [getWordId("willen")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds(wordTypeIds: [getWordTypeId("WW")], invertTerm: true)
            //terms << new SearchTermUsingIds(maxOccurences: 3)
            terms << new SearchTermUsingIds(wordIds: [getWordId("dat")], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

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
                buildWord("dat VG(onder;dat)"),
                buildWord("dit VNW(aanw,pron,stan,vol,3o,ev;dit)"),
                buildWord("ook BW(;ook)"),
                buildWord("gebeurt WW(pv,tgw,met-t;gebeuren)"),
                buildWord(". LET(;.)")
            )
        }
    }

}
