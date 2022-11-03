package me.sadev.dodge.listeners

import me.sadev.dodge.arena.players.toDodgePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object RMoveEvent : Listener {

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val p = e.player.toDodgePlayer()
        val game = p.game ?: return


    }
}