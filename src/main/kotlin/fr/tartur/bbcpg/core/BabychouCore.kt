package fr.tartur.bbcpg.core

import fr.tartur.bbcpg.core.events.PlayerStreamEvent
import org.bukkit.plugin.java.JavaPlugin

class BabychouCore : JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.registerEvents(PlayerStreamEvent(), this)
        logger.info("Bienvenue sur Babychou's Playground ! Amusez-vous bien !")
    }

    override fun onDisable() {
        logger.info("À bientôt sur Babychou's Playground !")
    }
}