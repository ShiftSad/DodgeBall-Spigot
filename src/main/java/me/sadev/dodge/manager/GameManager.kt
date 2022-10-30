package me.sadev.dodge.manager

import me.sadev.dodge.games.*
import org.tinylog.kotlin.Logger

class GameManager {

    private val games = mutableListOf<DodgeGame>()

    fun createGame(gameType: DodgeGameMode): DodgeGame {
        Logger.info("Creating game of type $gameType")
        val game = SimpleGame(
            gameMode = gameType,
            templateName = "dodge-default",
            configuration = ArenaConfiguration(
                name = "Test",
                maxPlayers = 8,
                minPlayers = 6,
                spawn = SimplexLocation(0.0, 65.0, 0.0, 0.0f, 0.0f),
            )
        )
        games.add(game)
        return game
    }

    fun getGame(gameId: String): DodgeGame? {
        return games.find { it.getUUID() == gameId }
    }

    fun joinableGame(): DodgeGame? {
        return games.find { it.getArenaStatus() == ArenaStatus.WAITING }
    }
}