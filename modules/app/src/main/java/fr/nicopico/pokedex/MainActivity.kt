package fr.nicopico.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.nicopico.pokedex.feature.pokemon.list.ui.PokemonListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.content,
                    PokemonListFragment()
                )
                .commit()
        }
    }
}
