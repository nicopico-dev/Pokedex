package fr.nicopico.pokedex.core.api.clients

import fr.nicopico.pokedex.core.api.models.PagedResource
import fr.nicopico.pokedex.core.api.models.PokemonDetailsJson
import fr.nicopico.pokedex.core.api.models.PokemonJson
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    companion object {
        const val endpoint_v2 = "https://pokeapi.co/api/v2/"
    }

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PagedResource<PokemonJson>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetails(@Path("name") name: String): PokemonDetailsJson
}
