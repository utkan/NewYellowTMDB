package io.github.utkan.data.network

import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.MovieListDto
import javax.inject.Inject

interface MovieRemoteSource {

    suspend fun getNowPlayingMovies(page: Int): MovieListDto

    suspend fun getMovieSearchResults(searchTerm: String, page: Int): MovieListDto

    suspend fun getConfiguration(): ConfigurationDto

    class Impl @Inject constructor(private val service: MovieService) : MovieRemoteSource {

        override suspend fun getNowPlayingMovies(page: Int): MovieListDto {
            return service.getNowPlayingMovies(page)
        }

        override suspend fun getMovieSearchResults(searchTerm: String, page: Int): MovieListDto {
            return service.getMovieSearchResults(searchTerm, page)
        }

        override suspend fun getConfiguration(): ConfigurationDto {
            return service.getConfiguration()
        }
    }
}
