package net.eratiem.kotlinprovider

import net.md_5.bungee.api.plugin.Plugin
import java.util.logging.Logger

class KotlinProviderPlugin : Plugin() {
    private lateinit var logger: Logger
    private val name = "KotlinProvider"

    override fun onEnable() {
        logger = getLogger()
        logger.info("Kotlin can now be used!")
    }

    override fun onDisable() {
        logger.info("Kotlin is no longer useable!")
    }
}