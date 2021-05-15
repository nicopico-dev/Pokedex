package fr.nicopico.pokedex.feature.pokemon.list.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import fr.nicopico.base.usecase.onFailure
import fr.nicopico.base.usecase.onSuccess
import fr.nicopico.pokedex.feature.pokemon.list.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.navigation.PokemonListNavigation
import fr.nicopico.pokedex.feature.pokemon.list.usecase.FetchPokemonListUseCase

class PokemonListViewModel(
    private val useCase: FetchPokemonListUseCase,
    private val navigation: PokemonListNavigation
) : ViewModel() {
    val pokemons: LiveData<List<Pokemon>> = liveData {
        useCase.execute(0)
            .onSuccess { emit(it) }
            .onFailure { throw it }
    }

    fun onPokemonClicked(pokemon: Pokemon) {
        navigation.onPokemonClicked(pokemon)
    }
}
