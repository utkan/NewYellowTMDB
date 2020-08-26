package io.github.utkan.data.network.dto

import com.squareup.moshi.Json

data class MovieListDto(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val results: List<MovieDto>,
    @Json(name = "dates")
    val dates: DatesDto?,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)
