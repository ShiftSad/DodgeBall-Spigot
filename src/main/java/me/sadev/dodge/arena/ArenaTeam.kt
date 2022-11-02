package me.sadev.dodge.arena

import me.sadev.dodge.api.SimplexLocation
import me.sadev.dodge.arena.players.DodgePlayer

data class ArenaTeam(
    val name: String,
    val players: MutableList<DodgePlayer>,

    val location: SimplexLocation
)
