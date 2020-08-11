package fr.nicopico.pokedex.core.api.repository

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.core.api.models.getLimit
import fr.nicopico.pokedex.core.api.models.getOffset
import fr.nicopico.pokedex.core.api.models.toPage
import fr.nicopico.pokedex.domain.model.*
import fr.nicopico.pokedex.domain.repository.PokemonRepository
import java.net.URI

class RemotePokemonRepository(
    private val pokemonApi: PokemonApi
) : PokemonRepository {

    override suspend fun list(pageIndex: PageIndex, pageSize: Int): Page<Pokemon> {
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
