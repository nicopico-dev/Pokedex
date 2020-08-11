package fr.nicopico.pokedex.domain.repository

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PagedResource
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.domain.model.getLimit
import fr.nicopico.pokedex.domain.model.getOffset
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class PokemonRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: PokemonRepository

    @MockK
    private lateinit var api: PokemonApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = PokemonRepository(api)
    }

    @Test
    fun `list() returns a list of pokemon`() {
        // Given
        val limitSlot = slot<Int>()
        val offsetSlot = slot<Int>()
        coEvery {
            api.fetchPokemonList(capture(limitSlot), capture(offsetSlot))
        } returns PagedResource(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
                PokemonJson(name = "Bulbizar", url = "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonJson(name = "Pikachu", url = "https://pokeapi.co/api/v2/pokemon/2/")
            )
        )

        // When
        val pageIndex = Random.nextInt()
        val pageSize = Random.nextInt()
        val pokemons = runBlocking {
            repository.list(pageIndex, pageSize)
        }

        // Then
        assertThat(pokemons).isNotNull()
        assertThat(pokemons.index).isEqualTo(pageIndex)
        assertThat(pokemons.totalCount).isEqualTo(2)
        assertThat(pokemons.content).hasSize(pokemons.totalCount)

        assertThat(offsetSlot.captured).isEqualTo(pageIndex.getOffset(pageSize))
        assertThat(limitSlot.captured).isEqualTo(pageIndex.getLimit(pageSize))
    }
}
