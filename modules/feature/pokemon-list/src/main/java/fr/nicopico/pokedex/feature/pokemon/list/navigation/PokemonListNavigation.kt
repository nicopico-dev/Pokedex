package fr.nicopico.pokedex.feature.pokemon.list.navigation

import fr.nicopico.pokedex.feature.pokemon.list.model.Pokemon

interface PokemonListNavigation {
    fun onPokemonClicked(pokemon: Pokemon)
}
