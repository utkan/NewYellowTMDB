package io.github.utkan.data.repository.mapper

import io.github.utkan.DataFactory
import io.github.utkan.data.repository.model.MovieList
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieListMapperTest {

    @Test
    fun map() {
        // given
        val input = DataFactory.MOVIE_LIST_DTO
        val movieListMapper = MovieMapper()
        val datesMapper = DatesMapper()
        val mapper = MovieListMapper(
            movieListMapper = movieListMapper,
            datesMapper = datesMapper
        )

        // when
        val output = mapper.map(input)

        // then
        assertEquals(
            MovieList(
                page = input.page,
                movies = input.results.map { movieListMapper.map(it) },
                dates = input.dates?.let { datesMapper.map(it) },
                totalPages = input.totalPages,
                totalResults = input.totalResults
            ),
            output
        )
    }

    @Test
    fun `map with null dates`() {
        // given
        val input = DataFactory.MOVIE_LIST_DTO.copy(
            dates = null
        )
        val movieListMapper = MovieMapper()
        val datesMapper = DatesMapper()
        val mapper = MovieListMapper(
            movieListMapper = movieListMapper,
            datesMapper = datesMapper
        )

        // when
        val output = mapper.map(input)

        // then
        assertEquals(
            MovieList(
                page = input.page,
                movies = input.results.map { movieListMapper.map(it) },
                dates = input.dates?.let { datesMapper.map(it) },
                totalPages = input.totalPages,
                totalResults = input.totalResults
            ),
            output
        )
    }
}
