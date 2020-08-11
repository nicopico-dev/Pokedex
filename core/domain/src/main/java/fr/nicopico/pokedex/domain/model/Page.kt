package fr.nicopico.pokedex.domain.model

import fr.nicopico.pokedex.core.api.models.PagedResource

data class Page<out T>(
    val index: PageIndex,
    val totalCount: Int,
    val content: List<T>
)

typealias PageIndex = Int

internal fun PageIndex.getOffset(pageSize: Int) = this * pageSize
internal fun PageIndex.getLimit(pageSize: Int) = this.getOffset(pageSize) + pageSize

internal fun <JSON, MODEL> PagedResource<JSON>.toPage(index: PageIndex, mapper: (JSON) -> MODEL): Page<MODEL> =
    Page(
        index = index,
        totalCount = count,
        content = results.map(mapper)
    )
