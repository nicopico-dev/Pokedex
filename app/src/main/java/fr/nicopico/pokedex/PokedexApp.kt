package fr.nicopico.pokedex

import android.app.Application
import fr.nicopico.pokedex.core.api.di.apiModule
import fr.nicopico.pokedex.feature.pokemon.list.di.pokemonListModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokedexApp : Application() {

    private val pokedexModules = listOf(apiModule, pokemonListModule)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PokedexApp)

            // TODO Await fix for Koin and replace the explicit invocations
            //  of loadModules() and createRootScope() with a single call to modules()
            //  (https://github.com/InsertKoinIO/koin/issues/847)
            koin.loadModules(pokedexModules)
            koin.createRootScope()
        }
    }
}
