package fr.nicopico.pokedex.feature.pokemon.list.api.repository

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PagedResource
import fr.nicopico.pokedex.core.api.models.PokemonJson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class RemotePokemonRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: RemotePokemonRepository

    @MockK
    private lateinit var api: PokemonApi

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = RemotePokemonRepository(api)
    }

    @Test
    fun `list() returns a list of pokemon`() {
        // Given
        val limitSlot = slot<Int>()
        val offsetSlot = slot<Int>()
        coEvery {
            api.fetchPokemonList(
                offset = capture(offsetSlot),
                limit = capture(limitSlot)
            )
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
        val offset = Random.nextInt()
        val limit = Random.nextInt()
        val pokemons = runBlocking {
            repository.list(offset, limit)
        }

        // Then
        assertThat(pokemons).isNotNull()
        assertThat(pokemons.size).isEqualTo(2)

        assertThat(offsetSlot.captured).isEqualTo(offset)
        assertThat(limitSlot.captured).isEqualTo(limit)
    }
}
