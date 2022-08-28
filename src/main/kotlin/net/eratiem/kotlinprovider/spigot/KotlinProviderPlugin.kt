package net.eratiem.kotlinprovider.spigot

import net.eratiem.eralogger.tools.EraLogger
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger

class KotlinProviderPlugin : JavaPlugin() {
    private lateinit var logger: EraLogger

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