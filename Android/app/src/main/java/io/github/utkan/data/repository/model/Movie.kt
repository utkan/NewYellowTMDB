package io.github.utkan.data.repository.model

data class Movie(
    val id: Long,
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    val releaseDate: String?,
    val originalTitle: String,
    val originalLanguage: String,
    val title: String,
    val backdropUrl: String?,
    val popularity: Double,
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Double,
    val genreIds: List<Int>
)
