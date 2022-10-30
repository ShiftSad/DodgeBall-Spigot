package me.sadev.dodge.manager

import me.sadev.dodge.games.DodgeGame
import me.sadev.dodge.games.DodgeGameMode
import me.sadev.dodge.games.SimpleGame

class GameManager {

    private val games = mutableListOf<DodgeGame>()

    fun createGame(gameType: DodgeGameMode): DodgeGame {
        val game = SimpleGame(gameType, "dodgeball-default")
        games.add(game)
        return game
    }

    fun getGame(gameId: String): DodgeGame? {
        return games.find { it.getUUID() == gameId }
    }
}