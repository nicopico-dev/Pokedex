package fr.nicopico.pokedex.feature.pokemon.list.api.di

import fr.nicopico.pokedex.core.api.clients.createApiClient
import fr.nicopico.pokedex.feature.pokemon.list.api.remote.PokemonListApi
import fr.nicopico.pokedex.feature.pokemon.list.api.repository.PokemonListRepository
import fr.nicopico.pokedex.feature.pokemon.list.api.repository.PokemonListRepositoryImpl
import org.koin.dsl.module

val pokemonListApiModule = module {
    factory { createApiClient(PokemonListApi::class) }
    factory<PokemonListRepository> { PokemonListRepositoryImpl(get()) }
}
