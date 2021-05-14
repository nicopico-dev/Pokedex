package fr.nicopico.pokedex.feature.pokemon.list.api.remote

import fr.nicopico.pokedex.core.api.models.PagedResource
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PokemonListApi {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PagedResource<PokemonJson>
}
