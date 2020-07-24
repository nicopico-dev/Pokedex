package fr.nicopico.pokedex.domain.usecase

import com.google.common.truth.Truth.assertThat
import fr.nicopico.pokedex.core.tests.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Rule
import org.junit.Test

class GetPokemonIllustrationUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var useCase: GetPokemonIllustrationUseCase

    @Before
    fun setUp() {
        useCase = GetPokemonIllustrationUseCase()
    }

    @Test
    fun `a valid http url is provided`() = runBlocking {
        val result = useCase.execute(1)
        assertThat(result).isNotNull()
        assertThat(result.scheme).isAnyOf("http", "https")
    }
}
