package io.github.utkan.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.utkan.common.Mapper
import io.github.utkan.data.network.MovieRemoteSource
import io.github.utkan.data.network.dto.ConfigurationDto
import io.github.utkan.data.network.dto.MovieListDto
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.data.repository.model.MovieList
import io.github.utkan.di.MovieListDtoMapper
import javax.inject.Inject

interface Repository {

    suspend fun getNowPlayingMovies(page: Int): Either<Throwable, MovieList>
    suspend fun getMovieSearchResults(searchTerm: String, page: Int): Either<Throwable, MovieList>

    class Impl @Inject constructor(
        private val movieRemoteSource: MovieRemoteSource,
        @MovieListDtoMapper private val movieListMapper: Mapper<MovieListDto, MovieList>
    ) : Repository {

        private var configuration: ConfigurationDto? = null

        override suspend fun getNowPlayingMovies(page: Int): Either<Throwable, MovieList> {
            return Either.runCatching {
                mapUrl(movieListMapper.map(movieRemoteSource.getNowPlayingMovies(page))).right()
            }.getOrElse { it.left() }
        }

        override suspend fun getMovieSearchResults(searchTerm: String, page: Int): Either<Throwable, MovieList> {
            return Either.runCatching {
                mapUrl(movieListMapper.map(movieRemoteSource.getMovieSearchResults(searchTerm, page))).right()
            }.getOrElse { it.left() }
        }

        private suspend fun mapUrl(mappedMovies: MovieList): MovieList {
            val url = getBaseUrl()
            return mappedMovies.copy(movies = mappedMovies.movies.map { buildUrl(it, url) })
        }

        private suspend fun getBaseUrl(): String {
            val imagesDto = configuration?.images ?: movieRemoteSource.getConfiguration().also { configuration = it }.images
            return imagesDto?.secureBaseUrl!!
        }

        private fun buildUrl(
            movie: Movie,
            url: String?
        ) = movie.copy(backdropUrl = "$url%s${movie.backdropUrl}")
    }
}
