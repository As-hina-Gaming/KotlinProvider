package net.eratiem.kotlinprovider

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import org.slf4j.Logger
import javax.inject.Inject

@Plugin(
  id = "kotlinprovider",
  name = "KotlinProvider",
  version = "2.0.0-RC1",
  description = "EraTiem-Networks plugin to provide Kotlin for other Plugins",
  authors = [
    "Motzkiste"
  ],
  dependencies = []
)
class KotlinProviderPlugin @Inject constructor(
  private val logger: Logger
) {
  private val name = "KotlinProvider"

  @Subscribe(order = PostOrder.FIRST)
  fun onEnable(event: ProxyInitializeEvent) {
    logger.info("Kotlin can now be used!")
  }

  @Subscribe(order = PostOrder.LAST)
  fun onDisable(event: ProxyShutdownEvent) {
    logger.info("Kotlin is no longer useable!")
  }
}