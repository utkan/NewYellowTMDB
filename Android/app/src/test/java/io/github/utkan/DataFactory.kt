package io.github.utkan

import io.github.utkan.data.network.dto.DatesDto
import io.github.utkan.data.network.dto.MovieDto
import io.github.utkan.data.network.dto.MovieListDto
import io.github.utkan.data.repository.model.Movie

object DataFactory {
    val MOVIE = Movie(
        id = 1,
        posterPath = "posterPath",
        adult = false,
        overview = "overview",
        releaseDate = "releaseDate",
        originalTitle = "originalTitle",
        originalLanguage = "originalLanguage",
        title = "title",
        backdropUrl = "backdropPath",
        popularity = 1.1,
        voteCount = 1,
        video = false,
        voteAverage = 1.1,
        genreIds = listOf(1)
    )

    val MOVIE_DTO = MovieDto(
        id = 1,
        posterPath = "posterPath",
        adult = false,
        overview = "overview",
        releaseDate = "releaseDate",
        originalTitle = "originalTitle",
        originalLanguage = "originalLanguage",
        title = "title",
        backdropPath = "backdropPath",
        popularity = 1.1,
        voteCount = 1,
        video = false,
        voteAverage = 1.1,
        genreIds = listOf(1)
    )

    val DATES_DTO = DatesDto(
        minimum = "any minimum",
        maximum = "any maximum"
    )

    val MOVIE_LIST_DTO = MovieListDto(
        page = 1,
        results = listOf(MOVIE_DTO),
        dates = DATES_DTO,
        totalPages = 1,
        totalResults = 10
    )
}
