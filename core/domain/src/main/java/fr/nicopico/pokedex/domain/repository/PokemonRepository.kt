package fr.nicopico.pokedex.domain.repository

import fr.nicopico.pokedex.domain.model.Page
import fr.nicopico.pokedex.domain.model.PageIndex
import fr.nicopico.pokedex.domain.model.Pokemon

interface PokemonRepository {
    suspend fun list(pageIndex: PageIndex, pageSize: Int): Page<Pokemon>
}
