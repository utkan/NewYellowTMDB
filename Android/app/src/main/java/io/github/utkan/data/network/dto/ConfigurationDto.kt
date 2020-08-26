package io.github.utkan.data.network.dto

import com.squareup.moshi.Json

data class ConfigurationDto(
    @Json(name = "images")
    val images: ImagesDto? = null,

    @Json(name = "change_keys")
    val changeKeys: List<String>? = null
)
