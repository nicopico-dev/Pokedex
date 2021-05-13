package fr.nicopico.pokedex.feature.pokemon.list.api.repository

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.domain.model.Pokemon
import java.net.URI

internal class RemotePokemonRepository(
    private val pokemonApi: PokemonApi
) : PokemonRepository {

    override suspend fun list(offset: Int, limit: Int?): List<Pokemon> {
        return pokemonApi
            .fetchPokemonList(offset, limit ?: 0)
            .results
            .map(::toModel)
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
