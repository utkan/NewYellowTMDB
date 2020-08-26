package io.github.utkan.data.network.dto

import com.squareup.moshi.Json

data class ImagesDto(
    @Json(name = "base_url")
    val baseUrl: String? = null,

    @Json(name = "secure_base_url")
    val secureBaseUrl: String? = null,

    @Json(name = "backdrop_sizes")
    val backdropSizes: List<String>? = null,

    @Json(name = "logo_sizes")
    val logoSizes: List<String>? = null,

    @Json(name = "poster_sizes")
    val posterSizes: List<String>? = null,

    @Json(name = "profile_sizes")
    val profileSizes: List<String>? = null,

    @Json(name = "still_sizes")
    val stillSizes: List<String>? = null
)
