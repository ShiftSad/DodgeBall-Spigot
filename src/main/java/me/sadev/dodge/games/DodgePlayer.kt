package me.sadev.dodge.games

import org.bukkit.entity.Player

data class DodgePlayer(
    val player: Player,
)

fun Player.toDodgePlayer(): DodgePlayer {
    return DodgePlayer(this)
}

