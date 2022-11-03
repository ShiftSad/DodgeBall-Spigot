package me.sadev.dodge.listeners

import me.sadev.dodge.Main
import me.sadev.dodge.arena.players.toDodgePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object RQuitEvent : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        // Remove the player from the game when quit.
        val player = e.player.toDodgePlayer()
        player.game?.removePlayer(player)
    }

}