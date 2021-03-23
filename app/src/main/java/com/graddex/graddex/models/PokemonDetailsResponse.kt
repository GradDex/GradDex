package com.graddex.graddex.models

data class PokemonType(
    val name: String, val url: String
)

data class PokemonTypes(
    val slot: Int, val type: PokemonType
)

data class PokemonSprites(
    val back_default: String, val front_default: String
)

data class PokemonDetailsResponse(
    val name: String, val types: List<PokemonTypes>, val sprites: PokemonSprites
)