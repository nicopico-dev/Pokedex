package fr.nicopico.pokedex.feature.pokemon.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import fr.nicopico.base.usecase.Result
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.api.repository.PokemonListRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.slot
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
    private lateinit var pokemonListRepository: PokemonListRepository
    private val pageSize = 20

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchPokemonListUseCase(
            pokemonListRepository,
            pageSize
        )
    }

    @Test
    fun `Some pokemons are returned`() = runBlocking {
        // Given
        val pageIndexSlot = slot<Int>()
        val pageSizeSlot = slot<Int>()
        coEvery {
            pokemonListRepository.list(capture(pageIndexSlot), capture(pageSizeSlot))
        } answers {
            listOf(
                Pokemon(
                    id = 1,
                    name = "Bulbizar",
                    illustrationUrl = "https://pokeapi.co/api/v2/pokemon/1/"
                ),
                Pokemon(
                    id = 2,
                    name = "Pikachu",
                    illustrationUrl = "https://pokeapi.co/api/v2/pokemon/2/"
                )
            )
        }

        // When
        val actual = useCase.execute(parameter = 0)

        // Then
        assertThat(actual).isInstanceOf(Result.Success::class.java)

        val success = actual as Result.Success<List<Pokemon>>
        assertThat(success.value).hasSize(2)

        val bulbizar = success.value[0]
        with(bulbizar) {
            assertThat(id).isEqualTo(1)
            assertThat(name).isEqualTo("Bulbizar")
        }

        val pikachu = success.value[1]
        with(pikachu) {
            assertThat(id).isEqualTo(2)
            assertThat(name).isEqualTo("Pikachu")
        }
    }
}
