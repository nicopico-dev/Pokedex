package fr.nicopico.pokedex.feature.pokemon.list.usecase

import fr.nicopico.base.usecase.Result
import fr.nicopico.base.usecase.ResultList
import fr.nicopico.base.usecase.UseCase
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.api.repository.PokemonListRepository

class FetchPokemonListUseCase(
    private val pokemonListRepository: PokemonListRepository,
    private val pageSize: Int = 20
) : UseCase<Int, ResultList<Pokemon>> {

    override suspend fun execute(parameter: Int) = Result.runCatching {
        pokemonListRepository.list(parameter * pageSize, pageSize)
    }
}
