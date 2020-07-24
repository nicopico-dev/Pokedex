package fr.nicopico.pokedex.domain.model

data class Pokemon(
    val id: PokemonId,
    val name: String
) {
    val imageUrl: String
        get() = "https://pokeres.bastionbot.org/images/pokemon/$id.png"
}
