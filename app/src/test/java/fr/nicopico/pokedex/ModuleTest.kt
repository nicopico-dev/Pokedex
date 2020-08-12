package fr.nicopico.pokedex

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules

@RunWith(AndroidJUnit4::class)
class ModuleTest : AutoCloseKoinTest() {

    @Test
    fun `Koin modules are properly configured`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        koinApplication {
            androidContext(context)
            modules(pokedexModules)
        }.checkModules()
    }
}
