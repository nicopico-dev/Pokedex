package fr.nicopico.pokedex.feature.pokemon.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import fr.nicopico.base.domain.usecase.onFailure
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.base.domain.usecase.onSuccess
import fr.nicopico.pokedex.feature.pokemon.list.usecase.FetchPokemonListUseCase

class PokemonListViewModel
internal constructor(
    private val useCase: FetchPokemonListUseCase
) : ViewModel() {
    val pokemons: LiveData<List<Pokemon>> = liveData {
        useCase.execute(0)
            .onSuccess { emit(it.content) }
            .onFailure { throw it }
    }
}
