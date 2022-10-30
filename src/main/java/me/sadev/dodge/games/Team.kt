package me.sadev.dodge.games

data class Team(
    val name: String,
    val players: MutableList<DodgePlayer>
)
