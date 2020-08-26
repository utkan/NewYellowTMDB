package io.github.utkan.ui.screen.mapper

import io.github.utkan.DataFactory
import io.github.utkan.ui.MovieViewItem
import org.junit.Test

import org.junit.Assert.*

class MovieViewItemMapperTest {

    @Test
    fun map() {
        // given
        val input = DataFactory.MOVIE
        val mapper = MovieViewItemMapper()

        // when
        val output = mapper.map(input)

        // then
        assertEquals(
            MovieViewItem(
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
            ),
            output
        )
    }
}
