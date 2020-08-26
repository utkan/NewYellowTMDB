package io.github.utkan.data.repository.mapper

import io.github.utkan.DataFactory
import io.github.utkan.data.repository.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {

    @Test
    fun map() {
        // given
        val input = DataFactory.MOVIE_DTO
        val mapper = MovieMapper()

        // when
        val output = mapper.map(input)

        // then
        assertEquals(
            Movie(
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
            ), output
        )
    }
}
