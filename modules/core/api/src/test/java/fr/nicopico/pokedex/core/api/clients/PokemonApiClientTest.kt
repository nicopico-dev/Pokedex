package fr.nicopico.pokedex.core.api.clients

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonApiClientTest : BaseApiClientTest() {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var apiClient: PokemonApiClient

    @Before
    fun setUp() {
        apiClient = PokemonApiClient(rootUrl = endpointUrl)
    }

    @Test
    fun fetchPokemonList() = runBlocking {
        enqueueResponse("fetchPokemonList.json")
        val result = apiClient.fetchPokemonList()
        waitForRequest()

        assertThat(result.count).isAtLeast(1)
        assertThat(result.results).isNotEmpty()
    }

    @Test
    fun fetchPokemonDetails() = runBlocking {
        enqueueResponse("fetchPokemonDetails_bulbasaur.json")
        val result = apiClient.fetchPokemonDetails("Bulbasaur")
        waitForRequest()

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("bulbasaur")
    }
}
