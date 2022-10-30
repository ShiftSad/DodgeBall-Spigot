package me.sadev.dodge.games

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
        val time = measureTimeMillis {
            val templateWorld = slimeAPI.getWorld(templateName)
            val loader = templateWorld.loader
            slimeAPI.asyncLoadWorld(loader, templateName, true, templateWorld.propertyMap).thenAccept {
                val wrd = it.get().clone(uuid, loader, true)
                slimeAPI.generateWorld(wrd) // Generate the world
                Logger.debug { "Loaded world $wrd" }
                Bukkit.getScheduler().runTask(instance, Runnable {
                    arena = Bukkit.getWorld(wrd.name) ?: throw Exception("World can not be null")
                    arenaStatus = ArenaStatus.WAITING
                    Logger.debug { "World $arena loaded!" }
                })
            }
        }
        Logger.debug { "World $uuid loaded in $time ms" }
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
