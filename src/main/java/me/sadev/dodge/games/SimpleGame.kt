package me.sadev.dodge.games

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException
import com.grinderwolf.swm.api.exceptions.NewerFormatException
import com.grinderwolf.swm.api.exceptions.UnknownWorldException
import com.grinderwolf.swm.api.exceptions.WorldInUseException
import com.grinderwolf.swm.api.loaders.SlimeLoader
import com.grinderwolf.swm.api.world.SlimeWorld
import me.sadev.dodge.Main
import me.sadev.dodge.Main.Companion.instance
import me.sadev.dodge.Main.Companion.slimeAPI
import org.bukkit.Bukkit
import org.bukkit.World
import org.tinylog.Logger
import java.util.*
import kotlin.system.measureTimeMillis


class SimpleGame(
    private val gameMode: DodgeGameMode,
    private val templateName: String,
    private val uuid: String = UUID.randomUUID().toString()
) : DodgeGame {

    private var arenaStatus: ArenaStatus = ArenaStatus.LOADING
    private lateinit var arena: World

    init {
        val scheduler = Bukkit.getScheduler()
        scheduler.runTaskAsynchronously(instance, Runnable {
            val time = measureTimeMillis {
                val templateWorld = slimeAPI.getWorld(templateName)
                val loader = templateWorld.loader
                // Cloning the world into a new world
                slimeAPI.loadWorld(loader, templateName, true, templateWorld.propertyMap).clone(uuid)
                // Calls to getWorld needs to be in the main thread
                scheduler.runTask(instance, Runnable {
                    arena = Bukkit.getWorld(uuid)!!
                    arenaStatus = ArenaStatus.WAITING
                })
            }
            Logger.debug { "World $uuid loaded in $time ms" }
        })
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun addPlayer(player: DodgePlayer): DodgeGame {
        TODO("Not yet implemented")
    }

    override fun removePlayer(player: DodgePlayer): DodgeGame {
        TODO("Not yet implemented")
    }

    override fun getPlayers(): List<DodgePlayer> {
        TODO("Not yet implemented")
    }

    override fun getPlayer(player: DodgePlayer): DodgePlayer? {
        TODO("Not yet implemented")
    }

    override fun getUUID(): String {
        return uuid
    }

    override fun getGameMode(): DodgeGameMode {
        return gameMode
    }
}
