package fr.nicopico.pokedex.feature.pokemon.list.api.di

import fr.nicopico.pokedex.feature.pokemon.list.api.repository.PokemonRepository
import fr.nicopico.pokedex.feature.pokemon.list.api.repository.RemotePokemonRepository
import org.koin.dsl.module

val pokemonListApiModule = module {
    factory<PokemonRepository> { RemotePokemonRepository(get()) }
}
