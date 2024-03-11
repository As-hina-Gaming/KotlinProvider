package net.eratiem.kotlinprovider

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class KotlinProviderPlugin : JavaPlugin() {
  private lateinit var logger: Logger

  override fun onEnable() {
    logger = getLogger()
    logger.info("Kotlin can now be used!")
  }

  override fun onDisable() {
    logger.info("Kotlin is no longer usable!")
  }
}