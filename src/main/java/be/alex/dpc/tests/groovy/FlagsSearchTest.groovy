package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class FlagsSearchTest extends GroovySearchTest {
	@Test
	public void testSimpleFlagSearch() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.properName], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)

			sentence(
				true,
				word("alex", types.noun, flags.properName),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(false, "Hier zitten zelfs geen flags op")
		}
	}

    @Test
    public void testSecondFlagSearch() {
        testSearch {
            terms << new SearchTermUsingIds(flagIds: [flags.flag4], flagsOrMode: false)

            sentence(
                true,
                word("alex", types.noun, flags.flag3, flags.flag4)
            )

            sentence(false, "Hier zitten zelfs geen flags op")
        }
    }

	@Test
	public void testOrFlagSearch() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.properName, flags.pastParticiple], flagsOrMode: true, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

			sentence(
				true,
				word("alex", types.noun, flags.properName),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(
				false,
				word("pralex", types.noun, flags.flag3),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(false, "Hier zitten zelfs geen flags op")
		}
	}

	@Test
	public void testAndFlagSearch() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.flag3, flags.flag4], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)

			sentence(
				true,
				word("alex", types.noun, flags.flag3, flags.flag4),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(
				false,
				word("alex", types.noun, flags.flag3),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(false, "Hier zitten zelfs geen flags op")
		}

	}

    @Test
    public void testSimpleFlagExclusion() {
        testSearch {
            terms << new SearchTermUsingIds(excludeFlagIds: [flags.properName], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

            sentence(
                true,
                word("alex", types.noun, flags.flag3, flags.flag4),
                word("heeft"),
                word("lekker"),
                word("gegeten", types.verb, flags.pastParticiple),
            )

            sentence(
                false,
                word("alex", types.noun, flags.flag3, flags.flag4, flags.properName),
                word("heeft"),
                word("lekker"),
                word("gegeten", types.verb, flags.pastParticiple),
            )

            sentence(true, "Hier zitten zelfs geen flags op")
        }
    }

	@Test
	public void testFlagExclusion() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.flag3, flags.flag4], excludeFlagIds: [flags.properName], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false, minOccurences: 1, maxOccurences: 1)

			sentence(
				true,
				word("alex", types.noun, flags.flag3, flags.flag4),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(
				false,
				word("alex", types.noun, flags.flag3, flags.flag4, flags.properName),
				word("heeft"),
				word("lekker"),
				word("gegeten", types.verb, flags.pastParticiple),
			)

			sentence(false, "Hier zitten zelfs geen flags op")
		}
	}

	@Test
	public void testFlagExclusionForTermWithoutFlags() {
		testSearch {
			terms << new SearchTermUsingIds(excludeFlagIds: [flags.properName], flagsOrMode: false)

			sentence(true, "Hier zitten zelfs geen flags op")
		}

	}

    @Test
    public void testFlagExclusionWithOrModeOnMoreComplexData() {
        testSearch {
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [ getWordId("haar") ], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(excludeFlagIds: [ getFlagId("inf"), getFlagId("vrij")], excludeFlagsOrMode: true, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [ getWordId("punt") ], minOccurences: 1, maxOccurences: 1 )
            terms << new SearchTermUsingIds()

            sentence(
                true,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("zou WW(pv,verl,ev;zullen)"),
                buildWord("haar VNW(bez,det,stan,vol,3,ev,prenom,zonder,agr;haar)"),
                buildWord("willen WW(pv,tgw,mv;willen)"),
                buildWord("vragen WW(inf,vrij,zonder;vragen)"),
                buildWord("dat VNW(aanw,det,stan,prenom,zonder,evon;dat)"),
                buildWord("punt N(soort,ev,basis,onz,stan;punt)"),
                buildWord("nog BW(;nog)"),
                buildWord("eens BW(;eens)"),
                buildWord("in VZ(init;in)"),
                buildWord("overweging N(soort,ev,basis,zijd,stan;overweging)"),
                buildWord("te VZ(init;te)"),
                buildWord("nemen WW(inf,vrij,zonder;nemen)"),
                buildWord(". LET(;.)")
            )

            sentence(
                false,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("zou WW(pv,verl,ev;zullen)"),
                buildWord("haar VNW(bez,det,stan,vol,3,ev,prenom,zonder,agr;haar)"),
                buildWord("vragen WW(inf,vrij,zonder;vragen)"),
                buildWord("punt N(soort,ev,basis,onz,stan;punt)"),
                buildWord("nog BW(;nog)"),
                buildWord("eens BW(;eens)"),
                buildWord("in VZ(init;in)"),
                buildWord("overweging N(soort,ev,basis,zijd,stan;overweging)"),
                buildWord("te VZ(init;te)"),
                buildWord("nemen WW(inf,vrij,zonder;nemen)"),
                buildWord(". LET(;.)")
            )
        }
    }

    @Test
    public void testFlagExclusionWithAndMode() {
        testSearch {
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [ getWordId("haar") ], minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(excludeFlagIds: [ getFlagId("inf"), getFlagId("vrij") ], excludeFlagsOrMode: false, minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()
            terms << new SearchTermUsingIds(wordIds: [ getWordId("punt") ] , minOccurences: 1, maxOccurences: 1)
            terms << new SearchTermUsingIds()

            sentence(
                false,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("zou WW(pv,verl,ev;zullen)"),
                buildWord("haar VNW(bez,det,stan,vol,3,ev,prenom,zonder,agr;haar)"),
                buildWord("vragen WW(inf,vrij,zonder;vragen)"),
                buildWord("punt N(soort,ev,basis,onz,stan;punt)"),
                buildWord("nog BW(;nog)"),
                buildWord("eens BW(;eens)"),
                buildWord("in VZ(init;in)"),
                buildWord("overweging N(soort,ev,basis,zijd,stan;overweging)"),
                buildWord("te VZ(init;te)"),
                buildWord("nemen WW(inf,vrij,zonder;nemen)"),
                buildWord(". LET(;.)")
            )

            sentence(
                true,
                buildWord("Ik VNW(pers,pron,nomin,vol,1,ev;ik)"),
                buildWord("zou WW(pv,verl,ev;zullen)"),
                buildWord("haar VNW(bez,det,stan,vol,3,ev,prenom,zonder,agr;haar)"),
                buildWord("vragen WW(inf,blah,zonder;vragen)"),
                buildWord("punt N(soort,ev,basis,onz,stan;punt)"),
                buildWord("nog BW(;nog)"),
                buildWord("eens BW(;eens)"),
                buildWord("in VZ(init;in)"),
                buildWord("overweging N(soort,ev,basis,zijd,stan;overweging)"),
                buildWord("te VZ(init;te)"),
                buildWord("nemen WW(inf,vrij,zonder;nemen)"),
                buildWord(". LET(;.)")
            )
        }
    }
}
