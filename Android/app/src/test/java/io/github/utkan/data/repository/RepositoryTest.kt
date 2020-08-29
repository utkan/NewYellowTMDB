package io.github.utkan.data.repository

import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.github.utkan.DataFactory
import io.github.utkan.data.network.MovieRemoteSource
import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.ImagesDto
import io.github.utkan.data.repository.mapper.DatesMapper
import io.github.utkan.data.repository.mapper.MovieListMapper
import io.github.utkan.data.repository.mapper.MovieMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {
    private val movieRemoteSource: MovieRemoteSource = mock()
    private val movieListMapper = MovieMapper()
    private val datesMapper = DatesMapper()
    private val mapper = MovieListMapper(
        movieListMapper = movieListMapper,
        datesMapper = datesMapper
    )

    @Test
    fun `when remote source throws, result is left movies`() {
        runBlockingTest {
            // given
            val exception = IllegalArgumentException()
            val page = 1
            whenever(movieRemoteSource.getNowPlayingMovies(page)).thenThrow(exception)
            val repository = Repository.Impl(
                movieRemoteSource = movieRemoteSource,
                movieListMapper = mapper
            )

            // when
            val output = repository.getNowPlayingMovies(page)

            // then
            assertEquals(
                exception.left(),
                output
            )
        }
    }

    @Test
    fun `when remote source throws, result is right movies`() {
        runBlockingTest {
            // given
            val config: ConfigurationDto = mock()
            val images: ImagesDto = mock()
            val baseUrl = "https://any.any.any/"
            whenever(images.secureBaseUrl).thenReturn(baseUrl)
            whenever(config.images).thenReturn(images)
            whenever(movieRemoteSource.getConfiguration()).thenReturn(config)
            val page = 1
            whenever(movieRemoteSource.getNowPlayingMovies(page)).thenReturn(DataFactory.MOVIE_LIST_DTO)
            val repository = Repository.Impl(
                movieRemoteSource = movieRemoteSource,
                movieListMapper = mapper
            )

            // when
            val output = repository.getNowPlayingMovies(page)

            // then
            val mapped = mapper.map(DataFactory.MOVIE_LIST_DTO)
            assertEquals(
                mapped.copy(movies = mapped.movies.map { it.copy(backdropUrl = baseUrl + "w300" + it.backdropUrl) }).right(),
                output
            )
        }
    }

    @Test
    fun `when remote source throws, result is left search movies`() {
        runBlockingTest {
            // given
            val searchTerm = "any"
            val exception = IllegalArgumentException()
            val page = 1
            whenever(movieRemoteSource.getMovieSearchResults(searchTerm, page)).thenThrow(exception)
            val repository = Repository.Impl(
                movieRemoteSource = movieRemoteSource,
                movieListMapper = mapper
            )

            // when
            val output = repository.getMovieSearchResults(searchTerm, page)

            // then
            assertEquals(
                exception.left(),
                output
            )
        }
    }

    @Test
    fun `when remote source throws, result is right search movies`() {
        runBlockingTest {
            // given
            val searchTerm = "any"
            val page = 1
            val config: ConfigurationDto = mock()
            val images: ImagesDto = mock()
            val baseUrl = "https://any.any.any/"
            whenever(images.secureBaseUrl).thenReturn(baseUrl)
            whenever(config.images).thenReturn(images)
            whenever(movieRemoteSource.getConfiguration()).thenReturn(config)
            whenever(movieRemoteSource.getMovieSearchResults(searchTerm, page)).thenReturn(DataFactory.MOVIE_LIST_DTO)
            val repository = Repository.Impl(
                movieRemoteSource = movieRemoteSource,
                movieListMapper = mapper
            )

            // when
            val output = repository.getMovieSearchResults(searchTerm, page)

            // then
            val mapped = mapper.map(DataFactory.MOVIE_LIST_DTO)
            assertEquals(
                mapped.copy(movies = mapped.movies.map { it.copy(backdropUrl = baseUrl + "w300" + it.backdropUrl) }).right(),
                output
            )
        }
    }

}
