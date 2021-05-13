package fr.nicopico.pokedex.navigation

import android.content.Context
import android.widget.Toast
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.navigation.PokemonListNavigation

class Navigator(
    private val context: Context
) : PokemonListNavigation {

    override fun onPokemonClicked(pokemon: Pokemon) {
        Toast.makeText(context, "Clicked on $pokemon", Toast.LENGTH_SHORT).show()
    }
}
