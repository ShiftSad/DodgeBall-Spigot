package me.sadev.dodge.arena.players

import org.bukkit.entity.Player

class PlayersManager {

    private val players = mutableListOf<DodgePlayer>()

    fun createUser(player: Player) : DodgePlayer {
        val dodgePlayer = DodgePlayer(player)
        players.add(dodgePlayer)
        return dodgePlayer
    }

    fun getUser(player: Player): DodgePlayer? {
        return players.firstOrNull { it.player == player }
    }
}