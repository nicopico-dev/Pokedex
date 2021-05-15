package fr.nicopico.pokedex.domain.model

@Deprecated("do not use")
data class PokemonDetails(
    val pokemon: Pokemon,
    val height: Height,
    val weight: Weight,
    val types: List<PokemonType>,
    val stats: List<PokemonStats>
)
