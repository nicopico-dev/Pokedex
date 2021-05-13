package fr.nicopico.pokedex.di

import fr.nicopico.pokedex.core.api.di.apiModule
import fr.nicopico.pokedex.feature.pokemon.list.api.di.pokemonListApiModule
import fr.nicopico.pokedex.feature.pokemon.list.di.pokemonListModule
import fr.nicopico.pokedex.feature.pokemon.list.navigation.PokemonListNavigation
import fr.nicopico.pokedex.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val navigationModule = module {
    single { Navigator(androidContext()) }

    factory<PokemonListNavigation> { get<Navigator>() }
}

val pokedexModules = listOf(
    navigationModule,
    apiModule,
    pokemonListApiModule, pokemonListModule
)
