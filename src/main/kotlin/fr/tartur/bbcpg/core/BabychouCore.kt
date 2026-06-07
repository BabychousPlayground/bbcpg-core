package fr.tartur.bbcpg.core

import fr.tartur.bbcpg.core.data.UserManager
import fr.tartur.bbcpg.core.data.UserSource
import fr.tartur.bbcpg.core.data.config.ConfigurationManager
import fr.tartur.bbcpg.core.data.config.DatabaseCredentials
import fr.tartur.bbcpg.core.events.PlayerStreamEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

class BabychouCore : JavaPlugin() {
    private var source: UserSource? = null

    override fun onEnable() {
        val configurations = ConfigurationManager(mapOf(
            Paths.get("plugins", name, "database", "credentials.yml") to DatabaseCredentials
        ))
        source = UserSource(checkNotNull(configurations.get<DatabaseCredentials>()))
        val users = UserManager(checkNotNull(source))

        server.pluginManager.registerEvents(PlayerStreamEvent(users), this)
        logger.info("Bienvenue sur Babychou's Playground ! Amusez-vous bien !")
    }

    override fun onDisable() {
        source?.close()
        logger.info("À bientôt sur Babychou's Playground !")
    }
}