package fr.nicopico.pokedex.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.random.Random

class PageIndexTest {

    @Test
    fun `on the first page, offset should be 0 and limit to the page size`() {
        val pageSize = Random.nextInt()
        val pageIndex = 0
        val offset = pageIndex.getOffset(pageSize)
        val limit = pageIndex.getLimit(pageSize)
        assertThat(offset).isEqualTo(0)
        assertThat(limit).isEqualTo(pageSize)
    }

    @Test
    fun `on the second page, offset should be equal to the page size`() {
        val pageSize = Random.nextInt()
        val pageIndex = 1
        val offset = pageIndex.getOffset(pageSize)
        val limit = pageIndex.getLimit(pageSize)
        assertThat(offset).isEqualTo(pageSize)
        assertThat(limit).isEqualTo(pageSize * 2)
    }
}
