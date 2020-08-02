package fr.nicopico.pokedex.feature.pokemon.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PagedResource
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.core.tests.CoroutineTestRule
import fr.nicopico.pokedex.domain.model.Page
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.domain.model.Result
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
    fun somePokemonsAreReturned() = runBlocking {
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
        assertThat(actual).isInstanceOf(Result.Success::class.java)
        val success = actual as Result.Success<Page<Pokemon>>
        assertThat(success.value.content).hasSize(2)
    }
}
