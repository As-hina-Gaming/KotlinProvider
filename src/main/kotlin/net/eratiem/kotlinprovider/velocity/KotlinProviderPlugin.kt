package net.eratiem.kotlinprovider.velocity;

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import java.util.logging.Logger
import javax.inject.Inject

@Plugin(
    id = "kotlinprovider", name = "KotlinProvider", version = "1.7.20-Beta",
    description = "EraTiem-Networks plugin to provide Kotlin for other Plugins", authors = ["Motzkiste"]
)
class KotlinProviderPlugin @Inject constructor(private val logger: Logger) {

    @Subscribe
    fun onEnable(event: ProxyInitializeEvent) {
        logger.info("Kotlin can now be used!")
    }

    @Subscribe
    fun onDisable(event: ProxyShutdownEvent) {
        logger.info("Kotlin is no longer useable!")
    }
}
