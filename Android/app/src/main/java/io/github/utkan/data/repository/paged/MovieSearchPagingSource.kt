package io.github.utkan.data.repository.paged

import androidx.paging.PagingSource
import io.github.utkan.data.repository.Repository
import io.github.utkan.data.repository.model.Movie
import timber.log.Timber

interface SearchSource {
    var searchTerm: String
}

class MovieSearchPagingSource(
    private val repository: Repository,
    private val keyProvider: PagingSourceKeyProvider
) : PagingSource<Int, Movie>(), SearchSource {

    override var searchTerm = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val nextPage = params.key ?: 1
        return repository.getMovieSearchResults(searchTerm, nextPage).fold({
            Timber.e(it)
            LoadResult.Error(it)
        }, { list ->
            val (prevKey, nextKey) = keyProvider.prevNextPair(nextPage, list.page, list.totalPages)
            LoadResult.Page(
                data = list.movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        })
    }
}
