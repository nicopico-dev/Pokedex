package fr.nicopico.pokedex.core.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonJson(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "url") val url: String
)
