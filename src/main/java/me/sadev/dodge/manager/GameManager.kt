package me.sadev.dodge.manager

import me.sadev.dodge.api.SimplexLocation
import me.sadev.dodge.arena.SimpleArena
import me.sadev.dodge.arena.*
import me.sadev.dodge.arena.enums.ArenaStatus
import me.sadev.dodge.arena.enums.ArenaMode
import org.tinylog.kotlin.Logger

class GameManager {

    private val games = mutableListOf<ArenaGame>()

    fun createGame(force: Boolean): ArenaGame {
        if (joinableGame() != null && !force) return joinableGame()!!
        val game = SimpleArena(
            templateName = "dodge-default",
            configuration = ArenaConfiguration(
                name = "Test",
                maxPlayers = 8,
                minPlayers = 2,
                spawn = SimplexLocation(0.0, 70.0, 0.0, 0.0f, 0.0f),

                redTeamSpawn = SimplexLocation(0.5, 69.0, -7.5, 0.0f, 0.0f),
                blueTeamSpawn = SimplexLocation(0.5, 69.0, 8.5, -180.0f, 0.0f),

                middlePointOne = SimplexLocation(19.0, 68.0, 0.0, 0.0f, 0.0f),
                middlePointTwo = SimplexLocation(-19.0, 71.0, 0.0, 0.0f, 0.0f),
            )
        )
        games.add(game)
        return game
    }

    fun getGame(gameId: String): ArenaGame? {
        return games.find { it.getUUID() == gameId }
    }

    fun joinableGame(): ArenaGame? {
        return games.filter { it.getArenaStatus() == ArenaStatus.WAITING || it.getArenaStatus() == ArenaStatus.STARTING }.maxByOrNull { it.getPlayers().size }
    }
}
