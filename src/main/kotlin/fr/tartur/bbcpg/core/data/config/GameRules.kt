package fr.tartur.bbcpg.core.data.config

import de.exlll.configlib.Configuration

@Configuration
data class GameRules(
    val loseFood: Boolean = true,
    val loseHealth: Boolean = true,
) : Configurable {
    companion object : ConfigurationProvider<GameRules> {
        override val clazz: Class<GameRules>
            get() = GameRules::class.java
    }
}