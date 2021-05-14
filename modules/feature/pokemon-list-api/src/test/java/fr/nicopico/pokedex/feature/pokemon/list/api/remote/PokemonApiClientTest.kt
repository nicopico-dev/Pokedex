package fr.nicopico.pokedex.feature.pokemon.list.api.remote

import com.google.common.truth.Truth.assertThat
import fr.nicopico.base.tests.CoroutineTestRule
import fr.nicopico.pokedex.core.api.clients.createApiClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonApiClientTest : BaseApiClientTest() {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var apiClient: PokemonListApi

    @Before
    fun setUp() {
        apiClient = createApiClient(PokemonListApi::class, baseUrl = endpointUrl)
    }

    @Test
    fun fetchPokemonList() = runBlocking {
        enqueueResponse("fetchPokemonList.json")
        val result = apiClient.fetchPokemonList(offset = 0, limit = 20)
        waitForRequest()

        assertThat(result.count).isAtLeast(1)
        assertThat(result.results).isNotEmpty()
    }
}
