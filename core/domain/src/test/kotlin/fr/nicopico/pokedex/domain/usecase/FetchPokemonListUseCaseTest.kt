package fr.nicopico.pokedex.domain.usecase

import com.google.common.truth.Truth.assertThat
import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PagedResource
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.core.tests.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FetchPokemonListUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var useCase: FetchPokemonListUseCase

    @MockK
    private lateinit var pokemonApi: PokemonApi
    private val pageSize = 20

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchPokemonListUseCase(pokemonApi, pageSize)
    }

    @Test
    fun `some pokemons are returned`() = runBlocking {
        // Given
        coEvery { pokemonApi.fetchPokemonList(any(), any()) } returns PagedResource(
                count = 2,
                next = null,
                previous = null,
                results = listOf(
                        PokemonJson(id = 1, name = "Bulbizar"),
                        PokemonJson(id = 2, name = "Pikachu")
                )
        )

        // When
        val actual = useCase.execute(parameter = 0)

        // Then
        assertThat(actual.content).hasSize(2)
    }
}
