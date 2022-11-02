package me.sadev.dodge.arena.players

import me.sadev.dodge.Main.Companion.playerManager
import me.sadev.dodge.arena.ArenaGame
import org.bukkit.entity.Player

data class DodgePlayer(
    val player: Player,
    var game: ArenaGame? = null,

    var local_kills: Int = 0,
    var local_deaths: Int = 0
)

fun Player.toDodgePlayer(): DodgePlayer {
    playerManager.getUser(this)?.let { return it }
    return playerManager.createUser(this)
}

