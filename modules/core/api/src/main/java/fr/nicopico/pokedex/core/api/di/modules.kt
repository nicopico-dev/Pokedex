package fr.nicopico.pokedex.core.api.di

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.clients.PokemonApiClient
import org.koin.dsl.module

val apiModule = module {
    factory<PokemonApi> { PokemonApiClient() }
}
