package me.sadev.dodge.games

data class ArenaConfiguration(
    val name: String,
    val maxPlayers: Int,
    val minPlayers: Int,

    val spawn: SimplexLocation,
)
