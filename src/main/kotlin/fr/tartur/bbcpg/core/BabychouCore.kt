package fr.tartur.bbcpg.core

import fr.tartur.bbcpg.core.data.UserManager
import fr.tartur.bbcpg.core.data.UserSource
import fr.tartur.bbcpg.core.data.config.ConfigurationManager
import fr.tartur.bbcpg.core.data.config.DatabaseCredentials
import fr.tartur.bbcpg.core.data.config.GameRules
import fr.tartur.bbcpg.core.events.GameRuleEvents
import fr.tartur.bbcpg.core.events.PlayerStreamEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Paths

class BabychouCore : JavaPlugin() {
    private val configurations: ConfigurationManager by lazy(::initConfigurations)
    private val source: UserSource by lazy(::initSource)
    private val users: UserManager by lazy(::initUsers)
    private val gameRules: GameRules by lazy(::initGameRules)

    override fun onEnable() {
        server.pluginManager.registerEvents(PlayerStreamEvent(users), this)
        server.pluginManager.registerEvents(GameRuleEvents(gameRules), this)
        logger.info("Bienvenue sur Babychou's Playground ! Amusez-vous bien !")
    }

    override fun onDisable() {
        source.close()
        logger.info("À bientôt sur Babychou's Playground !")
    }

    private fun initConfigurations(): ConfigurationManager {
        val base = Paths.get("plugins", name)

        return ConfigurationManager(mapOf(
            base.resolve("database", "credentials.yml") to DatabaseCredentials,
            base.resolve("gamerules.yml") to GameRules
        ))
    }

    private fun initSource() = UserSource(checkNotNull(configurations.get<DatabaseCredentials>()))

    private fun initUsers() = UserManager(source)

    private fun initGameRules(): GameRules = checkNotNull(configurations.get())
}