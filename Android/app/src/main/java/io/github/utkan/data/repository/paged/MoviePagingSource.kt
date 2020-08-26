package io.github.utkan.data.repository.paged

import androidx.paging.PagingSource
import io.github.utkan.data.repository.Repository
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.data.repository.model.MovieList
import timber.log.Timber

class MoviePagingSource(
    private val repository: Repository,
    private val keyProvider: PagingSourceKeyProvider
) : PagingSource<Int, Movie>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val nextPage = params.key ?: 1
        return repository.getNowPlayingMovies(nextPage).fold({
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
