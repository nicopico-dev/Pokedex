package fr.nicopico.pokedex.core.api.di

import fr.nicopico.pokedex.core.api.clients.PokemonApi
import fr.nicopico.pokedex.core.api.clients.PokemonApiClient
import fr.nicopico.pokedex.core.api.repository.RemotePokemonRepository
import fr.nicopico.pokedex.domain.repository.PokemonRepository
import org.koin.dsl.module

val apiModule = module {
    factory<PokemonRepository> { RemotePokemonRepository(get()) }
    factory<PokemonApi> { PokemonApiClient() }
}
