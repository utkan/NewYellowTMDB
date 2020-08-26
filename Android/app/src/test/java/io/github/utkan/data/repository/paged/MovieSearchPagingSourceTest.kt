package io.github.utkan.data.repository.paged

import androidx.paging.PagingSource
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.github.utkan.data.repository.Repository
import io.github.utkan.DataFactory
import io.github.utkan.data.repository.mapper.DatesMapper
import io.github.utkan.data.repository.mapper.MovieListMapper
import io.github.utkan.data.repository.mapper.MovieMapper
import io.github.utkan.data.repository.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException

@ExperimentalCoroutinesApi
class MovieSearchPagingSourceTest {

    @Test
    fun `when repository returns error, result is LoadResult Error`() {
        runBlockingTest {
            // given
            val exception = IOException()
            val searchTerm = ""
            val nextPage = 1
            val repository: Repository = mock()
            val keyProvider: PagingSourceKeyProvider = mock()
            whenever(repository.getMovieSearchResults(searchTerm, nextPage)).thenReturn(exception.left())
            val pagingSource = MovieSearchPagingSource(
                repository = repository,
                keyProvider = keyProvider
            )
            val params: PagingSource.LoadParams<Int> = mock()

            // when
            val output = pagingSource.load(params)

            // then
            assertEquals(
                PagingSource.LoadResult.Error<Int, Movie>(exception),
                output
            )
        }
    }

    @Test
    fun `when repository returns result, result is LoadResult Page`() {
        runBlockingTest {
            // given
            val searchTerm = ""
            val input = DataFactory.MOVIE_LIST_DTO
            val movieListMapper = MovieMapper()
            val datesMapper = DatesMapper()
            val mapper = MovieListMapper(
                movieListMapper = movieListMapper,
                datesMapper = datesMapper
            )
            val list = mapper.map(input)
            val nextPage = 1
            val repository: Repository = mock()
            val keyProvider: PagingSourceKeyProvider = mock()
            val pair = Pair(1, 2)
            whenever(keyProvider.prevNextPair(nextPage, list.page, list.totalPages)).thenReturn(pair)
            whenever(repository.getMovieSearchResults(searchTerm, nextPage)).thenReturn(list.right())

            val pagingSource = MovieSearchPagingSource(
                repository = repository,
                keyProvider = keyProvider
            )
            val params: PagingSource.LoadParams<Int> = mock()
            // when
            val output = pagingSource.load(params)

            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = list.movies,
                    prevKey = pair.first,
                    nextKey = pair.second
                ),
                output
            )
        }
    }

    @Test
    fun `when repository returns result, result is LoadResult Searched Page`() {
        runBlockingTest {
            // given
            val searchTerm = "any"
            val input = DataFactory.MOVIE_LIST_DTO
            val movieListMapper = MovieMapper()
            val datesMapper = DatesMapper()
            val mapper = MovieListMapper(
                movieListMapper = movieListMapper,
                datesMapper = datesMapper
            )
            val list = mapper.map(input)
            val nextPage = 1
            val repository: Repository = mock()
            val keyProvider: PagingSourceKeyProvider = mock()
            val pair = Pair(1, 2)
            whenever(keyProvider.prevNextPair(nextPage, list.page, list.totalPages)).thenReturn(pair)
            whenever(repository.getMovieSearchResults(searchTerm, nextPage)).thenReturn(list.right())

            val pagingSource = MovieSearchPagingSource(
                repository = repository,
                keyProvider = keyProvider
            )
            (pagingSource as SearchSource).searchTerm = searchTerm
            val params: PagingSource.LoadParams<Int> = mock()
            // when
            val output = pagingSource.load(params)

            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = list.movies,
                    prevKey = pair.first,
                    nextKey = pair.second
                ),
                output
            )
        }
    }

    @Test
    fun `when repository returns result, result is LoadResult Next Page`() {
        runBlockingTest {
            // given
            val searchTerm = ""
            val input = DataFactory.MOVIE_LIST_DTO
            val movieListMapper = MovieMapper()
            val datesMapper = DatesMapper()
            val mapper = MovieListMapper(
                movieListMapper = movieListMapper,
                datesMapper = datesMapper
            )
            val list = mapper.map(input)
            val nextPage = 2
            val repository: Repository = mock()
            val keyProvider: PagingSourceKeyProvider = mock()
            val pair = Pair(2, 3)
            whenever(keyProvider.prevNextPair(nextPage, list.page, list.totalPages)).thenReturn(pair)
            whenever(repository.getMovieSearchResults(searchTerm, nextPage)).thenReturn(list.right())

            val pagingSource = MovieSearchPagingSource(
                repository = repository,
                keyProvider = keyProvider
            )
            val params: PagingSource.LoadParams<Int> = mock()
            whenever(params.key).thenReturn(2)
            // when
            val output = pagingSource.load(params)

            // then
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = list.movies,
                    prevKey = pair.first,
                    nextKey = pair.second
                ),
                output
            )
        }
    }
}
