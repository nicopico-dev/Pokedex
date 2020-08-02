package fr.nicopico.pokedex.feature.pokemon.list.usecase

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.domain.model.*
import fr.nicopico.base.domain.usecase.Result
import fr.nicopico.base.domain.usecase.UseCase

internal class FetchPokemonListUseCase(
    private val pokemonApi: PokemonApi,
    private val pageSize: Int = 20
) : UseCase<PageIndex, Page<Pokemon>> {

    override suspend fun execute(parameter: PageIndex) = Result.runCatching {
        pokemonApi
            .fetchPokemonList(
                offset = parameter.getOffset(pageSize),
                limit = parameter.getLimit(pageSize)
            )
            .toPage(parameter, this::toModel)
    }

    private fun toModel(json: PokemonJson): Pokemon = Pokemon(
        id = json.id,
        name = json.name,
        illustrationUrl = "https://pokeres.bastionbot.org/images/pokemon/${json.id}.png"
    )
}
