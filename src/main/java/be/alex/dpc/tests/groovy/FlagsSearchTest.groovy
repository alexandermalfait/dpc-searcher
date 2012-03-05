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
}
