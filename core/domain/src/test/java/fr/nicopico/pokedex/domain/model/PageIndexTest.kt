package fr.nicopico.pokedex.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PageIndexTest {

    @Test
    fun `on the first page, offset should be 0`() {
        // Given
        val pageSize = 23
        val pageIndex = 0

        // When
        val offset = pageIndex.getOffset(pageSize)

        // Then
        assertThat(offset).isEqualTo(0)
    }

    @Test
    fun `on the first page, limit should be the page size`() {
        // Given
        val pageSize = 51
        val pageIndex = 0

        // When
        val limit = pageIndex.getLimit(pageSize)

        // Then
        assertThat(limit).isEqualTo(pageSize)
    }

    @Test
    fun `on the second page, offset should be equal to the page size`() {
        // Given
        val pageSize = 42
        val pageIndex = 1

        // When
        val offset = pageIndex.getOffset(pageSize)

        // Then
        assertThat(offset).isEqualTo(pageSize)
    }

    @Test
    fun `on the second page, limit should be equal to twice the page size`() {
        // Given
        val pageSize = 13
        val pageIndex = 1

        // When
        val limit = pageIndex.getLimit(pageSize)

        // Then
        assertThat(limit).isEqualTo(pageSize * 2)
    }
}
