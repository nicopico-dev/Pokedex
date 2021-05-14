package fr.nicopico.pokedex.feature.pokemon.list.api.repository

import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.api.remote.PokemonJson
import fr.nicopico.pokedex.feature.pokemon.list.api.remote.PokemonListApi
import java.net.URI

internal class PokemonListRepositoryImpl(
    private val remoteApi: PokemonListApi
) : PokemonListRepository {

    override suspend fun list(offset: Int, limit: Int?): List<Pokemon> {
        return remoteApi
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
