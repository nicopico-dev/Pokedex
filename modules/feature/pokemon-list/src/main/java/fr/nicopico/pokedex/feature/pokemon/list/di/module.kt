package fr.nicopico.pokedex.feature.pokemon.list.di

import fr.nicopico.pokedex.feature.pokemon.list.ui.PokemonListViewModel
import fr.nicopico.pokedex.feature.pokemon.list.usecase.FetchPokemonListUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonListModule = module {
    viewModel { PokemonListViewModel(get()) }

    factory { FetchPokemonListUseCase(get()) }
}
