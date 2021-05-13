package fr.nicopico.pokedex

import android.app.Application
import fr.nicopico.pokedex.di.pokedexModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PokedexApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.ERROR)
            androidContext(this@PokedexApp)
            modules(pokedexModules)
        }
    }
}
