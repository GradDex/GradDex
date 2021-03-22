package com.graddex.graddex.models

data class PokemonType(
    val name: String, val url: String
)

data class PokemonTypes(
    val slot: Int, val type: PokemonType
)

data class PokemonDetailsResponse(
    val name: String, val types: List<PokemonTypes>
)