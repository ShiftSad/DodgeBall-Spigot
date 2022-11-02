package me.sadev.dodge.arena

import me.sadev.dodge.api.SimplexLocation

data class ArenaConfiguration(
    val name: String,
    val maxPlayers: Int,
    val minPlayers: Int,

    val spawn: SimplexLocation,

    val redTeamSpawn: SimplexLocation,
    val blueTeamSpawn: SimplexLocation
)
