package fr.nicopico.pokedex.feature.pokemon.list.usecase

import fr.nicopico.base.usecase.Result
import fr.nicopico.base.usecase.UseCase
import fr.nicopico.pokedex.domain.model.Page
import fr.nicopico.pokedex.domain.model.PageIndex
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.domain.repository.PokemonRepository

class FetchPokemonListUseCase(
    private val pokemonRepository: PokemonRepository,
    private val pageSize: Int = 20
) : UseCase<PageIndex, Result<Page<Pokemon>>> {

    override suspend fun execute(parameter: PageIndex) = Result.runCatching {
        pokemonRepository.list(parameter, pageSize)
    }
}