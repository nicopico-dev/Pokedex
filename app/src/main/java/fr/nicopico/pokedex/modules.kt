package fr.nicopico.pokedex

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.clients.PokemonApiClient
import fr.nicopico.pokedex.core.api.repository.RemotePokemonRepository
import fr.nicopico.pokedex.domain.repository.PokemonRepository
import fr.nicopico.pokedex.feature.pokemon.list.ui.PokemonListViewModel
import fr.nicopico.pokedex.feature.pokemon.list.usecase.FetchPokemonListUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val pokedexModules: List<Module>
    get() = listOf(pokemonListModule, domainModule, apiModule)

val pokemonListModule = module {
    viewModel { PokemonListViewModel(get()) }

    factory { FetchPokemonListUseCase(get()) }
}

val domainModule = module {
    factory<PokemonRepository> { get<RemotePokemonRepository>() }
}

val apiModule = module {
    factory { RemotePokemonRepository(get()) }
    factory<PokemonApi> { PokemonApiClient() }
}
