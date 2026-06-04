package fr.tartur.bbcpg.core

import de.exlll.configlib.YamlConfigurations
import fr.tartur.bbcpg.core.data.DataSource
import fr.tartur.bbcpg.core.data.config.DatabaseCredentials
import fr.tartur.bbcpg.core.data.config.DatabaseType
import fr.tartur.bbcpg.core.events.PlayerStreamEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths
import kotlin.io.path.exists

class BabychouCore : JavaPlugin() {
    private var data: DataSource? = null

    override fun onEnable() {
        loadConfigurations()

        server.pluginManager.registerEvents(PlayerStreamEvent(), this)
        logger.info("Bienvenue sur Babychou's Playground ! Amusez-vous bien !")
    }

    override fun onDisable() {
        logger.info("À bientôt sur Babychou's Playground !")
    }

    private fun loadConfigurations() {
        val playersPath = Paths.get("plugins", name, "database", "credentials.yml")
        val credentials = if (playersPath.exists()) {
            YamlConfigurations.load(
                playersPath,
                DatabaseCredentials::class.java
            )
        } else {
            val credentials = DatabaseCredentials(
                DatabaseType.SQLITE,
                "babychous.db",
                "",
                ""
            )
            YamlConfigurations.save(
                playersPath,
                DatabaseCredentials::class.java,
                credentials
            )
            credentials
        }

        data = DataSource(credentials)
    }
}