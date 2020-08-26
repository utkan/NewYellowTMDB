package io.github.utkan.data.repository.model

data class MovieList(
    val page: Int,
    val movies: List<Movie>,
    val dates: Dates?,
    val totalPages: Int,
    val totalResults: Int
)
