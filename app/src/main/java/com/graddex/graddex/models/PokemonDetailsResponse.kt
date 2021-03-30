package com.graddex.graddex.models

data class PokemonAbility(val name: String, val url: String)

data class PokemonAbilities(val ability: PokemonAbility, val is_hidden: Boolean)

data class PokemonType(val name: String, val url: String)

data class PokemonTypes(val slot: Int, val type: PokemonType)

data class PokemonSprites(val back_default: String, val front_default: String)

data class PokemonLocationEncounters( val location_area_encounters: String )

data class PokemonDetailsResponse(
    val abilities: List<PokemonAbilities>, val name: String, val types: List<PokemonTypes>,
    val sprites: PokemonSprites, val location_area_encounters: String
)

data class PokemonLocation(val name: String, val url: String)

