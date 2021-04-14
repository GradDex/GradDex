package com.graddex.graddex.models

import com.squareup.moshi.Json

data class PokemonResponseResult(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

data class PokemonResponse(
    val count: Int, val next: String?,
    val previous: String?, val results: List<PokemonResponseResult>
)
