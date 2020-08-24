package fr.nicopico.pokedex.domain.model

data class PokemonDetails(
    val pokemon: Pokemon,
    val height: Height,
    val weight: Weight,
    val types: List<PokemonType>,
    val stats: List<PokemonStats>
)
