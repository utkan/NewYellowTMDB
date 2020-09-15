package io.github.utkan.data.repository.paged

import javax.inject.Inject

interface PagingSourceKeyProvider {

    fun prevNextPair(nextPage: Int, currentPage: Int, totalPages: Int): Pair<Int?, Int?>

    class Impl @Inject constructor() : PagingSourceKeyProvider {

        override fun prevNextPair(nextPage: Int, currentPage: Int, totalPages: Int): Pair<Int?, Int?> {

            val prevKey = if (nextPage == 1) null else nextPage - 1
            val plus = currentPage.plus(1)
            val nextKey = if (plus > totalPages) null else plus

            return Pair(prevKey, nextKey)
        }
    }
}
