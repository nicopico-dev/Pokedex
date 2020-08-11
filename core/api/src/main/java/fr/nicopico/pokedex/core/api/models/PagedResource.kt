package fr.nicopico.pokedex.core.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.nicopico.pokedex.domain.model.Page
import fr.nicopico.pokedex.domain.model.PageIndex

@JsonClass(generateAdapter = true)
data class PagedResource<T>(
    @field:Json(name = "count") val count: Int,
    @field:Json(name = "next") val next: String?,
    @field:Json(name = "previous") val previous: String?,
    @field:Json(name = "results") val results: List<T>
)

internal fun PageIndex.getOffset(pageSize: Int) = this * pageSize
internal fun PageIndex.getLimit(pageSize: Int) = this.getOffset(pageSize) + pageSize

internal fun <JSON, MODEL> PagedResource<JSON>.toPage(index: PageIndex, mapper: (JSON) -> MODEL): Page<MODEL> =
    Page(
        index = index,
        totalCount = count,
        content = results.map(mapper)
    )
