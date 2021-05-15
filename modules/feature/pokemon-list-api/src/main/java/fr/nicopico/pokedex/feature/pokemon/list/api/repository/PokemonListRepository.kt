package fr.nicopico.pokedex.feature.pokemon.list.api.repository

import fr.nicopico.pokedex.feature.pokemon.list.model.Pokemon

interface PokemonListRepository {
    suspend fun list(offset: Int = 0, limit: Int? = null): List<Pokemon>
}
