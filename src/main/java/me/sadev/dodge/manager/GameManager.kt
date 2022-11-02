package me.sadev.dodge.manager

import me.sadev.dodge.api.SimplexLocation
import me.sadev.dodge.arena.SimpleArena
import me.sadev.dodge.arena.*
import me.sadev.dodge.arena.enums.ArenaStatus
import me.sadev.dodge.arena.enums.ArenaMode
import org.tinylog.kotlin.Logger

class GameManager {

    private val games = mutableListOf<ArenaGame>()

    fun createGame(gameType: ArenaMode): ArenaGame {
        Logger.info("Creating game of type $gameType")
        val game = SimpleArena(
            gameMode = gameType,
            templateName = "dodge-default",
            configuration = ArenaConfiguration(
                name = "Test",
                maxPlayers = 8,
                minPlayers = 2,
                spawn = SimplexLocation(0.0, 65.0, 0.0, 0.0f, 0.0f),
                redTeamSpawn = SimplexLocation(0.0, 65.0, 64.0, 0.0f, 0.0f),
                blueTeamSpawn = SimplexLocation(64.0, 65.0, 0.0, 0.0f, 0.0f)
            )
        )
        games.add(game)
        return game
    }

    fun getGame(gameId: String): ArenaGame? {
        return games.find { it.getUUID() == gameId }
    }

    fun joinableGame(): ArenaGame? {
        return games.filter { it.getArenaStatus() == ArenaStatus.WAITING }.maxByOrNull { it.getPlayers().size }
    }
}
