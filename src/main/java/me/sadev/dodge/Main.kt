package me.sadev.dodge

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.grinderwolf.swm.api.SlimePlugin
import me.sadev.dodge.arena.enums.ArenaMode
import me.sadev.dodge.arena.players.PlayersManager
import me.sadev.dodge.arena.players.toDodgePlayer
import me.sadev.dodge.manager.GameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: Main
        lateinit var slimeAPI: SlimePlugin
        lateinit var manager:  GameManager
        lateinit var playerManager: PlayersManager
    }

    override fun onEnable() {
        instance = this
        slimeAPI = server.pluginManager.getPlugin("SlimeWorldManager") as SlimePlugin
        manager = GameManager()
        playerManager = PlayersManager()
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        val game = manager.createGame(ArenaMode.SQUADS)
        e.player.sendMessage("Created game ${game.getUUID()}")
    }

    @EventHandler
    fun onShift(e: PlayerToggleSneakEvent) {
        if (!e.isSneaking) return
        val player = e.player.toDodgePlayer()
        if (player.game != null) player.game!!.removePlayer(player)
        manager.joinableGame()?.let {
            it.addPlayer(player)
            e.player.sendMessage("Joined game ${it.getUUID()}")
        }
    }
}
