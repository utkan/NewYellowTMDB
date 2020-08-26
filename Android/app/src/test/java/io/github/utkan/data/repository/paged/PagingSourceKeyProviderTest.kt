package io.github.utkan.data.repository.paged

import org.junit.Assert.*
import org.junit.Test

class PagingSourceKeyProviderTest {

    @Test
    fun `when next page is 1, previous key is null`() {
        // given
        val nextPage = 1
        val currentPage = 1
        val totalPages = 10
        val expected = Pair<Int?, Int?>(null, 2)

        val provider = PagingSourceKeyProvider.Impl()

        // when
        val output = provider.prevNextPair(
            nextPage = nextPage,
            currentPage = currentPage,
            totalPages = totalPages
        )

        // then
        assertEquals(
            expected,
            output
        )
    }

    @Test
    fun `when next page is bigger than 1, previous key is -1`() {
        // given
        val nextPage = 2
        val currentPage = 1
        val totalPages = 10
        val expected = Pair<Int?, Int?>(nextPage - 1, 2)

        val provider = PagingSourceKeyProvider.Impl()

        // when
        val output = provider.prevNextPair(
            nextPage = nextPage,
            currentPage = currentPage,
            totalPages = totalPages
        )

        // then
        assertEquals(
            expected,
            output
        )
    }

    @Test
    fun `when next key is bigger than total pages, next key is null`() {
        // given
        val nextPage = 10
        val currentPage = 10
        val totalPages = 10
        val expected = Pair<Int?, Int?>(nextPage - 1, null)

        val provider = PagingSourceKeyProvider.Impl()

        // when
        val output = provider.prevNextPair(
            nextPage = nextPage,
            currentPage = currentPage,
            totalPages = totalPages
        )

        // then
        assertEquals(
            expected,
            output
        )
    }
}
