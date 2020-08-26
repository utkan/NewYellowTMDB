package io.github.utkan.data.network.dto

import com.squareup.moshi.Json

data class MovieDto(
    val id: Long,
    @Json(name = "poster_path")
    val posterPath: String?,
    val adult: Boolean,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "original_title")
    val originalTitle: String,
    @Json(name = "original_language")
    val originalLanguage: String,
    val title: String,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val popularity: Double,
    @Json(name = "vote_count")
    val voteCount: Int,
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "genre_ids")
    val genreIds: List<Int>
)
