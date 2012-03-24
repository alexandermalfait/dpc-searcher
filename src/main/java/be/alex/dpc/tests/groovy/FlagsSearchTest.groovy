package be.alex.dpc.tests.groovy

import be.alex.dpc.SearchTermUsingIds
import org.junit.Test

class FlagsSearchTest extends GroovySearchTest {
	@Test
	public void testSimpleFlagSearch() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.properName], flagsOrMode: false)
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false)

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
			terms << new SearchTermUsingIds(flagIds: [flags.properName, flags.pastParticiple], flagsOrMode: true)
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false)

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
			terms << new SearchTermUsingIds(flagIds: [flags.flag3, flags.flag4], flagsOrMode: false)
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false)

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
	public void testFlagExclusion() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.flag3, flags.flag4], excludeFlagIds: [flags.properName], flagsOrMode: false)
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false)

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
	public void testFlagExclusionWithOrMode() {
		testSearch {
			terms << new SearchTermUsingIds(flagIds: [flags.flag3, flags.flag4], excludeFlagIds: [flags.properName], flagsOrMode: true)
			terms << new SearchTermUsingIds(flagIds: [flags.pastParticiple], flagsOrMode: false)

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
    public void testFlagExclusionWithOrModeOnMoreComplexData() {
        testSearch {
            terms << new SearchTermUsingIds(wordIds: [ getWordId("haar") ])
            terms << new SearchTermUsingIds(excludeFlagIds: [ getFlagId("inf"), getFlagId("vrij")], excludeFlagsOrMode: true)
            terms << new SearchTermUsingIds(wordIds: [ getWordId("punt") ] )

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
            terms << new SearchTermUsingIds(wordIds: [ getWordId("haar") ])
            terms << new SearchTermUsingIds(excludeFlagIds: [ getFlagId("inf"), getFlagId("vrij") ], excludeFlagsOrMode: false)
            terms << new SearchTermUsingIds(wordIds: [ getWordId("punt") ] )

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
