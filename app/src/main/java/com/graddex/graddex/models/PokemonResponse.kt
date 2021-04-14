package com.graddex.graddex.models

data class PokemonResponseResult(val name: String, val url: String)

data class PokemonResponse(
    val count: Int, val next: String?,
    val previous: String?, val results: List<PokemonResponseResult>
)
