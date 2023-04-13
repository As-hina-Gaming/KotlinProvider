package net.eratiem.kotlinprovider

import net.eratiem.eralogger.tools.EraLogger
import net.md_5.bungee.api.plugin.Plugin
import java.util.logging.Logger

class KotlinProviderPlugin : Plugin() {
    private lateinit var logger: EraLogger
    private val name = "KotlinProvider"

    override fun onEnable() {
        logger = EraLogger.getInstance(name, getLogger() as Logger)
        logger.info("Kotlin can now be used!")
    }

    override fun onDisable() {
        logger.info("Kotlin is no longer useable!")
        if (this::logger.isInitialized)
            EraLogger.destroyInstance(name)
    }
}