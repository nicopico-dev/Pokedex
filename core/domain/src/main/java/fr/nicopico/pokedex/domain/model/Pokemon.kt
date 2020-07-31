package fr.nicopico.pokedex.domain.model

data class Pokemon(
    val id: PokemonId,
    val name: String,
    val illustrationUrl: String
)
