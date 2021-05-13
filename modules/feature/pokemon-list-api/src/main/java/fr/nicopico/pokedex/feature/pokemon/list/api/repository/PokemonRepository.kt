package fr.nicopico.pokedex.feature.pokemon.list.api.repository

import fr.nicopico.pokedex.domain.model.Pokemon

interface PokemonRepository {
    suspend fun list(offset: Int = 0, limit: Int? = null): List<Pokemon>
}
