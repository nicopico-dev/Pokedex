package fr.nicopico.pokedex

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.nicopico.pokedex.di.pokedexModules
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class ModuleTest : KoinTest {

    @Test
    fun `Koin modules are properly configured`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        koinApplication {
            androidContext(context)
            modules(pokedexModules)
        }.checkModules()
    }
}
