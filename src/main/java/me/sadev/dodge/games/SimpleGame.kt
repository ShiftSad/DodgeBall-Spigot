package me.sadev.dodge.games

import me.sadev.dodge.Main.Companion.instance
import me.sadev.dodge.Main.Companion.slimeAPI
import org.bukkit.Bukkit
import org.bukkit.World
import org.tinylog.Logger
import java.util.*


class SimpleGame(
    private val gameMode: DodgeGameMode,
    private val uuid: String = UUID.randomUUID().toString(),
    private val configuration: ArenaConfiguration,
    templateName: String,

    ) : DodgeGame {

    private var arenaStatus: ArenaStatus = ArenaStatus.LOADING
    private lateinit var arena: World

    private val teams = mutableListOf<Team>()

    init {
        val time = System.currentTimeMillis()
        val templateWorld = slimeAPI.getWorld(templateName)
        val loader = templateWorld.loader
        Bukkit.getScheduler().runTaskAsynchronously(instance, Runnable {
            val slimeWorld = slimeAPI.loadWorld(loader, templateName, true, templateWorld.propertyMap)
            if (slimeWorld == null) {
                Logger.error { "Failed to load world $templateName" }
                return@Runnable
            }
            val wrd = slimeWorld.clone(uuid, loader)
            Bukkit.getScheduler().runTask(instance, Runnable {
                slimeAPI.generateWorld(wrd) // Generate the world
                arena = Bukkit.getWorld(wrd.name) ?: throw Exception("World can not be null")
                arenaStatus = ArenaStatus.WAITING
                Logger.debug { "World $arena loaded in ${System.currentTimeMillis() - time} ms!" }
            })
        })
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun addPlayer(player: DodgePlayer): DodgeGame {
        if (arenaStatus != ArenaStatus.WAITING) { throw Exception("Game is not in waiting state") }
        teams.find { it.players.size < (configuration.maxPlayers / (teams.size + 1)) }
            ?.players?.add(player) ?: Team("Team ${teams.size + 1}", mutableListOf(player)).also { teams.add(it)
        }
        player.player.teleport(configuration.spawn.toLocation(arena))
        // TODO -> Send join message.
        return this
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

    override fun getArenaStatus(): ArenaStatus {
        return arenaStatus
    }
}
