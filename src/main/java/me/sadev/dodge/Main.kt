package me.sadev.dodge

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.grinderwolf.swm.api.SlimePlugin
import me.sadev.dodge.games.DodgeGameMode
import me.sadev.dodge.manager.GameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: Main
        lateinit var slimeAPI: SlimePlugin
        lateinit var manager:  GameManager
    }

    override fun onEnable() {
        instance = this
        slimeAPI = server.pluginManager.getPlugin("SlimeWorldManager") as SlimePlugin
        manager = GameManager()
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        manager.createGame(DodgeGameMode.SQUADS)
    }
}