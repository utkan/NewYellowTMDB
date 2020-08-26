package io.github.utkan.data.repository.mapper

import io.github.utkan.common.Mapper
import io.github.utkan.data.network.dto.DatesDto
import io.github.utkan.data.network.dto.MovieDto
import io.github.utkan.data.network.dto.MovieListDto
import io.github.utkan.data.repository.model.Dates
import io.github.utkan.data.repository.model.Movie
import io.github.utkan.data.repository.model.MovieList
import io.github.utkan.di.DatesDtoMapper
import io.github.utkan.di.MovieDtoMapper
import javax.inject.Inject

class MovieListMapper @Inject constructor(
    @MovieDtoMapper private val movieListMapper: Mapper<MovieDto, Movie>,
    @DatesDtoMapper private val datesMapper: Mapper<DatesDto, Dates>
) : Mapper<MovieListDto, MovieList> {
    override fun map(input: MovieListDto): MovieList {
        val dates = input.dates
        return MovieList(
            page = input.page,
            movies = input.results.map { movieListMapper.map(it) },
            dates = dates?.let { datesMapper.map(it) },
            totalPages = input.totalPages,
            totalResults = input.totalResults
        )
    }
}
