package fr.nicopico.pokedex

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.nicopico.base.android.dependency.injection.ViewModelFactoryProducerHolder
import fr.nicopico.pokedex.di.pokedexModules
import fr.nicopico.pokedex.feature.pokemon.list.ui.PokemonListViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokedexApp : Application() {

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

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass == PokemonListViewModel::class.java) {
                    return PokemonListViewModel(get()) as T
                } else {
                    throw UnsupportedOperationException("Don't know how to create ViewModels of type $modelClass")
                }
            }
        }
        ViewModelFactoryProducerHolder.init { factory }
    }
}
