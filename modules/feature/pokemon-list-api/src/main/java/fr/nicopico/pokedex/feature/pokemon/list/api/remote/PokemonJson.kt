package fr.nicopico.pokedex.feature.pokemon.list.api.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonJson(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String
)
