package me.sadev.dodge.api.events

import me.sadev.dodge.arena.ArenaGame
import me.sadev.dodge.arena.players.DodgePlayer
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerQuitMatchEvent(
    val player: DodgePlayer,
    val game: ArenaGame,
    private var isCancelled: Boolean = false
) : Event(), Cancellable {

    private val handlers = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlers
    }

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }
}