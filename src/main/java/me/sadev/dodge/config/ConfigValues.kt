package me.sadev.dodge.config

enum class ConfigValues(private var value: Any) {
    DefaultTimer(45),
    DefaultEnoughPlayersTimer(15),
    DefaultMatchTime(700);

    fun getValue(): Any {
        return value
    }

    fun getInteger(): Int {
        return value as Int
    }
}
