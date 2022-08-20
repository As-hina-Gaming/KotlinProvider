package net.eratiem.kotlinprovider.bungee

import net.md_5.bungee.api.plugin.Plugin

class KotlinProviderPlugin : Plugin() {

    override fun onEnable() {
        logger.info("Kotlin can now be used!")
    }

    override fun onDisable() {
        logger.info("Kotlin is no longer useable!")
    }
}