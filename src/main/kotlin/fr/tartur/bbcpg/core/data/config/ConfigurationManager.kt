package fr.tartur.bbcpg.core.data.config

import de.exlll.configlib.YamlConfigurations
import java.nio.file.Path
import kotlin.io.path.exists

sealed interface Configurable

sealed interface ConfigurationProvider<T : Configurable> {
    val clazz: Class<T>
    fun default(): T
}

class ConfigurationManager(configurations: Map<Path, ConfigurationProvider<out Configurable>>) {
    val configurations: List<Configurable> = configurations.map { (path, configuration) ->
        loadOrCreate(path, configuration) }

    fun <T : Configurable> loadOrCreate(path: Path, provider: ConfigurationProvider<T>): T =
        if (path.exists()) {
            YamlConfigurations.load(
                path,
                provider.clazz
            )
        } else {
            val default = provider.default()

            YamlConfigurations.save(
                path,
                provider.clazz,
                default
            )

            default
        }

    inline fun <reified T> get(): T? {
        for (configuration in configurations) {
            if (configuration is T) {
                return configuration
            }
        }

        return null
    }
}