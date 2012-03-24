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
