package io.github.utkan.data.repository.mapper

import io.github.utkan.common.Mapper
import io.github.utkan.data.network.dto.MovieDto
import io.github.utkan.data.repository.model.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() : Mapper<MovieDto, Movie> {
    override fun map(input: MovieDto): Movie {
        return Movie(
            id = input.id,
            posterPath = input.posterPath,
            adult = input.adult,
            overview = input.overview,
            releaseDate = input.releaseDate,
            originalTitle = input.originalTitle,
            originalLanguage = input.originalLanguage,
            title = input.title,
            backdropUrl = input.backdropPath,
            popularity = input.popularity,
            voteCount = input.voteCount,
            video = input.video,
            voteAverage = input.voteAverage,
            genreIds = input.genreIds
        )
    }
}
