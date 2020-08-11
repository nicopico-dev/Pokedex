package fr.nicopico.pokedex.domain.repository

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.domain.model.*
import java.net.URI

class PokemonRepository(
    private val pokemonApi: PokemonApi
) {

    suspend fun list(pageIndex: PageIndex, pageSize: Int): Page<Pokemon> {
        return pokemonApi
            .fetchPokemonList(
                offset = pageIndex.getOffset(pageSize),
                limit = pageIndex.getLimit(pageSize)
            )
            .toPage(pageIndex, this::toModel)
    }

    private fun toModel(json: PokemonJson): Pokemon {
        val id: Int = extractPokemonId(json)
        return Pokemon(
            id = id,
            name = json.name,
            illustrationUrl = "https://pokeres.bastionbot.org/images/pokemon/$id.png"
        )
    }

    private fun extractPokemonId(json: PokemonJson) =
        URI.create(json.url).path.split('/').findLast { it.isNotEmpty() }!!.toInt()
}
