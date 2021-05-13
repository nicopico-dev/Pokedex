package fr.nicopico.pokedex.feature.pokemon.list.navigation

import fr.nicopico.pokedex.domain.model.Pokemon

interface PokemonListNavigation {
    fun onPokemonClicked(pokemon: Pokemon)
}
