package fr.nicopico.pokedex.feature.pokemon.list.model

import fr.nicopico.pokedex.domain.model.PokemonId

data class Pokemon(
    val id: PokemonId,
    val name: String,
    val illustrationUrl: String
)
