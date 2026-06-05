package fr.tartur.bbcpg.core.data.config

import de.exlll.configlib.Comment
import de.exlll.configlib.Configuration

enum class DatabaseType {
    SQLITE {
        override fun uri(host: String) = "jdbc:sqlite:$host"
    },
    MYSQL {
        override fun uri(host: String) = "jdbc:mysql://$host"
    };

    abstract infix fun uri(host: String): String
}

@Configuration
data class DatabaseCredentials(
    @Comment("Valid values: 'mysql' (chosen by default), 'sqlite'")
    var type: DatabaseType = DatabaseType.MYSQL,
    var host: String = "localhost",
    var user: String = "root",
    var password: String = "",
) {}