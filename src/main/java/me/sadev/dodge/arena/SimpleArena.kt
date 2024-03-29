package me.sadev.dodge.arena

import me.sadev.dodge.Main.Companion.instance
import me.sadev.dodge.Main.Companion.slimeAPI
import me.sadev.dodge.api.events.ArenaStartEvent
import me.sadev.dodge.api.events.PlayerJoinMatchEvent
import me.sadev.dodge.api.events.PlayerQuitMatchEvent
import me.sadev.dodge.api.exceptions.WTFHowException
import me.sadev.dodge.api.libs.ItemBuilder
import me.sadev.dodge.arena.enums.ArenaStatus
import me.sadev.dodge.arena.enums.ArenaMode
import me.sadev.dodge.arena.players.DodgePlayer
import me.sadev.dodge.config.ConfigValues
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.scheduler.BukkitRunnable
import org.tinylog.Logger
import java.util.*


class SimpleArena(
    private val uuid: String = UUID.randomUUID().toString(),
    private val configuration: ArenaConfiguration,
    templateName: String,

    ) : ArenaGame {

    private var arenaStatus: ArenaStatus = ArenaStatus.LOADING
    private lateinit var arena: World

    private val teams = mutableListOf<ArenaTeam>()
    private val scheduler = Bukkit.getScheduler()

    private val players = mutableListOf<DodgePlayer>()
    private var countdownTask: Int = 20

    init {
        val time = System.currentTimeMillis()
        val templateWorld = slimeAPI.getWorld(templateName)
        val loader = templateWorld.loader

        teams.add(ArenaTeam("Red", mutableListOf(), configuration.redTeamSpawn))
        teams.add(ArenaTeam("Blue", mutableListOf(), configuration.blueTeamSpawn))

        start()

        scheduler.runTaskAsynchronously(instance, Runnable {
            val slimeWorld = slimeAPI.loadWorld(loader, templateName, true, templateWorld.propertyMap)
            if (slimeWorld == null) {
                Logger.error { "Failed to load world $templateName" }
                return@Runnable
            }
            val wrd = slimeWorld.clone(uuid, loader)
            scheduler.runTask(instance, Runnable {
                slimeAPI.generateWorld(wrd) // Generate the world
                arena = Bukkit.getWorld(wrd.name) ?: throw Exception("World can not be null")
                arenaStatus = ArenaStatus.WAITING
            })
            println("Arena $uuid loaded in ${System.currentTimeMillis() - time} ms!")
        })
    }

    override fun start() {
        // Create the task.
        scheduler.scheduleSyncRepeatingTask(instance, {
            // The game is idle, no need for logic.
            if (getPlayers().isEmpty())
            if (arenaStatus == ArenaStatus.LOADING) return@scheduleSyncRepeatingTask

            when (arenaStatus) {
                ArenaStatus.LOADING -> throw WTFHowException()
                ArenaStatus.WAITING -> {

                    // If there is not enough players...
                    if (getPlayers().size < configuration.minPlayers) {

                        // ...and the countdown is running, stop it.
                        if (countdownTask <= (ConfigValues.DefaultTimer.getInteger() - 1)) {
                            getPlayers().forEach { it.player.sendMessage(Component.text("Jogadores insuficientes, cancelando partida!")) }
                            arenaStatus = ArenaStatus.WAITING
                            countdownTask = ConfigValues.DefaultTimer.getInteger()
                            return@scheduleSyncRepeatingTask
                        }

                        // TODO -> Bossbar (Waiting for enough players (currentPlayers / minPlayers))
                        return@scheduleSyncRepeatingTask
                    }

                    // Enough players are reached, set the game to starting
                    getPlayers().forEach { it.player.sendMessage(Component.text("Partida iniciando em $countdownTask segundos!")) }
                    // TODO -> Bossbar (Starting in $countdownTask seconds)

                    arenaStatus = ArenaStatus.STARTING
                }
                ArenaStatus.STARTING -> {
                    // If the player count is not enough anymore
                    if (getPlayers().size < configuration.minPlayers) {
                        arenaStatus = ArenaStatus.WAITING
                        countdownTask = ConfigValues.DefaultTimer.getInteger()

                        // TODO -> Update BossBar
                        // Reset the player's EXP
                        getPlayers().forEach { it.player.level = 0; it.player.exp = 0f }

                        getPlayers().forEach { it.player.sendMessage(Component.text("Jogadores insuficientes, cancelando partida!")) }
                        return@scheduleSyncRepeatingTask
                    }

                    if (getPlayers().size == configuration.maxPlayers && countdownTask >= ConfigValues.DefaultEnoughPlayersTimer.getInteger()) {
                        countdownTask = ConfigValues.DefaultEnoughPlayersTimer.getInteger()
                        getPlayers().forEach { it.player.sendMessage(Component.text("Maximo de jogadores! Iniciando em $countdownTask.")) }
                        // TODO -> Update BossBar
                    }

                    countdownTask--
                    if (countdownTask <= 10) {
                        getPlayers().forEach {
                            it.player.playSound(it.player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                            it.player.sendMessage(Component.text("Iniciando em $countdownTask segundos!"))
                        }
                    }

                    // Update player level with timer.
                    getPlayers().forEach {
                        it.player.level = countdownTask
                        it.player.exp = countdownTask.toFloat() / ConfigValues.DefaultTimer.getInteger()
                    }

                    // If the timer is over
                    // start the game.
                    if (countdownTask <= 0) {
                        arenaStatus = ArenaStatus.INGAME

                        // Call the ArenaStartEvent for plugins to listen to
                        Bukkit.getPluginManager().callEvent(ArenaStartEvent(this))

                        getPlayers().forEach {
                            // TODO -> Update BossBar("Game started!")

                            // Reset the player's EXP
                            it.player.level = 0
                            it.player.exp = 0f

                            // Clear everyone's inventory
                            it.player.inventory.clear()
                            it.player.updateInventory()

                            // Just to be sure...
                            it.player.gameMode = GameMode.ADVENTURE
                            it.local_deaths = 0
                            it.local_kills = 0

                            // Get the team with fewer players
                            val team = teams.minByOrNull { getPlayers().size }
                            team?.players?.add(it)
                            // Fallback, if no team could not be found
                            if (team == null) teams[0].players.add(it)
                            Logger.debug { "Player ${it.player.name} joined team ${team?.name}" } // Debug message

                            // Give the player the items
                            it.player.inventory.setItem(4, ItemBuilder(Material.BOW).setName("§aArco").addLoreLine("TODO - Fazer a Lore kkkk").toItemStack() )
                            it.player.updateInventory()

                            // Teleport to the team's spawn
                            val loc = team?.location?: configuration.redTeamSpawn
                            it.player.teleport(loc.toLocation(arena))
                        }

                        return@scheduleSyncRepeatingTask
                    }
                    return@scheduleSyncRepeatingTask
                }
                ArenaStatus.INGAME -> {
                    getPlayers().forEach { it.player.level = 0; it.player.exp = 0f }
                    if (countdownTask <= ConfigValues.DefaultMatchTime.getInteger()) {
                        // TODO -> End the game.
                        return@scheduleSyncRepeatingTask
                    }
                    countdownTask--
                    // TODO -> Update BossBar
                }
                ArenaStatus.ENDING -> {
                    getPlayers().forEach { it.player.sendMessage("Ending") }
                }
            }
        }, 0L, 20L)
    }

    override fun end() {
        arenaStatus = ArenaStatus.ENDING
        getPlayers().forEach {
            it.player.sendMessage("Ending")
        }
    }

    override fun addPlayer(player: DodgePlayer): ArenaGame {
        if (arenaStatus == ArenaStatus.LOADING || arenaStatus == ArenaStatus.ENDING || arenaStatus == ArenaStatus.INGAME ) { throw Exception("Game is not in waiting state") }
        if (getPlayers().size >= configuration.maxPlayers) { throw Exception("Game is full") }

        // Call the join match event
        val event = PlayerJoinMatchEvent(player, this)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) { return this }

        // Add player to the game
        players.add(player)

        // Checks needed
        player.local_deaths = 0
        player.local_kills = 0
        player.game = this

        // Reset inventory
        player.player.inventory.clear()
        player.player.updateInventory()

        // Reset EXP
        player.player.level = 0
        player.player.exp = 0f

        player.player.teleport(configuration.spawn.toLocation(arena))

        getPlayers().forEach { it.player.sendMessage(Component.text("${player.player.name} entrou na partida! ${getPlayers().size} / ${configuration.maxPlayers}")) }
        return this
    }

    override fun removePlayer(player: DodgePlayer): ArenaGame {
        // Calls the leave match event
        val event = PlayerQuitMatchEvent(player, this)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) { return this }

        teams.find { it.players.contains(player) }?.players?.remove(player)
        players.remove(player)

        if (arenaStatus == ArenaStatus.WAITING || arenaStatus == ArenaStatus.STARTING) {
            getPlayers().forEach { it.player.sendMessage(Component.text("${player.player.name} saiu da partida! ${getPlayers().size} / ${configuration.maxPlayers}")) }
            return this
        }

        // Checks needed
        player.local_deaths = 0
        player.local_kills = 0
        player.game = null
        player.player.teleport(Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0))

        // Reset inventory
        player.player.inventory.clear()
        player.player.updateInventory()

        // Reset EXP
        player.player.level = 0
        player.player.exp = 0f

        getPlayers().forEach { it.player.sendMessage(Component.text("${player.player.name} saiu da partida!")) }
        return this
    }

    override fun getPlayers(): List<DodgePlayer> {
        return players
    }

    override fun getPlayer(player: DodgePlayer): DodgePlayer? {
        return getPlayers().find { it == player }
    }

    override fun getUUID(): String {
        return uuid
    }

    override fun getArenaStatus(): ArenaStatus {
        return arenaStatus
    }
}
