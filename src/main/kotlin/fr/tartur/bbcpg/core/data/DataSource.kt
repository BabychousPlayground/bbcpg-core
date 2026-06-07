package fr.tartur.bbcpg.core.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fr.tartur.bbcpg.core.data.config.DatabaseCredentials
import fr.tartur.bbcpg.core.data.config.DatabaseType
import org.bukkit.Bukkit
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.SQLException
import java.util.UUID
import kotlin.io.path.createFile
import kotlin.io.path.exists

sealed class DataSource(credentials: DatabaseCredentials) {
    private val credentials: DatabaseCredentials
    private val source: HikariDataSource

    init {
        this.credentials = credentials
        source = HikariDataSource(HikariConfig().run {
            jdbcUrl = credentials.type uri credentials.host
            username = credentials.user
            password = credentials.password
            this
        })
        initialize(Paths.get(credentials.init))
    }

    fun connect(): Connection =
        try {
            return source.connection
        } catch (_: SQLException) {
            println("Could not connect to database '${credentials.host}'.")
            initialize(Paths.get(credentials.init))
            return source.connection
        }

    fun close() {
        println("Closing data source connection...")
        source.close()
        println("Connection closed successfully.")
    }

    fun initialize(init: Path) {
        println("Initializing database '${credentials.host}'...")

        if (credentials.type == DatabaseType.SQLITE) {
            createIfNotExists(Paths.get(credentials.host))
        }

        println("Retrieving SQL initialization request...")
        val request = Files.readString(createIfNotExists(init))

        if (request.isBlank()) {
            println("Failed initializing database: script is empty.")
            return
        }

        println("Success!")

        connect().use {
            println("Sending SQL request...")
            val statement = it.prepareStatement(request)
            statement.executeUpdate()
            println("Success!")
        }
    }

    private fun createIfNotExists(path: Path): Path =
        if (!path.exists()) {
            println("Could not find file $path. Generating it...")
            val result = path.createFile()
            println("Success!")
            result
        } else path
}

class UserSource(credentials: DatabaseCredentials) : DataSource(credentials) {
    fun retrieve(uuid: UUID): User? = connect().use {
        val player = Bukkit.getOfflinePlayer(uuid)
        println("Fetching data of ${player.name}...")

        val query = """
                SELECT coins, level, experience FROM babychous
                WHERE uuid = ?;
            """.trimIndent()
        val statement = it.prepareStatement(query)
        statement.setString(1, uuid.toString())
        val result = statement.executeQuery()

        if (result.next() && player.isOnline) {
            println("Success!")
            User(
                checkNotNull(player.player), // Obviously not null, but turns Player? to Player.
                coins = result.getInt(1),
                level = result.getInt(2),
                experience = result.getFloat(3),
            )
        } else {
            println("No result or player is offline.")
            null
        }
    }

    fun create(uuid: UUID): Boolean = connect().use {
        val player = Bukkit.getOfflinePlayer(uuid)
        println("Creating data of new player ${player.name}...")
        val query = """
            INSERT INTO babychous(uuid, name, coins, level, experience)
            VALUES(?, ?, ?, ?, ?);
        """.trimIndent()
        val statement = it.prepareStatement(query)
        statement.setString(1, uuid.toString())
        statement.setString(2, player.name)
        statement.setInt(3, 0)
        statement.setInt(4, 1)
        statement.setFloat(5, 0f)
        val result = statement.executeUpdate() > 0

        if (result) {
            println("Success!")
            true
        } else {
            println("No data created.")
            false
        }
    }

    fun update(player: User): Boolean = connect().use {
        println("Updating data of player ${player.spigot.name}...")
        val query = """
            UPDATE babychous SET coins = ?, level = ?, experience = ?
            WHERE uuid = ?;
        """.trimIndent()
        val statement = it.prepareStatement(query)
        statement.setInt(1, player.coins)
        statement.setInt(2, player.level)
        statement.setFloat(3, player.experience)
        statement.setString(4, player.spigot.uniqueId.toString())
        val result = statement.executeUpdate() > 0

        if (result) {
            println("Success!")
            true
        } else {
            println("No data to update.")
            false
        }
    }
}