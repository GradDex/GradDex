package com.graddex.graddex.models

data class PokemonDetails_NameUrl(val name: String, val url: String)


data class PokemonDetails_Abilities(
    val ability: List<PokemonDetails_NameUrl>, val isHidden: Boolean, val slot: Int
)

data class PokemonDetails_GameIndices(val gameIndex: Int, val version: List<PokemonDetails_NameUrl>)

data class PokemonDetails_VersionGroupDetails(
    val levelLearnedAt: Int, val moveLearnMethod: List<PokemonDetails_NameUrl>,
    val versionGroup: List<PokemonDetails_NameUrl>
)

data class PokemonDetails_Moves(
    val move: List<PokemonDetails_NameUrl>,
    val versionGroupDetail: List<PokemonDetails_VersionGroupDetails>
)

data class PokemonDetails_SpritesDreamWorld(val frontDefault: String?, val frontFemale: String?)

data class PokemonDetails_SpriteOfficialArtwork(val frontDefault: String?)

data class PokemonDetails_SpritesOthers(
    val dreamWorld: List<PokemonDetails_SpritesDreamWorld>,
    val officialArtwork: List<PokemonDetails_SpriteOfficialArtwork>
)

data class PokemonDetails_Sprites(
    val backDefault: String?, val backFemale: String?, val backShiny: String?,
    val backShinyFemale: String?, val frontDefault: String?, val frontFemale: String?,
    val frontShiny: String?, val frontShinyFemale: String?,
    val other: List<PokemonDetails_SpritesOthers>
)

data class PokemonDetails_Stats(
    val baseStat: Int, val effort: Int, val stat: List<PokemonDetails_NameUrl>
)

data class PokemonDetails_Types(val slot: Int, val type: List<PokemonDetails_NameUrl>)

data class PokemonDetailsResponse(
    val abilities: List<PokemonDetails_Abilities>, val baseExperience: Int,
    val forms: List<PokemonDetails_NameUrl>, val gameIndices: List<PokemonDetails_GameIndices>,
    val height: Int, /*val heldItems: List<>,*/ val id: Int, val isDefault: Boolean,
    val locationAreaEncounters: String, val moves: List<PokemonDetails_Moves>, val name: String,
    val order: Int, /*val pastTypes: List<>,*/ val species: List<PokemonDetails_NameUrl>,
    val sprites: List<PokemonDetails_Sprites>, val stats: List<PokemonDetails_Stats>,
    val types: List<PokemonDetails_Types>, val weight: Int
)