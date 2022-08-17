package net.eratiem.kotlinprovider.paper

import org.bukkit.plugin.java.JavaPlugin

class KotlinProviderPlugin: JavaPlugin() {

    override fun onEnable() {
        logger.info("Kotlin can now be used!")
    }

    override fun onDisable() {
        logger.info("Kotlin is no longer useable!")
    }
}