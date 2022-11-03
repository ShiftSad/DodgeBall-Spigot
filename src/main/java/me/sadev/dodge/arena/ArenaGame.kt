package me.sadev.dodge.arena

import me.sadev.dodge.arena.enums.ArenaStatus
import me.sadev.dodge.arena.enums.ArenaMode
import me.sadev.dodge.arena.players.DodgePlayer

interface ArenaGame {

    fun start()
    fun end()

    fun addPlayer(player: DodgePlayer) : ArenaGame
    fun removePlayer(player: DodgePlayer) : ArenaGame

    fun getPlayers(): List<DodgePlayer>
    fun getPlayer(player: DodgePlayer): DodgePlayer?

    fun getArenaStatus(): ArenaStatus

    fun getUUID(): String
}