package fr.nicopico.pokedex.domain.model

@Deprecated("do not use")
data class Pokemon(
    val id: PokemonId,
    val name: String,
    val illustrationUrl: String
)
