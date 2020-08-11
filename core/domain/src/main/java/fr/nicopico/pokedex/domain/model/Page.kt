package fr.nicopico.pokedex.domain.model

data class Page<out T>(
    val index: PageIndex,
    val totalCount: Int,
    val content: List<T>
)

typealias PageIndex = Int
