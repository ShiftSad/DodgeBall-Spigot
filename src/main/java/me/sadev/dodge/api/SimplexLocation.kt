package me.sadev.dodge.api

import org.bukkit.World

data class SimplexLocation(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    fun toLocation(world: World): org.bukkit.Location {
        return org.bukkit.Location(world, x, y, z, yaw, pitch)
    }
}
