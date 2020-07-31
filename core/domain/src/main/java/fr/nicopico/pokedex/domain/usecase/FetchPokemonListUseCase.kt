package fr.nicopico.pokedex.domain.usecase

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.models.PokemonJson
import fr.nicopico.pokedex.domain.model.*

class FetchPokemonListUseCase(
    private val pokemonApi: PokemonApi,
    private val pageSize: Int = 20
) : UseCase<PageIndex, Page<Pokemon>> {

    override suspend fun execute(parameter: PageIndex) =
        pokemonApi
            .fetchPokemonList(
                offset = parameter.getOffset(pageSize),
                limit = parameter.getLimit(pageSize)
            )
            .toPage(parameter, this::toModel)

    private fun toModel(json: PokemonJson): Pokemon = Pokemon(
        id = json.id,
        name = json.name,
        illustrationUrl = "https://pokeres.bastionbot.org/images/pokemon/${json.id}.png"
    )
}
