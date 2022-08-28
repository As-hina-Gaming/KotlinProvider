package net.eratiem.kotlinprovider.bungee

import net.eratiem.eralogger.tools.EraLogger
import net.md_5.bungee.api.plugin.Plugin
import org.slf4j.Logger

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