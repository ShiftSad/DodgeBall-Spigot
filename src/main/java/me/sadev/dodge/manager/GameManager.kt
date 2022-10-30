package me.sadev.dodge.manager

import me.sadev.dodge.games.DodgeGame
import me.sadev.dodge.games.DodgeGameMode
import me.sadev.dodge.games.SimpleGame
import org.tinylog.kotlin.Logger

class GameManager {

    private val games = mutableListOf<DodgeGame>()

    fun createGame(gameType: DodgeGameMode): DodgeGame {
        Logger.info("Creating game of type $gameType")
        val game = SimpleGame(gameType, "dodge-default")
        games.add(game)
        return game
    }

    fun getGame(gameId: String): DodgeGame? {
        return games.find { it.getUUID() == gameId }
    }
}