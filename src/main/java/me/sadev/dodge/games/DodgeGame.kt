package me.sadev.dodge.games

interface DodgeGame {

    fun start()

    fun addPlayer(player: DodgePlayer) : DodgeGame
    fun removePlayer(player: DodgePlayer) : DodgeGame

    fun getPlayers(): List<DodgePlayer>
    fun getPlayer(player: DodgePlayer): DodgePlayer?

    fun getGameMode(): DodgeGameMode

    fun getUUID(): String
}