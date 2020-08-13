package fr.nicopico.pokedex.feature.pokemon.list.usecase

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import fr.nicopico.base.usecase.Result
import fr.nicopico.pokedex.domain.model.Page
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.domain.repository.PokemonRepository
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
    private lateinit var pokemonRepository: PokemonRepository
    private val pageSize = 20

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = FetchPokemonListUseCase(
            pokemonRepository,
            pageSize
        )
    }

    @Test
    fun somePokemonsAreReturned() = runBlocking {
        // Given
        val pageIndexSlot = slot<Int>()
        val pageSizeSlot = slot<Int>()
        coEvery {
            pokemonRepository.list(capture(pageIndexSlot), capture(pageSizeSlot))
        } answers {
            Page(
                index = pageIndexSlot.captured,
                totalCount = 2,
                content = listOf(
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
            )
        }

        // When
        val actual = useCase.execute(parameter = 0)

        // Then
        assertThat(actual).isInstanceOf(Result.Success::class.java)

        val success = actual as Result.Success<Page<Pokemon>>
        assertThat(success.value.content).hasSize(2)

        val bulbizar = success.value.content[0]
        with(bulbizar) {
            assertThat(id).isEqualTo(1)
            assertThat(name).isEqualTo("Bulbizar")
        }

        val pikachu = success.value.content[1]
        with(pikachu) {
            assertThat(id).isEqualTo(2)
            assertThat(name).isEqualTo("Pikachu")
        }
    }
}
