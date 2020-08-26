package io.github.utkan.ui.screen.mapper

import io.github.utkan.common.Mapper
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.ui.MovieViewItem
import javax.inject.Inject

class MovieViewItemMapper @Inject constructor() : Mapper<Movie, MovieViewItem> {
    override fun map(input: Movie): MovieViewItem {
        return MovieViewItem(
            id = input.id,
            posterPath = input.posterPath,
            adult = input.adult,
            overview = input.overview,
            releaseDate = input.releaseDate,
            originalTitle = input.originalTitle,
            originalLanguage = input.originalLanguage,
            title = input.title,
            backdropUrl = input.backdropUrl,
            popularity = input.popularity,
            voteCount = input.voteCount,
            video = input.video,
            voteAverage = input.voteAverage,
            genreIds = input.genreIds,
        )
    }
}
