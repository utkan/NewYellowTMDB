package io.github.utkan.data.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.MovieListDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieRemoteSourceTest {

    @Test
    fun getConfiguration() {
        runBlockingTest {
            // given
            val dto: ConfigurationDto = mock()
            val service: MovieService = mock()
            whenever(service.getConfiguration()).thenReturn(dto)
            val remoteSource = MovieRemoteSource.Impl(service)

            // when
            val output = remoteSource.getConfiguration()

            // then
            verify(service).getConfiguration()
            assertEquals(dto, output)
        }
    }

    @Test
    fun getNowPlayingMovies() {
        runBlockingTest {
            // given
            val page = 1
            val dto: MovieListDto = mock()
            val service: MovieService = mock()
            whenever(service.getNowPlayingMovies(page)).thenReturn(dto)
            val remoteSource = MovieRemoteSource.Impl(service)

            // when
            val output = remoteSource.getNowPlayingMovies(page)

            // then
            verify(service).getNowPlayingMovies(page)
            assertEquals(dto, output)
        }
    }

    @Test
    fun getMovieSearchResults() {
        runBlockingTest {
            // given
            val page = 1
            val searchTerm = "any"
            val dto: MovieListDto = mock()
            val service: MovieService = mock()
            whenever(service.getMovieSearchResults(searchTerm, page)).thenReturn(dto)
            val remoteSource = MovieRemoteSource.Impl(service)

            // when
            val output = remoteSource.getMovieSearchResults(searchTerm, page)

            // then
            verify(service).getMovieSearchResults(searchTerm, page)
            assertEquals(dto, output)
        }
    }
}
