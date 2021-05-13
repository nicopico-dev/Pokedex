package fr.nicopico.pokedex.di

import fr.nicopico.pokedex.core.api.di.apiModule
import fr.nicopico.pokedex.feature.pokemon.list.api.di.pokemonListApiModule
import fr.nicopico.pokedex.feature.pokemon.list.di.pokemonListModule

val pokedexModules = listOf(apiModule, pokemonListApiModule, pokemonListModule)
